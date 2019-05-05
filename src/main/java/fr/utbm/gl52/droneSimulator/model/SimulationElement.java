package fr.utbm.gl52.droneSimulator.model;

import fr.utbm.gl52.droneSimulator.exception.OutOfMainAreaException;

public abstract class SimulationElement implements SimulationElementInterface {
    protected Float[] coord;

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

    public double distanceCalcul(SimulationElement ge) {
        return Math.sqrt(Math.pow(distanceXCalcul(ge), 2) + Math.pow(distanceYCalcul(ge), 2));
    }

    public double distanceXCalcul(SimulationElement ge) {
        return Math.abs(getX() - ge.getX());
    }

    public double distanceYCalcul(SimulationElement ge) {
        return Math.abs(getY() - ge.getY());
    }

    public double distanceCalcul(Float x, Float y) {
        return Math.sqrt(Math.pow(distanceXCalcul(x), 2) + Math.pow(distanceYCalcul(y), 2));
    }

    private double distanceXCalcul(Float x) {
        return Math.abs(getX() - x);
    }

    private double distanceYCalcul(Float y) {
        return Math.abs(getY() - y);
    }

    public static Area getArea(double x, double y) {
        Area areaReturn = null;
        for (Area area : Simulation.getAreas()) {
            if (
                    x >= area.getX() && x < area.getX() + area.getWidth()
                            && y >= area.getY() && y < area.getY() + area.getHeight()
            ) {
                areaReturn = area;
            }
        }
        return areaReturn;
    }

    public void setRandX(Area area) {
        setX(RandomHelper.getRandFloat(area.getX() + getWidth() / 2, (area.getX() + area.getWidth()) - getWidth() / 2));
    }

    public void setRandY(Area area) {
        setY(RandomHelper.getRandFloat(area.getY() + getHeight() / 2, (area.getY() + area.getHeight()) - getHeight() / 2));
    }

    public void setRandCoord(Area area) {
        setRandX(area);
        setRandY(area);
    }

    /*
        Fonctions mathématiques
    */
    // Modulo
    protected Float simplifyAngle(Float angle) {
        // angle %= 2*Math.PI;
        while (angle > 2 * Math.PI)
            angle -= 2 * Math.PI;
        while (angle < 0)
            angle += 2 * Math.PI;
        return angle;
    }

    // ajuste le calcul trigonométrique de l'angle en fonction de la position relative du second objet
    public Float angleCalcul(SimulationElement ge) {
        return angleCalcul(ge.getX(), ge.getY());
    }

    public Float angleCalcul(Float x, Float y) {
        Float YmaY = getY() - y;
        Float XmaX = getX() - x;

        Float angle;
        if (XmaX == 0) {
            if (YmaY < 0)
                angle = (Float) (Math.PI / 2);
            else
                angle = (Float) (-Math.PI / 2);
        } else if (YmaY == 0) {
            if (XmaX < 0)
                angle = 0;
            else
                angle = (Float) Math.PI;
        } else {
            angle = (Float) Math.atan(distanceYCalcul(y) / distanceXCalcul(x));

            if (XmaX < 0 && YmaY < 0)
                angle = -angle;
            else if (XmaX > 0 && YmaY < 0)
                angle += Math.PI;
            else if (XmaX > 0 && YmaY > 0)
                angle = (Float) (Math.PI - angle);
        }

        return simplifyAngle(angle);
    }

    public static Float degreeToRadian(Integer degres) {
        return (Float) Math.toRadians(degres);
    }
}
