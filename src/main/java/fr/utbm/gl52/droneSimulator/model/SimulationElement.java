package fr.utbm.gl52.droneSimulator.model;

import fr.utbm.gl52.droneSimulator.model.exception.OutOfMainAreaException;

public abstract class SimulationElement implements SimulationElementInterface {
    protected Float[] coord = new Float[2];

    public SimulationElement() {
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

    /**
     * Set a rand abscissa within the area boundaries
     *
     * @param area Area in which the abscissa should be set
     *
     * @throws OutOfMainAreaException The abscissa exceeds the area boundaries
     */
    protected void setRandX(Area area) throws OutOfMainAreaException {
//        setX(RandomHelper.getRandFloat(area.getX(), (area.getX() + area.getWidth())));
        setX(area.getWidth()/2);
    }

    /**
     * Set a rand ordinate within the area boundaries
     *
     * @param area Area in which the ordinate should be set
     *
     * @throws OutOfMainAreaException The ordinate exceeds the area boundaries
     */
    protected void setRandY(Area area) throws OutOfMainAreaException {
//        setY(RandomHelper.getRandFloat(area.getY(), (area.getY() + area.getHeight())));
        setY(area.getHeight()/2);
    }

    /**
     * Set Rand coordinates in a given area
     *
     * @param area The area i which the element should be placed
     *
     * @throws OutOfMainAreaException The element is out of the main area
     */
    public void setRandCoord(Area area) throws OutOfMainAreaException {
        setRandX(area);
        setRandY(area);
    }

    /* getteurs et setteurs triviaux */

    /**
     * Get the coordinate array [x,Y]
     *
     * @return The coordinates
     */
    public Float[] getCoord() {
        return coord;
    }

    /**
     * Set the coordinates of the element
     *
     * @param x the abscissa position in meters
     * @param y the ordinate position in meters
     *
     * @throws OutOfMainAreaException x or y are not in the main area
     */
    public void setCoord(Float x, Float y) throws OutOfMainAreaException{
        setX(x);
        setY(y);
    }

    /**
     * Get the abscissa of the element
     *
     * @return abscissa in meters
     */
    public Float getX() {
        return coord[0];
    }

    /**
     * get the ordinate of the element
     *
     * @return ordinate in meter
     */
    public Float getY() {
        return coord[1];
    }

    /**
     * Set the abscissa of the element
     *
     * @param x the abscissa position in meters
     *
     * @throws OutOfMainAreaException x is not in the main area
     */
    public void setX(Float x) throws OutOfMainAreaException {
        if (Simulation.getMainArea().isInAreaXBoundary(x))
            throw new OutOfMainAreaException("x out of mainArea boundary : " + x);
        else
            coord[0] = x;
    }

    /**
     * Set the coordinates of the element
     *
     * @param y the ordinate position in meters
     *
     * @throws OutOfMainAreaException y is not in the main area
     */
    public void setY(Float y) throws OutOfMainAreaException {
        if (Simulation.getMainArea().isInAreaYBoundary(y))
            throw new OutOfMainAreaException("y out of mainArea boundary : " + y);
        else
            coord[1] = y;
    }

    @Override
    public String toString() {
        String s = "coords: [" + getX() + "," + getY() + "]" + System.getProperty("line.separator");
        return s;
    }
}
