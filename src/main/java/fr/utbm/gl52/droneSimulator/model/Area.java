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

    public Area(Float x, Float y, Float width, Float height) {
        try {
            setX(x);
            setY(y);
        } catch (OutOfMainAreaException e) {
            e.printStackTrace();
        }

        setWidth(width);
        setHeight(height);
    }
    public static Area createRandomized() {
        Area area = new Area();
        area.randomize();
        return area;
    }

    public void randomize() {
        setRandCoord(Simulation.getMainArea());
        setRandSize();
    }

    private void setRandSize() {
        setRandWidth();
        setRandHeight();
    }

    private void setRandWidth() {
        Float randomPossibleWidth = RandomHelper.getRandFloat(0f, Simulation.getMainArea().getWidth() - getX());
        setWidth(randomPossibleWidth);
    }
    private void setRandHeight() {
        Float randomPossibleHeight = RandomHelper.getRandFloat(0f, Simulation.getMainArea().getHeight() - getY());
        setHeight(randomPossibleHeight);
    }

    public Float getWidth() {
        return width;
    }

    public Float getHeight() {
        return height;
    }

    public void setWidth(Float w) {
        try {
            if (w <= 0)
                throw new NotSupportedValueException("width can't be <= 0");
            if (Simulation.getMainArea().isInAreaXBoundary(getX() + w))
                throw new OutOfMainAreaException("x + width out of mainArea boundary: " + (getX() + w));
            else
                width = w;
        } catch (OutOfMainAreaException | NotSupportedValueException e) {
            e.printStackTrace();
//            w = Simulation.getMainArea().getWidth() - getX();
//            setHeight(w);
        }
    }

    public void setHeight(Float h) {
        try {
            if (h <= 0)
                throw new NotSupportedValueException("height can't be <= 0");
            if (Simulation.getMainArea().isInAreaYBoundary(getY() + h))
                throw new OutOfMainAreaException("y + height out of mainArea boundary: " + (getY() + h));
            else
                height = h;
        } catch (OutOfMainAreaException | NotSupportedValueException e) {
            e.printStackTrace();

            System.out.println("sh "+ getY());
            System.out.println("sh "+ h);
            System.out.println("sh "+ (getY() + h));
//            h = Simulation.getMainArea().getHeight() - getY();
//            setHeight(h);
        }
    }

    public Boolean isInAreaXBoundary(Float x) {
        return (x < getX() )|| (x > (getX() + getWidth()));
    }

    public Boolean isInAreaYBoundary(Float y) {
        return (y < getY()) || (y > (getY() + getHeight()));
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
