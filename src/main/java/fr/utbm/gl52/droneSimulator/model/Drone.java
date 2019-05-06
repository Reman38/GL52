package fr.utbm.gl52.droneSimulator.model;

import java.util.ArrayList;
import java.util.Date;

public class Drone extends SquaredSimulationElement {
    // constantes
    static final private Float speed = 5f;
    static final private Integer visibleDistance = 10000;

    // attributs
    private Boolean isBusy;
    private Boolean isLoaded;
    private Float charge;
    private Float rotation; // TODO degres ou radian ?

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

    Memory memory;

    public class Memory {
        ArrayList<ParcelRecord> parcelRecords = new ArrayList<ParcelRecord>();

        public class ParcelRecord {
            private Float[] coord = new Float[2];;
            private Date lastDetectedDateTime;
            private Date popTime;

            public ParcelRecord(Parcel p) {
                coord = p.getCoord();
                popTime = p.getPopTime();
                lastDetectedDateTime = new Date();
            }
        }

        public void add(Parcel p) {
            // TODO if not already in the list
            parcelRecords.add(new ParcelRecord(p));
        }

        public void remove() {
            // TODO remove detectedParcel if not detected when flying near it
        }
    }

    public Drone() {
        super(.8f);

        isBusy = false;
        isLoaded = false;
        charge = 100f;
        rotation = 0f;

        memory = new Memory();
    }

    public static Drone createRandomizedDrone() {
        Drone drone = new Drone();
        drone.randomize();
        return drone;
    }

    public void randomize() {
        setRandCoord();
    }

    public void move() {
        Float newX = getX() + (speed * (float) Math.cos(rotation));
        Float newY = getY() + (speed * (float) Math.sin(-rotation));

        setX(newX);
        setY(newY);
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

    public void setRandCoord() {
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

    public Float getCharge() {
        return charge;
    }

    public void setLoaded(Boolean b) {
        isLoaded = b;
    }

    public void setCharge(Float f) {
        charge = f;
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

    public String toString() {
        String s =
                super.toString() +
                        "isBusy: " + (isBusy() ? "busy" : "free") +
                        "isLoaded: " + (isLoaded() ? "loaded" : "empty") +
                        "chargeBattery: " + getCharge() + "%" +
                        "rotate: " + getRotation() +
                        System.getProperty("line.separator");
        return s;
    }
}