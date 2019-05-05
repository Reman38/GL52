package fr.utbm.gl52.droneSimulator.model;

public class Area extends SimulationElement {
    protected float width;
    protected float height;

    public Area(float x, float y, float caseWidth, float caseHeight) {
        try {
            setX(x);
            setY(y);
            setWidth(caseWidth);
            setHeight(caseHeight);
        } catch (OutOfMainAreaException | NotSupportedValueException e) {
            e.printStackTrace();
        }
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void setX(float x) throws OutOfMainAreaException {
        if (Simulation.getMainArea().isInAreaXBoundary(x))
            throw new OutOfMainAreaException("x out of mainArea boundary");
        else
            coord[0] = x;
    }

    public void setY(float y) throws OutOfMainAreaException {
        if (Simulation.getMainArea().isInAreaYBoundary(y))
            throw new OutOfMainAreaException("y out of mainArea boundary");
        else
            coord[1] = y;
    }

    public void setWidth(float w) throws OutOfMainAreaException, NotSupportedValueException {
        if (w <= 0)
            throw new NotSupportedValueException("width can't be <= 0");
        if (Simulation.getMainArea().isInAreaXBoundary(getX() + w))
            throw new OutOfMainAreaException("x out of mainArea boundary");
        else
            width = w;
    }

    public void setHeight(float h) throws OutOfMainAreaException, NotSupportedValueException {
        if (h <= 0)
            throw new NotSupportedValueException("height can't be <= 0");
        if (Simulation.getMainArea().isInAreaYBoundary(getY() + h))
            throw new OutOfMainAreaException("y out of mainArea boundary");
        else
            height = h;
    }

    public boolean isInAreaXBoundary(float x) {
        return x < getX() || x > getX() + getWidth();
    }

    public boolean isInAreaYBoundary(float y) {
        return y < getY() || y > getY() + getHeight();
    }

    public boolean isInAreaBoundary(SimulationElement se) {
        return
            isInAreaXBoundary(se.getX() - getWidth()/2) &&
            isInAreaXBoundary(se.getX() - getWidth()/2) &&
            isInAreaYBoundary(se.getY() - getHeight()/2) &&
            isInAreaYBoundary(se.getY() - getHeight()/2)
        ;
    }

}
