package fr.utbm.gl52.droneSimulator.model;

public class Area extends SimulationElement {
    private float width;
    private float height;

    public Area(float x, float y, float caseWidth, float caseHeight) {
        try {
            setX(x);
        } catch (OutOfMainAreaException e) {
            e.printStackTrace();
        }
        try {
            setY(y);
        } catch (OutOfMainAreaException e) {
            e.printStackTrace();
        }
        try {
            setWidth(caseWidth);
        } catch (OutOfMainAreaException e) {
            e.printStackTrace();
        } catch (NotSupportedValueException e) {
            e.printStackTrace();
        }
        try {
            setHeight(caseHeight);
        } catch (OutOfMainAreaException e) {
            e.printStackTrace();
        } catch (NotSupportedValueException e) {
            e.printStackTrace();
        }
    }

    public void setX(float x) throws OutOfMainAreaException {
        Area ma = Simulation.getMainArea();
        if (x < ma.getX() || x > ma.getX() + ma.getWidth())
            throw new OutOfMainAreaException("x out of mainArea boundary");
        else
            coord[0] = x;
    }

    public void setY(float y) throws OutOfMainAreaException {
        Area ma = Simulation.getMainArea();
        if (y < ma.getY() || y > ma.getY() + ma.getHeight())
            throw new OutOfMainAreaException("y out of mainArea boundary");
        else
            coord[1] = y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void setWidth(float w) throws OutOfMainAreaException, NotSupportedValueException {
        Area ma = Simulation.getMainArea();
        if (w <= 0)
            throw new NotSupportedValueException("width can't be <= 0");
        if (getX() + w < ma.getX() || getX() + w > ma.getX() + ma.getWidth())
            throw new OutOfMainAreaException("x out of mainArea boundary");
        else
            width = w;
    }

    public void setHeight(float h) throws OutOfMainAreaException, NotSupportedValueException {
        Area ma = Simulation.getMainArea();
        if (h <= 0)
            throw new NotSupportedValueException("height can't be <= 0");
        if (getY() + h < ma.getY() || getY() + h > ma.getY() + ma.getHeight())
            throw new OutOfMainAreaException("y out of mainArea boundary");
        else
            height = h;
    }
}
