package fr.utbm.gl52.droneSimulator.model;

import fr.utbm.gl52.droneSimulator.model.exception.NotSupportedValueException;
import fr.utbm.gl52.droneSimulator.model.exception.OutOfMainAreaException;

public class Area extends SimulationElement {
    protected Float width;
    protected Float height;
    protected static Float minSize = 1000f;
    protected static Float maxSize = 5000f;

    public Area() {
    }

    /**
     * Constructor of a new area
     *
     * @param x abscissa of the area
     * @param y ordinate of the area
     * @param width width of the area
     * @param height height of the area
     */
    public Area(Float x, Float y, Float width, Float height) {
        try {
            setX(x);
            setY(y);
            setWidth(width);
            setHeight(height);
        } catch (OutOfMainAreaException | NotSupportedValueException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generate an area width random position within the main area and random size
     *
     * @return random area
     */
    public static Area createRandomized() {
        Area area = new Area();
        try {
            area.randomize();
        } catch (OutOfMainAreaException | NotSupportedValueException e) {
            e.printStackTrace();
        }
        return area;
    }

    /**
     * Randomize the size and the position of the area
     *
     * @throws OutOfMainAreaException The random position is out of the main area or the size exceeds the main area
     * @throws NotSupportedValueException Position or size are negative
     */
    public void randomize() throws OutOfMainAreaException, NotSupportedValueException {
        setRandCoord(Simulation.getMainArea());
        setRandSize();
    }


    /**
     * Randomize the size of the area
     *
     * @throws OutOfMainAreaException The size exceeds the main area
     * @throws NotSupportedValueException size is negative
     */
    private void setRandSize() throws OutOfMainAreaException, NotSupportedValueException {
        setRandWidth();
        setRandHeight();
    }

    /**
     * Randomize the width of the area
     *
     * @throws OutOfMainAreaException The width exceeds the main area boundaries
     * @throws NotSupportedValueException The width is negative
     */
    private void setRandWidth() throws OutOfMainAreaException, NotSupportedValueException {
        Float randomPossibleWidth = RandomHelper.getRandFloat(0f, Simulation.getMainArea().getWidth() - getX());
        setWidth(randomPossibleWidth);
    }

    /**
     * Randomize the height of the area
     *
     * @throws OutOfMainAreaException The height exceeds the main area boundaries
     * @throws NotSupportedValueException The height is negative
     */
    private void setRandHeight() throws OutOfMainAreaException, NotSupportedValueException {
        Float randomPossibleHeight = RandomHelper.getRandFloat(0f, Simulation.getMainArea().getHeight() - getY());
        setHeight(randomPossibleHeight);
    }


    public Float getWidth() {
        return width;
    }

    public Float getHeight() {
        return height;
    }

    /**
     * Set the with of the Area
     *
     * @param w width in meters
     * @throws NotSupportedValueException The width is negative
     * @throws OutOfMainAreaException The width exceeds the main area boundaries
     */
    public void setWidth(Float w) throws NotSupportedValueException, OutOfMainAreaException{
        if (w <= 0)
            throw new NotSupportedValueException("width can't be <= 0");
        if (Simulation.getMainArea().isInAreaXBoundary(getX() + w))
            throw new OutOfMainAreaException("x + width out of mainArea boundary: " + (getX() + w));
        else
            width = w;
    }

    /**
     * Set the height of the Area
     *
     * @param h width in meters
     * @throws NotSupportedValueException The height is negative
     * @throws OutOfMainAreaException The height exceeds the main area boundaries
     */
    public void setHeight(Float h) throws NotSupportedValueException, OutOfMainAreaException{
        if (h <= 0)
            throw new NotSupportedValueException("height can't be <= 0");
        if (Simulation.getMainArea().isInAreaYBoundary(getY() + h))
            throw new OutOfMainAreaException("y + height out of mainArea boundary: " + (getY() + h));
        else
            height = h;
    }

    /**
     * Check if the area exceeds the width boundary of the main area
     *
     * @param x width of the area in meters
     *
     * @return true if x is in boundary
     */
    public Boolean isInAreaXBoundary(Float x) {
        return x < getX() || x > getX() + getWidth();
    }

    /**
     * Check if the area exceeds the height boundary of the main area
     *
     * @param y height of the area in meters
     *
     * @return true if y is in boundary
     */
    public Boolean isInAreaYBoundary(Float y) {
        return y < getY() || y > getY() + getHeight();
    }

    public Boolean isInAreaBoundary(SimulationElement se) {
        return
                isInAreaXBoundary(se.getX() - getWidth() / 2) &&
                        isInAreaXBoundary(se.getX() + getWidth() / 2) &&
                        isInAreaYBoundary(se.getY() - getHeight() / 2) &&
                        isInAreaYBoundary(se.getY() + getHeight() / 2)
                ;
    }

}
