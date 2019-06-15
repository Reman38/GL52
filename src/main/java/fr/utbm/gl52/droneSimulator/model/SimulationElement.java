package fr.utbm.gl52.droneSimulator.model;

import fr.utbm.gl52.droneSimulator.model.exception.OutOfMainAreaException;

public abstract class SimulationElement implements SimulationElementInterface {
    protected Float[] coord = new Float[2];

    public SimulationElement(){
    }

    public static Area getArea(double x, double y) {
        Area areaReturn = null;
        for (Area area : Simulation.getAreas()) {
            if (
                x >= area.getX() && x < area.getX() + area.getWidth() &&
                y >= area.getY() && y < area.getY() + area.getHeight()
            ) {
                areaReturn = area;
            }
        }
        return areaReturn;
    }

    protected void setRandX(Area area) throws OutOfMainAreaException {
        setX(RandomHelper.getRandFloat(area.getX(), (area.getX() + area.getWidth())));
    }

    protected void setRandY(Area area) throws OutOfMainAreaException {
        setY(RandomHelper.getRandFloat(area.getY(), (area.getY() + area.getHeight())));
    }

    public void setRandCoord(Area area) {
        try {
            setRandX(area);
            setRandY(area);
        } catch (OutOfMainAreaException e) {
            e.printStackTrace();
        }
    }

    /* getteurs et setteurs triviaux */
    public Float[] getCoord() {
        return coord;
    }

    public Float getX() {
        return coord[0];
    }

    public Float getY() {
        return coord[1];
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

    public String toString() {
        String s = "coords: [" + getX() + "," + getY() + "]" + System.getProperty("line.separator");
        return s;
    }
}
