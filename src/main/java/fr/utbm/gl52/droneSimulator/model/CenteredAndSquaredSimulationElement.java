package fr.utbm.gl52.droneSimulator.model;

import fr.utbm.gl52.droneSimulator.model.exception.OutOfMainAreaException;

public class CenteredAndSquaredSimulationElement extends SimulationElement{
    private Float size;
    private Integer id;

    public CenteredAndSquaredSimulationElement(Integer _id, Float _size){
        size = _size;
        id = _id;
    }

    public Float getSize() {
        return size;
    }

    public Float getWidth() {
        return getSize();
    }

    public Float getHeight() {
        return getSize();
    }

    protected void setRandX(Area area) throws OutOfMainAreaException {
        setX(RandomHelper.getRandFloat(area.getX() + getWidth() / 2, (area.getX() + area.getWidth()) - getWidth() / 2));
    }

    protected void setRandY(Area area) throws OutOfMainAreaException {
        setY(RandomHelper.getRandFloat(area.getY() + getHeight() / 2, (area.getY() + area.getHeight()) - getHeight() / 2));
    }

    public Integer getId() {
        return id;
    }
}