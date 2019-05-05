package fr.utbm.gl52.droneSimulator.model;

import java.util.ArrayList;
import java.util.Date;

public class Drone extends SimulationElement {
    // constantes
    static final private Integer size = 5;
    static final private Integer speed = 5;

    // attributs
    private Boolean isBusy;
    private Boolean isLoaded;
    private Float charge;
    private Float rotation; // TODO degres ou radian ?

    ArrayList<DetectedParcel> detectedParcels = new ArrayList<DetectedParcel>();
    public class DetectedParcel {
        private Float[] coord; // TODO factoriser avec SimulationElement
        private Date lastDetectedDateTime;

        public DetectedParcel(Parcel p){
            coord = p.coord;
            lastDetectedDateTime = new Date();
        }
    }

    public Drone() {
        isBusy = false;
        isLoaded = false;
        charge = 100;
        rotation = 0;

        routageTable = new ArrayList<DetectedParcel>();
    }

    public static Drone createRandomizedDrone() {
        Drone drone = new Drone();
        drone.randomize();
        return drone;
    }

    public void randomize() {
        setRandCoord();
    }

    public String toString() {
        String s =
                super.toString() +
                        "isBusy: " + (isBusy() ? "busy" : "free") +
                        "isLoaded: " + (isLoaded() ? "loaded" : "empty") +
                        "charge: " + getCharge() + "%" +
                        "rotation: " + getRotation() +
                        System.getProperty("line.separator");
        return s;
    }

    public void move() {
        Float newX = (Float) (getX() + (speed * Math.cos(rotation)));
        Float newY = (Float) (getY() + (speed * Math.sin(-rotation)));
    }

    public void goTo(SimulationElement ge) {
        setRotation(ge);
    }

    public Boolean isBusy() {
        return isBusy;
    }

    public Float getRotation() {
        return rotation;
    }


    public void rotation(Float radian) {
        setRotation(getRotation() + radian);
    }

    // v2
    public Boolean detect(SimulationElement ge) {
        Float angle = angleCalcul(ge);
        Float angleWithTwoPi = (Float) (angle + 2 * Math.PI);

        return distanceCalcul(ge) < getVisibleDistance();
    }

    // v2
    public void exchangeData(Drone drone) {
//        data.merge(drone.data);
//        drone.data.merge(data);
    }

    public Boolean meet(Drone drone) {
        return (distanceCalcul(drone) < (getSize() / 2 + drone.getSize() / 2));
    }

    public Boolean isTransportable(Parcel parcel) {
        return true; // TODO
    }

    public Boolean reactToParcel(Parcel parcel) {
        Boolean react = false;
        if (isTransportable(parcel)) {
            goTo(parcel);
            react = true;
        }
        return react;
    }

    public void handleDroneInteraction() {
        ArrayList<Drone> drones = Simulation.getDrones();

        for (Integer j = 0; j < drones.size(); ++j) {
            Drone drone = drones.get(j);
            if (this != drone && detect(drone)) {
                exchangeData(drone);
            }
        }
    }

    public void handleParcelsInteraction() {
        ArrayList<Parcel> parcels = Simulation.getParcels();

        if (!isBusy()) { // dans un premier temps on peut estimer que un drone fait entierement sa livraison avant d'en envisager un autre / avant de lacher un colis pour en prendre une autre // TODO changer le comportement
            for (Integer k = 0; k < parcels.size(); ++k) {
                Parcel parcel = parcels.get(k);

                if (detect(parcel)) {
                    Boolean react = reactToParcel(parcel);
                    if (react) {
                        setBusy(true);
                        break; // si on réagit, (premier de la liste) on arrête les tests pour ce drone TODO améliorer au plus proche dans un premier temps
                    }
                }
            }
        }
    }

    public Boolean meet(Parcel f) {
        return (distanceCalcul(f) < (getSize() / 2 + f.getSize() / 2));
    }

    public void interact(Parcel f) {
        if (isTransportable(f))
            load(f);
    }

    public void load(Parcel f) {
        Simulation.removeParcel(f);
    }

    public void charge() {
        // TODO
    }

    /* v2 */
    static final private Integer visibleDistance = 10000; // TODO v2


    public Boolean isLoaded() {
        return isLoaded;
    }

    public void setRotation(Float radian) {
        rotation = simplifyAngle(radian);
    }

    public void setRotation(SimulationElement ge) {
        Float newOrientation = angleCalcul(ge);

        setRotation(newOrientation);
    }

    public void setRotation(Integer x, Integer y) {
        setRotation(angleCalcul(x, y));
    }

    public void setRandRotation() {
        setRotation(RandomHelper.getRandInt(0, (int) (2 * Math.PI)));
    }

    public void setRandCoord() {
        setRandCoord(Simulation.getMainArea());
    }



    /* getteurs et setteurs triviaux */
    public Float getWidth() {
        return getSize();
    }

    public Float getHeight() {
        return getSize();
    }

    public Integer getSpeed() {
        return speed;
    }

    public Integer getVisibleDistance() {
        return visibleDistance;
    }

    public Integer getSize() {
        return size;
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

}