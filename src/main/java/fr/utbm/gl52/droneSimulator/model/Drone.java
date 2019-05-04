package fr.utbm.gl52.droneSimulator.model;

import static jdk.internal.jimage.decompressor.CompressedResourceHeader.getSize;

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

    public boolean reactToAnimal(Drone drone) {
        Boolean react = false;
        if (isSameSpecie(drone)) {
            goTo(drone);
            react = true;
        } else {
            if (isPredator(drone)) {
                goTo(drone);
                react = true;
            }
        }
        return react;
    }

    public boolean meet(Drone drone) {
        return (distanceCalcul(drone) < (getSize() / 2 + drone.getSize() / 2));
    }

    public float getLukeToKill(Drone drone) {
        float luke = getSize() / drone.getSize();

        if (getSize() > drone.getSize())
            luke = 1 / luke;

        return luke;
    }

    public boolean isPredator(Drone drone) {
        boolean isPredator = false;

        if ((isCarnivorous() && drone.isVertebrate()) || (isInsectivorous() && drone.isInsect()))
            isPredator = true;

        return isPredator;
    }

    public boolean isPrey(Drone drone) {
        boolean isPrey = false;

        if ((drone.isCarnivorous() && isVertebrate()) || (drone.isInsectivorous() && isInsect()))
            isPrey = true;

        return isPrey;
    }

    private void makeLove(Drone drone) {
        // TODO ajouter dans l'ajout d'eespece et dans la recuperation des caracs d'especes
        if (getRandInt(1, 100) < getFertilityRate())
            makeBaby();
    }

    private void makeBaby() {
        Drone drone = SpecieManager.getSpecie(getSpecie());
        drone.setRandCoord(getCase());
        Simulation.getAnimals().add(drone);
    }

    private Area getCase() {
        return SimulationElement.getCase(getX(), getY());
    }

    public int getSexe() {
        return sexe;
    }

    private void setSexe(int s) {
        sexe = s;
    }

    private void setRandSexe() {
        setSexe(getRandInt(0, 1));
    }

    public void interact(Drone drone) {
        if (!isSameSpecie(drone)) {
            if (isPredator(drone)) {
//                if (getRandInt(1,100) < getLukeToKill(a))
                kill(drone);
            } else if (isPrey(drone)) {
//                if (getRandInt(1,100) < a.getLukeToKill(this))
                drone.kill(this);
            } else {
                flee(drone);
                drone.flee(this);
            }
        } else {
            if ((getSexe() == 2 && drone.getSexe() == 2) || (getSexe() == 0 && drone.getSexe() == 1) || (drone.getSexe() == 0 && getSexe() == 1))
                makeLove(drone);

            flee(drone);
            drone.flee(this);
        }
    }

    public void kill(Drone drone) {
        Simulation.removeAnimal(drone);
    }

    /*
        Fonctions d'interaction - nourriture
        TODO faire une action sur l'animal mangeur sur la vie, la taille ou autre
    */
    public boolean isEatable(Food f) {
        return (
                (f.getType() == "vegetable" && isHerbivorous())
                        || (f.getType() == "meat" && isCarnivorous())
                        || (f.getType() == "deadInsecte" && isInsectivorous())
        );
    }

    public boolean reactToFood(Food f) {
        Boolean react = false;
        if (isEatable(f)) {
            goTo(f);
            react = true;
        }
        return react;
    }

    public boolean meet(Food f) {
        return (distanceCalcul(f) < (getSize() / 2 + f.getSize() / 2));
    }

    public void interact(Food f) {
        if (isEatable(f))
            eat(f);
    }

    public void eat(Food f) {
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