package fr.utbm.gl52.droneSimulator.model;

import java.util.concurrent.ThreadLocalRandom;

public abstract class SimulationElement implements SimulationElementInterface {
    protected float[] coord;

    public float getX() {
        return coord[0];
    }

    public float getY() {
        return coord[1];
    }

    public void setX(float x) {
        coord[0] = x;
    }

    public void setY(float y) {
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

    public double distanceCalcul(float x, float y) {
        return Math.sqrt(Math.pow(distanceXCalcul(x), 2) + Math.pow(distanceYCalcul(y), 2));
    }

    private double distanceXCalcul(float x) {
        return Math.abs(getX() - x);
    }

    private double distanceYCalcul(float y) {
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
    protected float simplifyAngle(float angle) {
        // angle %= 2*Math.PI;
        while (angle > 2 * Math.PI)
            angle -= 2 * Math.PI;
        while (angle < 0)
            angle += 2 * Math.PI;
        return angle;
    }

    // ajuste le calcul trigonométrique de l'angle en fonction de la position relative du second objet
    public float angleCalcul(SimulationElement ge) {
        return angleCalcul(ge.getX(), ge.getY());
    }

    public float angleCalcul(float x, float y) {
        float YmaY = getY() - y;
        float XmaX = getX() - x;

        float angle;
        if (XmaX == 0) {
            if (YmaY < 0)
                angle = (float) (Math.PI / 2);
            else
                angle = (float) (-Math.PI / 2);
        } else if (YmaY == 0) {
            if (XmaX < 0)
                angle = 0;
            else
                angle = (float) Math.PI;
        } else {
            angle = (float) Math.atan(distanceYCalcul(y) / distanceXCalcul(x));

            if (XmaX < 0 && YmaY < 0)
                angle = -angle;
            else if (XmaX > 0 && YmaY < 0)
                angle += Math.PI;
            else if (XmaX > 0 && YmaY > 0)
                angle = (float) (Math.PI - angle);
        }

        return simplifyAngle(angle);
    }

    public static float degreeToRadian(Integer degres) {
        return (float) Math.toRadians(degres);
    }
}
