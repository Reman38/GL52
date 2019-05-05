package fr.utbm.gl52.droneSimulator.model;

import fr.utbm.gl52.droneSimulator.exception.OutOfMainAreaException;

public abstract class SimulationElement implements SimulationElementInterface {
    protected Float[] coord;

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

    protected void setRandX(Area area) {
        setX(RandomHelper.getRandFloat(area.getX() + getWidth() / 2, (area.getX() + area.getWidth()) - getWidth() / 2));
    }

    protected void setRandY(Area area) {
        setY(RandomHelper.getRandFloat(area.getY() + getHeight() / 2, (area.getY() + area.getHeight()) - getHeight() / 2));
    }

    public void setRandCoord(Area area) {
        setRandX(area);
        setRandY(area);
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
        coord[0] = x;
    }

    public void setY(Float y) throws OutOfMainAreaException {
        coord[1] = y;
    }

    public String toString() {
        String s = "coords: [" + getX() + "," + getY() + "]" + System.getProperty("line.separator");
        return s;
    }
}
