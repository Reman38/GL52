package fr.utbm.gl52.droneSimulator.model;

import fr.utbm.gl52.droneSimulator.model.exception.OutOfMainAreaException;
import fr.utbm.gl52.droneSimulator.view.SimulationWindowView;

import java.util.*;

import static fr.utbm.gl52.droneSimulator.controller.ControllerHelper.isSameCoord;
import static fr.utbm.gl52.droneSimulator.model.MathHelper.*;
import static fr.utbm.gl52.droneSimulator.model.Parcel.loadParcelAtCoord;

public class Drone extends CenteredAndSquaredSimulationElement implements Runnable{
    // constantes
    static final private Float speed = 18f; // 41 mph, 65 km/h, 18m/s
    static final private Integer visibleDistance = 100000;
    private static final float RADIUS = 10f;

    // attributs
    private Boolean isBusy;
    private Boolean isLoaded;
    private Float batteryFullCapacity;
    private Float batteryCapacity;
    private Float rotation = 0f; //radians
    private Float weightCapacity;
    private Memory.ParcelRecord targetParcel = null;
    private Float[] geographicalTarget = new Float[2];

    private Boolean hasStartedToCharge = false;
    private Boolean isOutOfBattery = false;

    private ChargingStation targetChargingStation = null;

    private Long t1 = System.nanoTime();
    private Long t2 = System.nanoTime();
    private Long deltaTSimStep = 0L;
    private Long realDeltaT = 0L;

    private Memory memory;
    private boolean isLanded = false;

    /**
     * Run the drone simulation
     */
    @Override
    public void run() {

        waitForViewToLoad();

        droneRoutine();
    }

    /**
     * Execute drone logic at each step
     */
    private void droneRoutine() {
        while (!Thread.currentThread().isInterrupted()) {
            t1 = System.nanoTime();
            parcelMemoryUpdate();
            //System.out.println(memory.toString());
            manageRotation();
            parcelRoutine();
            batteryRoutine();
            threadSleep();
            t2 = System.nanoTime();
            manageDronesSpeedCoefAccordingToSimAcceleration();
            move(deltaTSimStep);
        }
    }

    /**
     * Parcel related routine
     */
    private void parcelRoutine() {
        if(!isBusy){
            selectParcel();
        } else {
            if (targetChargingStation == null) {
                if (!isLoaded) {
                    if (isTargetParcelUnavailable()) {
                        resetTargetParcel();
                    } else if (!areCoordsNull(geographicalTarget)) {
                        if (isInRadius(geographicalTarget)) {
                            loadParcel();
                        }
                    }
                } else if (!areCoordsNull(geographicalTarget)) {
                    if (isInRadius(geographicalTarget)) {
                        deliverParcel();
                    }
                }
            }
        }
    }

    /**
     * Battery related routine
     */
    private void batteryRoutine() {
        if (couldNotGoToNearestChargingStationAtNextStep() && targetChargingStation == null) {
            targetChargingStation();
        }
        if(targetChargingStation != null){
            if(isInRadius(geographicalTarget)) {
                if (targetChargingStation.isBusy() && !targetChargingStation.isCurrentDroneReloading(this)) {
                    land();
                } else {
                    connectToChargingStation();
                    if (isBatteryFull()) {
                        leaveChargingStation();
                    }
                }
            }
        }
    }

    /**
     * Leave the charging station
     */
    private void leaveChargingStation() {
        hasStartedToCharge = false;
        Message.endOfCharge(this);
        targetChargingStation.freeChargingStation();
        targetChargingStation = null;
        resumeDelivery();
        takeOff();
    }

    /**
     * If a delivery was initiated before reloading at charging station, resume it
     */
    private void resumeDelivery() {
        if(targetParcel != null) {
            geographicalTarget = isLoaded ? targetParcel.getDestCoords() : targetParcel.getCoords();
        }
    }

    /**
     * Plug the drone to a charging station
     */
    private void connectToChargingStation() {
        if(!hasStartedToCharge){
            hasStartedToCharge = true;
            Message.startToCharge(this);
        }

        targetChargingStation.reloadDrone(this, deltaTSimStep);
        land();
    }

    /**
     * Target a charging station
     */
    private void targetChargingStation() {
        targetChargingStation = getClosestChargingStation();
        geographicalTarget = targetChargingStation.getCoord();
        Message.targetChargingStation(this, targetChargingStation.getId());
    }

    private void takeOff() {
        isLanded = false;
    }

    /**
     * Check if the battery is fully loaded
     *
     * @return true if yes
     */
    private Boolean isBatteryFull() {
        return batteryCapacity >= batteryFullCapacity;
    }

    /**
     * Check if the drone could reach a charging station at the next simulation step
     *
     * @return true if it couldn't
     */
    private Boolean couldNotGoToNearestChargingStationAtNextStep() {
       ChargingStation nearestChargingStation = getClosestChargingStation();
       Float batteryNeededToJoin = getBatteryNeededToJoin(nearestChargingStation);
       return batteryCapacity < batteryNeededToJoin + 1;
    }

    /**
     * Check the battery capacity needed to join an element
     *
     * @param se The element to join
     *
     * @return The battery duration needed in minutes
     */
    private Float getBatteryNeededToJoin(CenteredAndSquaredSimulationElement se) {
        Float norm = computeVectorNorm(this, se);
        return ((norm / speed)/60);
    }

    /**
     * Rotate the drone on the direction of it target
     */
    private void manageRotation() {
        if(!areCoordsNull(geographicalTarget)){
            rotation = calculAngleWith(getX(), geographicalTarget[0], getY(), geographicalTarget[1]);
        }
    }

    /**
     * Deliver the loaded parcel
     */
    private void deliverParcel() {
        Long deltaT = realDeltaT;
        Message.deliverParcel(this, targetParcel.id);
        isLoaded = false;
        isBusy = false;
        geographicalTarget = null;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        realDeltaT = deltaT;
    }

    /**
     * Load the target parcel
     */
    private void loadParcel() {
        Message.loadParcel(this, targetParcel.id);
        isLoaded = true;

        loadParcelAtCoord(targetParcel.getCoords(), this);
        geographicalTarget = targetParcel.getDestCoords();
    }

    /**
     * Reset the target parcel
     */
    private void resetTargetParcel() {
        Message.parcelDisappear(this, targetParcel.id);
        targetParcel = null;
        geographicalTarget = null;
        isBusy = false;
    }

    /**
     * Check if the targeted parcel still exist
     *
     * @return true if not
     */
    private boolean isTargetParcelUnavailable() {
        return targetParcel != null && !isParcelInMemory(targetParcel);
    }

    /**
     * select a target parcel
     */
    private void selectParcel() {
        targetParcel = getClosestLoadableParcel();
        if(targetParcel != null) {
            geographicalTarget = targetParcel.getCoords();
            isBusy = true;
            Message.chooseClosestParcel(this, targetParcel.id);
        }
    }

    /**
     * Wait until the view is loaded to start the simulation
     */
    private void waitForViewToLoad() {
        while(!SimulationWindowView.isViewFullyLoaded()){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Check if coords are null
     *
     * @param coords coords to check
     *
     * @return true if yes
     */
    private boolean areCoordsNull(Float[] coords) {
        return coords == null || coords[0] == null || coords[1] == null;
    }

    /**
     * Check if the geographical is in the given radius
     *
     * @param geographicalTarget the geographical target to check
     * @return true if yes
     */
    private Boolean isInRadius(Float[] geographicalTarget) {
        return !(isValueOutOfRange(getX(), geographicalTarget[0] - (Float) Drone.RADIUS, geographicalTarget[0] + (Float) Drone.RADIUS) || isValueOutOfRange(getY(), geographicalTarget[1] - (Float) Drone.RADIUS, geographicalTarget[1] + (Float) Drone.RADIUS));
    }

    /**
     * Get the closest loadable parcel to the drone in it memory
     *
     * @return a record of the closest parcel
     */
    private Memory.ParcelRecord getClosestLoadableParcel() {
        Memory.ParcelRecord minParcelRecord = null;
        if(memory.parcelRecords.size() > 0) {
            Float min = null;

            for (Memory.ParcelRecord parcel : memory.parcelRecords) {
                float value = computeVectorNorm(getX(), parcel.getCoords()[0], getY(), parcel.getCoords()[1]);
                if (min == null || value < min) {
                    if(parcel.getWeight() <= weightCapacity) {
                        min = value;
                        minParcelRecord = parcel;
                    }
                }
            }
        }
        return minParcelRecord;
    }

    /**
     * Get the closest charging station to the drone in it memory
     *
     * @return a record of the closest charging station
     */
    private ChargingStation getClosestChargingStation(){

        ChargingStation minChargingStation;
        float min = computeVectorNorm(getX(), Simulation.getChargingStations().get(0).getCoord()[0], getY(), Simulation.getChargingStations().get(0).getCoord()[1]);
        minChargingStation = Simulation.getChargingStations().get(0);

        for (ChargingStation chargingStation : Simulation.getChargingStations()) {
            float value = computeVectorNorm(getX(), chargingStation.getCoord()[0], getY(), chargingStation.getCoord()[1]);
            if (value < min) {
                min = value;
                minChargingStation = chargingStation;
            }
        }
        return minChargingStation;
    }

    /**c
     * Add the new discovered parcel in the memory of the drone
     * Remove the parcels that became unavailable form the memory of the drone
     */
    private void parcelMemoryUpdate() {
        Parcel parcel;
        for(int i = 0; i < Simulation.getParcels().size(); i++){
            parcel = Simulation.getParcels().get(i);
            if (detect(parcel)) {
                if(!isParcelInMemory(parcel)){
                    memory.add(parcel);
                }
            }
        }

        memory.parcelRecords.removeIf(parcelRecord -> !isParcelStillAvailable(parcelRecord));
    }

    /**
     * Check if the given parcel is already in the drone memory
     *
     * @param parcel The checked parcel
     *
     * @return True if yes
     */
    private boolean isParcelInMemory(Parcel parcel) {
        Memory.ParcelRecord parcelRecord = memory.createParcelRecord(parcel);
        return isParcelInMemory(parcelRecord);
    }

    /**
     * Check if the given parcel record is already in the available parcels
     *
     * @param parcel The checked parcel record
     *
     * @return True if yes
     */
    private boolean isParcelInMemory(Memory.ParcelRecord parcel) {
        boolean res = false;
        for(Memory.ParcelRecord parcelRecord: memory.parcelRecords){
            res = isSameCoord(parcel.getCoords(), parcelRecord.getCoords());
            if(res){
                res = parcel.getPopTime().equals(parcelRecord.getPopTime());
                if(res){
                    parcelRecord.setLastDetectedDateTime(new Date());
                    break;
                }
            }
        }
        return res;
    }

    /**
     * Check the parcel record is still available in the simulation
     *
     * @param parcelRecord the parcel to check
     *
     * @return true if yes
     */
    private boolean isParcelStillAvailable(Memory.ParcelRecord parcelRecord) {
        boolean res = false;
        for(Parcel parcel: Simulation.getParcels()){
            if(!parcel.isInJourney()) {
                res = isSameCoord(parcel.getCoord(), parcelRecord.getCoords());
                if (res) {
                    res = parcel.getPopTime().equals(parcelRecord.getPopTime());
                    if (res) {
                        parcelRecord.setLastDetectedDateTime(new Date());
                        break;
                    }
                }
            }
        }
        return res;
    }

    /**
     * Accelerate simulation by increasing drone speed.
     */
    private void manageDronesSpeedCoefAccordingToSimAcceleration() {
        realDeltaT = StrictMath.abs(t2 - t1);
        deltaTSimStep = (long)(realDeltaT * Simulation.getSimulationSpeed());
    }

    /**
     * Iteration step within the simulation
     */
    private void threadSleep() {
        try {
            Thread.sleep((long) (1000 / Simulation.getImagesPerSecond()));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Constructor of the drone
     *
     * @param id Unique ID of the drone
     */
    public Drone(Integer id) {
        super(id, .8f);

        isBusy = false;
        isLoaded = false;
        batteryFullCapacity = Simulation.getDroneBatteryCapacity()[1];
        batteryCapacity = batteryFullCapacity;

        memory = new Memory();
    }

    /**
     * Create a drone with randomized parameters
     *
     * @param id Id of the drone
     *
     * @return The created drone
     */
    static Drone createRandomizedDrone(Integer id) {
        Drone drone = new Drone(id);
        try {
            drone.randomize();
        } catch (OutOfMainAreaException e) {
            e.printStackTrace();
        }
        return drone;
    }

    /**
     * Randomize the position  and the weight capacity of the drone
     *
     * @throws OutOfMainAreaException Position is out of main area
     */
    public void randomize() throws OutOfMainAreaException {
        setRandCoord();
        setRandRotation();
        setRandWeightCapacity();
    }

    /**
     * Set a random weight capacity between parcel min weight and max
     */
    private void setRandWeightCapacity() {
        setWeightCapacity(RandomHelper.getRandFloat(Simulation.getDroneWeightCapacity()[0], Simulation.getDroneWeightCapacity()[1]));
    }

    /**
     * Move the drone of one step
     *
     * @param deltaT time elapsed since the begining of the step
     */
    public void move(long deltaT) {
        //System.out.println("deltaT "+ deltaT);
        if(!isLanded) {
            deltaT = avoidNullDeltaT(deltaT);
            if (!isBatteryEmpty()) {
                float deltaTSec = convertNanosecondsToSeconds(deltaT);
                //System.out.println("deltaTSec " + deltaTSec);

                Float newX = getX() + (getSpeed() * deltaTSec * (float) Math.cos(rotation));
                Float newY = getY() + (getSpeed() * deltaTSec * (float) Math.sin(-rotation));

                /*Float norm = computeVectorNorm(getX(), newX, getY(), newY);
                consumeBattery(norm);*/
                consumeBatteryDuring(deltaT);
                //printDroneSpeedForDebug(deltaTSec, newX, newY);

                tryToMoveDroneWithinBoundaries(deltaT, newX, newY);
//                System.out.println("newX " + newX + " newY " + newY);
            } else {
                printOutOfBattery();
            }
        }
    }

    /**
     * Print of out battery log
     */
    private void printOutOfBattery() {
        if(!isOutOfBattery){
            Message.outOfBattery(this);
            isOutOfBattery = true;
        }
    }

    /**
     * Move drone within the boundaries of the main area
     *
     * @param deltaT time elapsed since the last iteration
     * @param newX new abscissa candidate in meters
     * @param newY new ordinate candidate in meters
     */
    private void tryToMoveDroneWithinBoundaries(long deltaT, Float newX, Float newY) {
        try {
            setCoord(newX, newY);
        } catch (OutOfMainAreaException e) {
            if (isNewXOutOfBoundaries(newX)) {
                rotation += MathHelper.getPi()/3;
            }
            if (isNewYOutOfMainArea(newY)) {
                rotation += MathHelper.getPi()/3;
            }

            move(deltaT);
        }
    }

    /**
     * Check if ordinate is out of main area
     *
     * @param newY value to check
     *
     * @return true if yes
     */
    private boolean isNewYOutOfMainArea(Float newY) {
        return isValueOutOfRange(newY, Simulation.getMainArea().getY(), Simulation.getMainArea().getHeight());
    }

    /**
     *  Check if the value is out of range
     *
     * @param newPt Value to check
     * @param min Min limit
     * @param max Max limit
     *
     * @return True if yes
     */
    private boolean isValueOutOfRange(Float newPt, Float min, Float max) {
        return newPt < min || newPt > max;
    }

    /**
     * Check if abscissa is out of main area
     *
     * @param newX value to check
     *
     * @return true if yes
     */
    private boolean isNewXOutOfBoundaries(Float newX) {
        return isValueOutOfRange(newX, Simulation.getMainArea().getX(), Simulation.getMainArea().getWidth());
    }

    /**
     * Forbid deltaT to be null
     *
     * @param deltaT the non nullable deltaT in nanoseconds
     *
     * @return a non null value
     */
    private long avoidNullDeltaT(long deltaT) {
        if(deltaT == 0L){
            deltaT = nanosecondsInASecond;
        }
        return deltaT;
    }

    /*private void printDroneSpeedForDebug(float deltaTsec, Float newX, Float newY) {
        float dist = computeVectorNorm(getX(), newX, getY(), newY);
        System.out.println("dist "+ dist);
        System.out.println("Drone speed " + computeCurrentSpeed(dist,deltaTsec));
        System.out.println("real drone speed " + dist/(convertNanosecondsToSeconds(realDeltaT)));
    }*/

   /* private Float computeCurrentSpeed(Float norm, Float deltaTsec){
        return norm/deltaTsec;
    }*/

    /**
     * Rotate the drone
     *
     * @param f angle in rad
     */
    public void rotate(Float f) {
        setRotation(getRotation() + f);
    }

    /**
     * Check if the drone can detect the element
     *
     * @param se simulation element to check
     *
     * @return True if yes
     */
    public Boolean detect(SimulationElement se) {
        return MathHelper.calculDistanceWith(this, se) < getDetectionRange();
    }

    private void exchangeData(Drone drone) {
        // TODO appeler à l'aide pour porter un colis
        // TODO detect drones
        // TODO detect parcels
        // data.merge(drone.data);
        // drone.data.merge(data);
    }

    /**
     * Check if the parcel is transportable
     *
     * @param parcel Parcel to check
     *
     * @return True if yes
     */
    private Boolean isTransportable(Parcel parcel) {
        return true; // TODO
    }

   /* public void handleDroneInteractions() {
        ArrayList<Drone> drones = Simulation.getDrones();

        for (Integer j = 0; j < drones.size(); ++j) {
            Drone drone = drones.get(j);
            if (this != drone && detect(drone)) {
                exchangeData(drone);
            }
        }
    }

    public void handleParcelInteractions() {
        ArrayList<Parcel> detectedParcels = getDetectedParcel();

        for (Parcel parcel : detectedParcels) {
            memory.add(parcel);
        }
    }*/

    /**
     * Get a list of all detected parcel
     *
     * @return a list of parcels
     */
    private ArrayList<Parcel> getDetectedParcel() {
        ArrayList<Parcel> detectedParcel = new ArrayList<>();

        // TODO : améliorer le réalisme, ici on boucle sur les paquets présent dans la simulation..
        for (Parcel parcel : Simulation.getParcels()) {
            if (detect(parcel)) {
                detectedParcel.add(parcel);
            }
        }

        return detectedParcel;
    }

   /* public Boolean isOverThe(Parcel parcel) {
        return MathHelper.calculDistanceWith(this, parcel) < getSize() / 2;
    }

    public void interactWith(Parcel parcel) {
        if (isTransportable(parcel))
            load(parcel);
        else
            callHelp();
    }*/

    /*private void callHelp() {
//        TODO
    }*/

    /**
     * Load parcel
     *
     * @param parcel parcel to load
     */
    public void load(Parcel parcel) {
        Simulation.removeParcel(parcel);
    }

    /**
     * Land the drone
     */
    private void land() {
        isLanded = true;
    }

    /**
     * Increase battery load
     *
     * @param nanoSeconds elapsed time since the last step
     */
    void chargeBatteryDuring(Long nanoSeconds){
        batteryCapacity += convertNanosecondsToMinutes(nanoSeconds) / 2;
        if(batteryCapacity > batteryFullCapacity){
            batteryCapacity = batteryFullCapacity;
        }
    }

    /**
     * Reduce battery load
     *
     * @param nanoSeconds elapsed time since the last step
     */
    private void consumeBatteryDuring(Long nanoSeconds){
        batteryCapacity -= convertNanosecondsToMinutes(nanoSeconds);
        if(batteryCapacity <= 0){
            batteryCapacity = 0f;
        }
    }

    /**
     * Reduce the load of the battery according to the fly distance
     *
     * @param norm Fly distance
     */
    public void consumeBattery(Float norm){
        batteryCapacity -= (norm / getSpeed())/60;
        if(batteryCapacity <= 0){
            batteryCapacity = 0f;
        }
    }

    private Boolean isBatteryEmpty(){
        return batteryCapacity <= 0f;
    }

    private void setRotation(Float radian) {
        rotation = MathHelper.simplifyAngle(radian);
    }

    public void setRotation(SimulationElement ge) {
        Float rotation = MathHelper.calculAngleWith(this, ge);
        setRotation(rotation);
    }

    private void setRandRotation() {
        setRotation(RandomHelper.getRandFloat(0f, (float) (2 * Math.PI)));
    }

    public void setRandCoord() throws OutOfMainAreaException {
        setRandCoord(Simulation.getMainArea());
    }

    public class Memory {
        ArrayList<ParcelRecord> parcelRecords = new ArrayList<ParcelRecord>();

        public class ParcelRecord {
            private Integer id;
            private Float[] coords;
            private Float[] destCoords;
            private Date lastDetectedDateTime;
            private Date popTime;
            private Float weight;

            public ParcelRecord(Parcel p) {
                id = p.getId();
                coords = p.getCoord();
                destCoords = p.getDestCoord();
                popTime = p.getPopTime();
                lastDetectedDateTime = new Date();
                weight = p.getWeight();
            }

            Float[] getCoords(){
                return coords;
            }

            Date getPopTime(){
                return popTime;
            }

            Float[] getDestCoords() {
                return destCoords;
            }

            void setLastDetectedDateTime(Date lastDetectedDateTime){
                this.lastDetectedDateTime = lastDetectedDateTime;
            }

            public Integer getId() {
                return id;
            }

            public Float getWeight(){
                return weight;
            }

            @Override
            public String toString() {
                return "ParcelRecord{" +
                        "coords=" + Arrays.toString(coords) +
                        ", lastDetectedDateTime=" + lastDetectedDateTime +
                        ", popTime=" + popTime +
                        ", weight=" + weight +
                        '}';
            }
        }

        public void add(Parcel p) {
            parcelRecords.add(createParcelRecord(p));
        }

        ParcelRecord createParcelRecord(Parcel parcel){
            return new ParcelRecord(parcel);
        }

        public void remove(ParcelRecord parcelRecord) {
            parcelRecords.remove(parcelRecord);
        }

        @Override
        public String toString() {
            return "Memory{" +
                    "parcelRecords=" + parcelRecords +
                    '}';
        }
    }

    /* getteurs et setteurs triviaux */
    public Boolean isLoaded() {
        return isLoaded;
    }

    public Float getSpeed() {
        return speed;
    }

    public Integer getDetectionRange() {
        return visibleDistance;
    }

    public Float getBatteryFullCapacity() {
        return batteryFullCapacity;
    }

    public void setLoaded(Boolean b) {
        isLoaded = b;
    }

    public void setBatteryFullCapacity(Float i) {
        if(i < Simulation.getDroneBatteryCapacity()[0] || i > Simulation.getDroneBatteryCapacity()[1]){
            throw new IllegalArgumentException("battery capacity must be between " + Simulation.getDroneBatteryCapacity()[0] + " and " + Simulation.getDroneBatteryCapacity()[1] + " (" + i + " given)");
        } else {
            this.batteryFullCapacity = i;
            this.batteryCapacity = this.batteryFullCapacity;
        }
    }

    public void setBusy(Boolean b) {
        isBusy = b;
    }

    public Boolean isBusy() {
        return isBusy;
    }

    private Float getRotation() {
        return rotation;
    }

    public Float getWeightCapacity() {
        return weightCapacity;
    }

    public void setWeightCapacity(Float weightCapacity) {
        if(isValueOutOfRange(weightCapacity, Simulation.getDroneWeightCapacity()[0], Simulation.getDroneWeightCapacity()[1])){
            throw new IllegalArgumentException("weigh capacity must be between " + Simulation.getDroneWeightCapacity()[0] + " and " + Simulation.getDroneWeightCapacity()[1] + " (" + weightCapacity + " given)");
        } else {
            this.weightCapacity = weightCapacity;

        }
    }

    Float getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setBatteryCapacity(Float batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    @Override
    public String toString() {
        return "Drone{" +
                "isBusy=" + isBusy +
                ", isLoaded=" + isLoaded +
                ", batteryFullCapacity=" + batteryFullCapacity +
                ", rotation=" + rotation +
                ", weightCapacity=" + weightCapacity +
                ", memory=" + memory +
                ", coords=" + Arrays.toString(coord) +
                '}';
    }
}