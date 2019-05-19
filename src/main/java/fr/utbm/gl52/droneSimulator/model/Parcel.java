package fr.utbm.gl52.droneSimulator.model;

import fr.utbm.gl52.droneSimulator.model.exception.OutOfMainAreaException;

import java.util.Date;

public class Parcel extends CenteredAndSquaredSimulationElement {
    private Date popTime;
    private Float weight;
    private Date timeDeliveryGoal;

    public Parcel() {
        super(.5f);
        popTime = new Date();
    }

    public static Parcel createRandomized() {
        Parcel parcel = new Parcel();
        parcel.randomize();
        return parcel;
    }

    public void randomize() {
        setRandWeight();
        try {
            setRandCoord(Simulation.getMainArea()); // après setWeight car size nécessaire pour le controle de contrainte de mainArea
        } catch (OutOfMainAreaException e) {
            e.printStackTrace();
        }
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

    public String toString() {
        String s =
                super.toString() +
                        "weight: " + getWeight() + System.getProperty("line.separator");
        return s;
    }
}
