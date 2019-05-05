package fr.utbm.gl52.droneSimulator.model;

import java.util.Date;

public class Parcel extends SimulationElement {
    private Date popTime;
    private Float weight;
    private Date timeDeliveryGoal;

    public Parcel() {
    }

    public Parcel(Float x, Float y) {
        setX(x);
        setY(y);
    }

    public static Parcel createRandomized() {
        Parcel parcel = new Parcel();
        parcel.randomize();
        return parcel;
    }

    public void randomize() {
        setRandWeight();

        // après le setRandWeight car set la size pour la vue ; sinon contrainte de zone non prise en compte
        setRandX();
        setRandY();
    }

    public String toString() {
        String s =
            super.toString() +
            "weight: " + getWeight() + System.getProperty("line.separator");
        return s;
    }

    private void setRandX() {
        setRandX(Simulation.getMainArea());
    }
    private void setRandY() {
        setRandY(Simulation.getMainArea());
    }

    public void setRandWeight() {
        setWeight(RandomHelper.getRandFloat(0f, 20f));
    }

    /* getteurs et setteurs triviaux */
    public Date getPopTime() {
        return popTime;
    }
    public void setPopTime(Date popTime) {
        this.popTime = popTime;
    }
    public Float getWeight() {
        return weight;
    }
    public void setWeight(Float weight) {
        this.weight = weight;
    }
}
