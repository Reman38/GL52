package fr.utbm.gl52.droneSimulator.model;

import fr.utbm.gl52.droneSimulator.model.exception.OutOfMainAreaException;

import java.util.Date;

public class Parcel extends CenteredAndSquaredSimulationElement {
    private Date popTime;
    private Float weight;
    private Date timeDeliveryGoal;
    private Float[] destCoord = new Float[2];

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

        try {
            setRandDestCoord(Simulation.getMainArea()); // après setWeight car size nécessaire pour le controle de contrainte de mainArea
        } catch (OutOfMainAreaException e) {
            e.printStackTrace();
        }
    }

    public void setRandWeight() {
        setWeight(RandomHelper.getRandFloat(0f, 20f));
    }

    protected void setRandDestX(Area area) throws OutOfMainAreaException {
//        setX(RandomHelper.getRandFloat(area.getX(), (area.getX() + area.getWidth())));
        setDestX(area.getWidth()/2);
    }

    protected void setRandDestY(Area area) throws OutOfMainAreaException {
//        setY(RandomHelper.getRandFloat(area.getY(), (area.getY() + area.getHeight())));
        setDestY(area.getHeight()/2);
    }

    public void setRandDestCoord(Area area) throws OutOfMainAreaException {
        setRandDestX(area);
        setRandDestY(area);
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

    public Float[] getDestCoord() {
        return destCoord;
    }

    public void setDestX(float x) throws OutOfMainAreaException {
        if (Simulation.getMainArea().isInAreaYBoundary(x))
            throw new OutOfMainAreaException("y out of mainArea boundary : " + x);
        else
            destCoord[0] = x;
    }

    public void setDestY(float y) throws OutOfMainAreaException {
        if (Simulation.getMainArea().isInAreaYBoundary(y))
            throw new OutOfMainAreaException("y out of mainArea boundary : " + y);
        else
            destCoord[1] = y;
    }
}
