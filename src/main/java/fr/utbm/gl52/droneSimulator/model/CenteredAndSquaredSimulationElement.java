package fr.utbm.gl52.droneSimulator.model;

import fr.utbm.gl52.droneSimulator.model.exception.OutOfMainAreaException;

public class CenteredAndSquaredSimulationElement extends SimulationElement{

    private Float size;
    private Integer id;

    /**
     * Construct object with an Id and a size
     *
     * @param _id id of the element, must be unique.
     * @param _size size of the element (width = height = size) in meters
     */
    public CenteredAndSquaredSimulationElement(Integer _id, Float _size){
        size = _size;
        id = _id;
    }

    /**
     * Get the size of the element
     *
     * @return size in meter
     */
    public Float getSize() {
        return size;
    }

    /**
     * Get the width of the element
     *
     * @return width in meters
     */
    public Float getWidth() {
        return getSize();
    }

    /**
     * Get the height of the element
     *
     * @return height in meters
     */
    public Float getHeight() {
        return getSize();
    }

    /**
     * Set a rand abscissa within the area boundaries
     *
     * @param area Area in which the abscissa should be set
     *
     * @throws OutOfMainAreaException The abscissa exceeds the area boundaries
     */
    protected void setRandX(Area area) throws OutOfMainAreaException {
        setX(RandomHelper.getRandFloat(area.getX() + getWidth() / 2, (area.getX() + area.getWidth()) - getWidth() / 2));
    }

    /**
     * Set a rand ordinate within the area boundaries
     *
     * @param area Area in which the ordinate should be set
     *
     * @throws OutOfMainAreaException The ordinate exceeds the area boundaries
     */
    protected void setRandY(Area area) throws OutOfMainAreaException {
        setY(RandomHelper.getRandFloat(area.getY() + getHeight() / 2, (area.getY() + area.getHeight()) - getHeight() / 2));
    }

    /**
     * Get the Id of the element
     *
     * @return Id of the element
     */
    public Integer getId() {
        return id;
    }
}