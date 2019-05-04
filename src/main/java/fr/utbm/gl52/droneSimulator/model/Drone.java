package fr.utbm.gl52.droneSimulator.model;

public class Drone extends SimulationElement {
    // v2: TODO récupérer zone possible pour les drone ?

    // constantes de classe
    static final private int size = 5;
    static final private int speed = 5;

    // attributs
    private float rotation; // TODO degres ou radian ?
    private boolean busy; // TODO conserver ?

    public Drone() {
        rotation = 0;
    }

    /*
        Gestion de l'affichage pour le débug
        TODO terminer une fois les attributs fixés
    */
    public String toString() {
        String s = super.toString() + "rotation: " + getRotation() + System.getProperty("line.separator")
        return s;
    }

    public int getSize() {
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

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int s) {
        speed = s;
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

    public void setRotation(int x, int y) {
        setRotation(angleCalcul(x, y));
    }

    public void setRandRotation() {
        setRotation(getRandInt(0, (int) (2 * Math.PI)));
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
    public boolean reactToDrone(Drone drone) {
        Boolean react = false;

        // interact
        react = true; // TODO conserver ?

        return react;
    }

    public boolean meet(Drone drone) {
        return (distanceCalcul(drone) < (getSize() / 2 + drone.getSize() / 2));
    }

    public boolean reactToParcel(Parcel f) {
        Boolean react = false;
        if (isEatable(f)) {
            goTo(f);
            react = true;
        }
        return react;
    }




    public boolean meet(Parcel f) {
        return (distanceCalcul(f) < (getSize() / 2 + f.getSize() / 2));
    }

    public void interact(Parcel f) {
        if (isEatable(f))
            eat(f);
    }

    public void eat(Parcel f) {
        Simulation.removeFood(f);
        // TODO activer ou supprimer grow(aj.getSize());
    }

    public void setSpecie(String specie) {
        this.specie = specie;
    }

    public boolean isSameSpecie(Drone drone) {
        return getSpecie().equals(drone.getSpecie());
    }

    public float getFertilityRate() {
        return fertilityRate;
    }

    public void setFertilityRate(float fr) {
        fertilityRate = fr;
    }


    /* v2 */
    static final private int visibleDistance = 10000; // TODO v2

    public int getVisibleDistance() {
        return visibleDistance;
    }

    public void setVisibleDistance(int vd) {
        visibleDistance = vd;
    }
}