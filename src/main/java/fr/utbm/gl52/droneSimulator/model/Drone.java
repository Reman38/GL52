package fr.utbm.gl52.droneSimulator.model;

import fr.utbm.gl52.droneSimulator.model.exception.OutOfMainAreaException;
import javafx.application.Platform;

import java.util.*;

import static fr.utbm.gl52.droneSimulator.controller.ControllerHelper.isSameCoord;
import static fr.utbm.gl52.droneSimulator.model.MathHelper.calculAngleWith;
import static fr.utbm.gl52.droneSimulator.model.MathHelper.computeVectorNorm;
import static fr.utbm.gl52.droneSimulator.model.Parcel.loadParcelAtCoord;
import static fr.utbm.gl52.droneSimulator.view.graphicElement.ParcelGraphicElement.removeParcelGraphicAtCoord;

public class Drone extends CenteredAndSquaredSimulationElement implements Runnable{
    // constantes
    static final private Float speed = 18f; // 41 mph, 65 km/h, 18m/s
    static final private Integer visibleDistance = 100000;
    public static final long nanosecondsInASecond = (long) StrictMath.pow(10, 9);
    public static final float RADIUS = 8f;

    // attributs
    private Boolean isBusy;
    private Boolean isLoaded;
    private Integer batteryCapacity;
    private Float rotation = 0f; // TODO degres ou radian ?
    private Float weightCapacity;
    private Memory.ParcelRecord targetParcel = null;
    private Float[] geographicalTarget = new Float[2];

// TODO prise de décision par rapport aux paquets
    /*
    if (isTransportable(parcel)) {
        goTo(parcel);
        react = true;
    }
    if (react) {
        setBusy(true);
        break; // si on réagit, (premier de la liste) on arrête les tests pour ce drone TODO améliorer au plus proche dans un premier temps
    }
    */

    private Memory memory;

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            parcelMemoryUpdate();
            //System.out.println(memory.toString());
            if(!areCoordNull(geographicalTarget)){
                rotation = calculAngleWith(getX(), geographicalTarget[0], getY(), geographicalTarget[1]);
            }
            if(!isBusy){
                targetParcel = getCloserParcel();
                if(targetParcel != null) {
                    geographicalTarget = targetParcel.getCoord();
                }
                isBusy = true;
            } else {
                if(!isLoaded) {
                    if (targetParcel != null && !isParcelInMemory(targetParcel)) {
                        targetParcel = null;
                        geographicalTarget = null;
                        isBusy = false;
                    } else if (!areCoordNull(geographicalTarget)){
                        if(isInRadius(geographicalTarget, RADIUS)){
                            System.out.println("Parcel loaded");
                            isLoaded = true;

                            loadParcelAtCoord(targetParcel.getCoord(), this);
                            geographicalTarget = targetParcel.getDestCoord();
                        }
                    }
                } else if (!areCoordNull(geographicalTarget)) {
                    if(isInRadius(geographicalTarget, RADIUS)) {
                        System.out.println("Parcel delivered");
                        isLoaded = false;
                        isBusy = false;
                        geographicalTarget = null;
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // Thread.currentThread().interrupt();
                    }
                }
            }
            move(nanosecondsInASecond);
            try {
                Thread.sleep(1000 / 30);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private boolean areCoordNull(Float[] coord) {
        return coord == null || coord[0] == null || coord[1] == null;
    }

    private boolean isInRadius(Float[] geographicalTarget, Float radius) {
        return !(isValueOutOfRange(getX(), geographicalTarget[0] - radius, geographicalTarget[0] + radius) || isValueOutOfRange(getY(), geographicalTarget[1] - radius, geographicalTarget[1] + radius));
    }


    private Memory.ParcelRecord getCloserParcel() {
        Memory.ParcelRecord minParcelRecord = null;
        if(memory.parcelRecords.size() > 0) {
            float min = computeVectorNorm(getX(), memory.parcelRecords.get(0).getCoord()[0], getY(), memory.parcelRecords.get(0).getCoord()[1]);
            minParcelRecord = memory.parcelRecords.get(0);

            for (Memory.ParcelRecord parcel : memory.parcelRecords) {
                float value = computeVectorNorm(getX(), parcel.getCoord()[0], getY(), parcel.getCoord()[1]);
                if (value < min) {
                    min = value;
                    minParcelRecord = parcel;
                }
            }
        }
        return minParcelRecord;
    }

    private void parcelMemoryUpdate() {
        for(Parcel parcel: Simulation.getParcels()){
            if (detect(parcel)) {
                if(!isParcelInMemory(parcel)){
                    memory.add(parcel);
                }
            }
        }

        Iterator<Memory.ParcelRecord> iterator = memory.parcelRecords.iterator();

        while (iterator.hasNext()){
            Memory.ParcelRecord parcelRecord = iterator.next();
            if(!isParcelStillAvailable(parcelRecord)){
                iterator.remove();
            }
        }
    }

    private boolean isParcelInMemory(Parcel parcel) {
        Memory.ParcelRecord parcelRecord = memory.createParcelRecord(parcel);
        return isParcelInMemory(parcelRecord);
    }

    private boolean isParcelInMemory(Memory.ParcelRecord parcel) {
        boolean res = false;
        for(Memory.ParcelRecord parcelRecord: memory.parcelRecords){
            res = isSameCoord(parcel.getCoord(), parcelRecord.getCoord());
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

    private boolean isParcelStillAvailable(Memory.ParcelRecord parcelRecord) {
        boolean res = false;
        for(Parcel parcel: Simulation.getParcels()){
            if(!parcel.isInJourney()) {
                res = isSameCoord(parcel.getCoord(), parcelRecord.getCoord());
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

    public class Memory {
        ArrayList<ParcelRecord> parcelRecords = new ArrayList<ParcelRecord>();

        public class ParcelRecord {
            private Float[] coord = new Float[2];
            private Float[] destCoord = new Float[2];
            private Date lastDetectedDateTime;
            private Date popTime;

            public ParcelRecord(Parcel p) {
                coord = p.getCoord();
                destCoord = p.getDestCoord();
                popTime = p.getPopTime();
                lastDetectedDateTime = new Date();
            }

            public Float[] getCoord(){
                return coord;
            }

            public Date getPopTime(){
                return popTime;
            }

            public Float[] getDestCoord() {
                return destCoord;
            }

            public void setLastDetectedDateTime(Date lastDetectedDateTime){
                this.lastDetectedDateTime = lastDetectedDateTime;
            }

            @Override
            public String toString() {
                return "ParcelRecord{" +
                        "coord=" + Arrays.toString(coord) +
                        ", lastDetectedDateTime=" + lastDetectedDateTime +
                        ", popTime=" + popTime +
                        '}';
            }
        }

        public void add(Parcel p) {
            // TODO if not already in the list
            parcelRecords.add(createParcelRecord(p));
        }

        public ParcelRecord createParcelRecord(Parcel parcel){
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

    public Drone() {
        super(.8f);

        isBusy = false;
        isLoaded = false;
        batteryCapacity = Simulation.getDroneBatteryCapacity()[1];
        weightCapacity = Simulation.getDroneWeightCapacity()[0];

        memory = new Memory();
    }

    public static Drone createRandomizedDrone() {
        Drone drone = new Drone();
        try {
            drone.randomize();
        } catch (OutOfMainAreaException e) {
            e.printStackTrace();
        }
        return drone;
    }

    public void randomize() throws OutOfMainAreaException {
        setRandCoord();
        setRandRotation();
    }

    public void move(long deltaT) {
        //System.out.println("deltaT "+ deltaT);
        deltaT = avoidNullDeltaT(deltaT);

        float deltaTSec = convertNanosecondsToSeconds(deltaT);
        //System.out.println("deltaTSec "+ deltaTSec);

        Float newX = getX() + (getSpeed() * deltaTSec * (float) Math.cos(rotation));
        Float newY = getY() + (getSpeed() * deltaTSec * (float) Math.sin(-rotation));

        //printDroneSpeedForDebug(deltaTSec, newX, newY);

        tryToMoveDroneWithinBoundaries(deltaT, newX, newY);

        //System.out.println("newX " + newX + " newY " + newY);
    }

    public void moveTo(long deltaT, Float[] coord) {
        //System.out.println("deltaT "+ deltaT);
        deltaT = avoidNullDeltaT(deltaT);

        float deltaTSec = convertNanosecondsToSeconds(deltaT);
        //System.out.println("deltaTSec "+ deltaTSec);

        if(isInRadius(coord, 1f)) {

            Float newX = coord[0] / (getSpeed() * deltaTSec);
            Float newY = coord[1] / (getSpeed() * deltaTSec);

            try {
                setCoord(newX, newY);
            } catch (OutOfMainAreaException e) {
                //moveTo(deltaT, coord);
            }
        }


        //printDroneSpeedForDebug(deltaTSec, newX, newY);

        //System.out.println("newX " + newX + " newY " + newY);
    }

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

    private boolean isNewYOutOfMainArea(Float newY) {
        return isValueOutOfRange(newY, Simulation.getMainArea().getY(), Simulation.getMainArea().getHeight());
    }

    private boolean isValueOutOfRange(Float newPt, Float min, Float max) {
        return newPt < min || newPt > max;
    }

    private boolean isNewXOutOfBoundaries(Float newX) {
        return isValueOutOfRange(newX, Simulation.getMainArea().getX(), Simulation.getMainArea().getWidth());
    }

    private float convertNanosecondsToSeconds(long deltaT) {
        return (float)(deltaT) / nanosecondsInASecond;
    }

    private long avoidNullDeltaT(long deltaT) {
        if(deltaT == 0L){
            deltaT = nanosecondsInASecond;
        }
        return deltaT;
    }

    private void printDroneSpeedForDebug(float deltaTsec, Float newX, Float newY) {
        float dist = computeVectorNorm(getX(), newX, getY(), newY);
        System.out.println("dist "+ dist);
        System.out.println("Drone speed " + dist/deltaTsec);
    }

    public void goTo(SimulationElement se) {
        setRotation(se);
    }

    public void rotate(Float f) {
        setRotation(getRotation() + f);
    }

    public Boolean detect(SimulationElement ge) {
        return MathHelper.calculDistanceWith(this, ge) < getDetectionRange();
    }

    public void exchangeData(Drone drone) {
        // TODO appeler à l'aide pour porter un colis
        // TODO detect drones
        // TODO detect parcels
        // data.merge(drone.data);
        // drone.data.merge(data);
    }

    public Boolean isTransportable(Parcel parcel) {
        return true; // TODO
    }

    public void handleDroneInteractions() {
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
    }

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

    public Boolean isOverThe(Parcel parcel) {
        return MathHelper.calculDistanceWith(this, parcel) < getSize() / 2;
    }

    public void interactWith(Parcel parcel) {
        if (isTransportable(parcel))
            load(parcel);
        else 
            callHelp();
    }

    private void callHelp() {
//        TODO
    }

    public void load(Parcel f) {
        Simulation.removeParcel(f);
    }

    public void chargeBattery() {
        // TODO
    }

    public void land() {
        // TODO
    }

    public void setRotation(Float radian) {
        rotation = MathHelper.simplifyAngle(radian);
    }

    public void setRotation(SimulationElement ge) {
        Float rotation = MathHelper.calculAngleWith(this, ge);
        setRotation(rotation);
    }

    public void setRandRotation() {
        setRotation(RandomHelper.getRandFloat(0f, (float) (2 * Math.PI)));
    }

    public void setRandCoord() throws OutOfMainAreaException {
        setRandCoord(Simulation.getMainArea());
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

    public Integer getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setLoaded(Boolean b) {
        isLoaded = b;
    }

    public void setBatteryCapacity(Integer i) {
        if(i < Simulation.getDroneBatteryCapacity()[0] || i > Simulation.getDroneBatteryCapacity()[1]){
            throw new IllegalArgumentException("battery capacity must be between " + Simulation.getDroneBatteryCapacity()[0] + " and " + Simulation.getDroneBatteryCapacity()[1] + " (" + i + " given)");
        } else {
            this.batteryCapacity = i;

        }
    }

    public void setBusy(Boolean b) {
        isBusy = b;
    }

    public Boolean isBusy() {
        return isBusy;
    }

    public Float getRotation() {
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

    @Override
    public String toString() {
        return "Drone{" +
                "isBusy=" + isBusy +
                ", isLoaded=" + isLoaded +
                ", batteryCapacity=" + batteryCapacity +
                ", rotation=" + rotation +
                ", weightCapacity=" + weightCapacity +
                ", memory=" + memory +
                ", coord=" + Arrays.toString(coord) +
                '}';
    }
}