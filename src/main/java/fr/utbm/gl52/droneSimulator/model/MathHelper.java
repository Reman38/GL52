package fr.utbm.gl52.droneSimulator.model;

public abstract class MathHelper {
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

    public static double calculDistanceWith(SimulationElement se1, SimulationElement se2) {
        return Math.sqrt(Math.pow(distanceXCalcul(se1, se2), 2) + Math.pow(distanceYCalcul(se1, se2), 2));
    }

    private static double distanceXCalcul(SimulationElement se1, SimulationElement se2) {
        return computeDistance(se1.getX(), se2.getX());
    }

    private static double distanceYCalcul(SimulationElement se1, SimulationElement se2) {
        return computeDistance(se1.getY(), se2.getY());
    }

    private static double computeDistance(Float d, Float d1){
        return StrictMath.abs(d - d1);
    }

    public static Float calculAngleWith(SimulationElement se1, SimulationElement se2) {
        return calculAngleWith(se1.getX(), se2.getX(), se1.getY(), se2.getY());
    }

    public static Float calculAngleWith(Float x, Float x1, Float y, Float y1) {
        Float YmaY = y - y1;
        Float XmaX = x - x1;

        Float angle;
        if (XmaX == 0) {
            if (YmaY < 0)
                angle = (MathHelper.getPi() / 2);
            else
                angle = (-MathHelper.getPi() / 2);
        } else if (YmaY == 0) {
            if (XmaX < 0)
                angle = 0f;
            else
                angle = MathHelper.getPi();
        } else {
            angle = (float) Math.atan(computeDistance(y, y1) / computeDistance(x, x1));

            if (XmaX < 0 && YmaY < 0)
                angle = -angle;
            else if (XmaX > 0 && YmaY < 0)
                angle += MathHelper.getPi();
            else if (XmaX > 0 && YmaY > 0)
                angle = (MathHelper.getPi() - angle);
        }

        return MathHelper.simplifyAngle(angle);
    }

    public static Float computeVectorNorm(SimulationElement simulationElement, SimulationElement simulationElement1){

        return computeVectorNorm(simulationElement.getX(), simulationElement1.getX(), simulationElement.getY(), simulationElement1.getY());
    }

    public static Float computeVectorNorm(Float x, Float x1, Float y, Float y1){

        float x2 = (float) StrictMath.pow(StrictMath.abs(x - x1), 2);
        float y2 = (float) StrictMath.pow(StrictMath.abs(y - y1), 2);
        return (float) StrictMath.sqrt(x2 + y2);
    }
}
