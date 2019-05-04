package fr.utbm.gl52.droneSimulator.model;

import java.util.ArrayList;

public class Drone extends SimulationElement {
    // TODO float array to vector 2/3
    // v2: TODO récupérer zone possible pour les drone ?

    // constantes de classe
    static final private Integer size = 5;
    static final private Integer speed = 5;

    // attributs
    private float rotation; // TODO degres ou radian ?
    private boolean busy; // TODO conserver ?

    public Drone() {
        rotation = 0;
    }

    public void randomize() {
        setRandCoord();
    }

    public static Drone createRandomizedDrone() {
        Drone drone = new Drone();
        drone.randomize();
        return drone;
    }

    /*
        Gestion de l'affichage pour le débug
        TODO terminer une fois les attributs fixés
    */
    public String toString() {
        String s = super.toString() + "rotation: " + getRotation() + System.getProperty("line.separator");
        return s;
    }

    public Integer getSize() {
        return size;
    }

    public void setRandCoord() {
        setRandCoord(Simulation.getMainArea()); // Conserver pour évolution
    }

    public float getWidth() {
        return getSize();
    }

    public float getHeight() {
        return getSize();
    }

    public Integer getSpeed() {
        return speed;
    }

    public void move() {
        float newX = (float) (getX() + (speed * Math.cos(rotation)));
        float newY = (float) (getY() + (speed * Math.sin(-rotation)));
    }

    public void goTo(SimulationElement ge) {
        setRotation(ge);
    }

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean b) {
        busy = b;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float radian) {
        rotation = simplifyAngle(radian);
    }

    public void setRotation(SimulationElement ge) {
        float newOrientation = angleCalcul(ge);

        setRotation(newOrientation);
    }

    public void setRotation(Integer x, Integer y) {
        setRotation(angleCalcul(x, y));
    }

    public void setRandRotation() {
        setRotation(RandomHelper.getRandInt(0, (int) (2 * Math.PI)));
    }

    public void rotation(float radian) {
        setRotation(getRotation() + radian);
    }

    // v2
    public boolean detect(SimulationElement ge) {
        float angle = angleCalcul(ge);
        float angleWithTwoPi = (float) (angle + 2 * Math.PI);

        return distanceCalcul(ge) < getVisibleDistance();
    }

    // v2
    public void exchangeData(Drone drone) {
//        this.data.merge(drone.data);
//        drone.data.merge(this.data);
    }

    public boolean meet(Drone drone) {
        return (distanceCalcul(drone) < (getSize() / 2 + drone.getSize() / 2));
    }

    public boolean isTransportable(Parcel parcel) {
        return true; // TODO
    }

    public boolean reactToParcel(Parcel parcel) {
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
            if (this != drone && this.detect(drone)) {
                this.exchangeData(drone);
            }
        }
    }

    public void handleParcelsInteraction() {
        ArrayList<Parcel> parcels = Simulation.getParcels();

        if (!this.isBusy()) { // dans un premier temps on peut estimer que un drone fait entierement sa livraison avant d'en envisager un autre / avant de lacher un colis pour en prendre une autre // TODO changer le comportement
            for (Integer k = 0; k < parcels.size(); ++k) {
                Parcel parcel = parcels.get(k);

                if (this.detect(parcel)) {
                    boolean react = this.reactToParcel(parcel);
                    if (react) {
                        this.setBusy(true);
                        break; // si on réagit, (premier de la liste) on arrête les tests pour ce drone TODO améliorer au plus proche dans un premier temps
                    }
                }
            }
        }
    }

    public boolean meet(Parcel f) {
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

    public Integer getVisibleDistance() {
        return visibleDistance;
    }
}