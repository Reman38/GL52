package fr.utbm.gl52.droneSimulator.model;

public class MathHelper {
    public static Float simplifyAngle(Float angle) {
        while (angle > 2 * getPi())
            angle -= 2 * getPi();
        while (angle < 0)
            angle += 2 * getPi();
        return angle;
    }

    public static Float degreeToRadian(Integer degres) {
        return (float) Math.toRadians(degres);
    }

    public static Float getPi() {
        return (float) Math.PI;
    }
}
