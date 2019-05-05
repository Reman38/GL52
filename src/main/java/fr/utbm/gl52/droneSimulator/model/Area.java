package fr.utbm.gl52.droneSimulator.model;

import fr.utbm.gl52.droneSimulator.exception.NotSupportedValueException;
import fr.utbm.gl52.droneSimulator.exception.OutOfMainAreaException;

public class Area extends SimulationElement {
    protected Float width;
    protected Float height;

    public Area(Float x, Float y, Float caseWidth, Float caseHeight) {
        try {
            setX(x);
            setY(y);
            setWidth(caseWidth);
            setHeight(caseHeight);
        } catch (OutOfMainAreaException | NotSupportedValueException e) {
            e.printStackTrace();
        }
    }

    public Float getWidth() {
        return width;
    }

    public Float getHeight() {
        return height;
    }

    public void setX(Float x) throws OutOfMainAreaException {
        if (Simulation.getMainArea().isInAreaXBoundary(x))
            throw new OutOfMainAreaException("x out of mainArea boundary");
        else
            coord[0] = x;
    }

    public void setY(Float y) throws OutOfMainAreaException {
        if (Simulation.getMainArea().isInAreaYBoundary(y))
            throw new OutOfMainAreaException("y out of mainArea boundary");
        else
            coord[1] = y;
    }

    public void setWidth(Float w) throws OutOfMainAreaException, NotSupportedValueException {
        if (w <= 0)
            throw new NotSupportedValueException("width can't be <= 0");
        if (Simulation.getMainArea().isInAreaXBoundary(getX() + w))
            throw new OutOfMainAreaException("x out of mainArea boundary");
        else
            width = w;
    }

    public void setHeight(Float h) throws OutOfMainAreaException, NotSupportedValueException {
        if (h <= 0)
            throw new NotSupportedValueException("height can't be <= 0");
        if (Simulation.getMainArea().isInAreaYBoundary(getY() + h))
            throw new OutOfMainAreaException("y out of mainArea boundary");
        else
            height = h;
    }

    public Boolean isInAreaXBoundary(Float x) {
        return x < getX() || x > getX() + getWidth();
    }

    public Boolean isInAreaYBoundary(Float y) {
        return y < getY() || y > getY() + getHeight();
    }

    public Boolean isInAreaBoundary(SimulationElement se) {
        return
            isInAreaXBoundary(se.getX() - getWidth()/2) &&
            isInAreaXBoundary(se.getX() + getWidth()/2) &&
            isInAreaYBoundary(se.getY() - getHeight()/2) &&
            isInAreaYBoundary(se.getY() + getHeight()/2)
        ;
    }

}
