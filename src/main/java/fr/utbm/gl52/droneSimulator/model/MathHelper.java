package fr.utbm.gl52.droneSimulator.model;

public abstract class MathHelper {

    static final long nanosecondsInASecond = (long) StrictMath.pow(10, 9);

    /**
     * Get the equivalent angle between 0 and 2pi
     *
     * @param angle Angle in rad
     * @return simplified angle in rad
     */
    static Float simplifyAngle(Float angle) {
        while (angle > 2 * getPi())
            angle -= 2 * getPi();
        while (angle < 0)
            angle += 2 * getPi();
        return angle;
    }


    public static Float getPi() {
        return (float) Math.PI;
    }

    /**
     * Calculate the distance between to simulation elements
     *
     * @param se1 element 1
     * @param se2 element 2
     * @return distance in meter
     */
    static double calculDistanceWith(SimulationElement se1, SimulationElement se2) {
        return Math.sqrt(Math.pow(distanceXCalcul(se1, se2), 2) + Math.pow(distanceYCalcul(se1, se2), 2));
    }

    /**
     * Calculate distance between two simulation elements in abscissa only
     *
     * @param se1 element 1
     * @param se2 element 2
     * @return distance in meter
     */
    private static double distanceXCalcul(SimulationElement se1, SimulationElement se2) {
        return computeDistance(se1.getX(), se2.getX());
    }

    /**
     * Calculate distance between two simulation elements in ordinate only
     *
     * @param se1 element 1
     * @param se2 element 2
     * @return distance in meter
     */
    private static double distanceYCalcul(SimulationElement se1, SimulationElement se2) {
        return computeDistance(se1.getY(), se2.getY());
    }

    private static double computeDistance(Float d, Float d1){
        return StrictMath.abs(d - d1);
    }

    /**
     * Calculate the angle between two simulation elements
     *
     * @param se1 element 1
     * @param se2 element 2
     * @return angle in rad
     */
    static Float calculAngleWith(SimulationElement se1, SimulationElement se2) {
        return calculAngleWith(se1.getX(), se2.getX(), se1.getY(), se2.getY());
    }

    /**
     * Calculate the angle between two simulation elements represented by their coordinates
     *
     * @param x Abscissa of the first element
     * @param x1 Abscissa of the second element
     * @param y Ordinate of the first element
     * @param y1 Ordinate of the second element
     * @return angle in rad
     */
    static Float calculAngleWith(Float x, Float x1, Float y, Float y1) {
        float YmaY = y - y1;
        float XmaX = x - x1;

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

    static Float computeVectorNorm(SimulationElement simulationElement, SimulationElement simulationElement1){

        return computeVectorNorm(simulationElement.getX(), simulationElement1.getX(), simulationElement.getY(), simulationElement1.getY());
    }

    static Float computeVectorNorm(Float x, Float x1, Float y, Float y1){

        float x2 = (float) StrictMath.pow(StrictMath.abs(x - x1), 2);
        float y2 = (float) StrictMath.pow(StrictMath.abs(y - y1), 2);
        return (float) StrictMath.sqrt(x2 + y2);
    }

    static float convertNanosecondsToSeconds(long deltaT) {
        return (float)(deltaT) / nanosecondsInASecond;
    }

    static Float convertNanosecondsToMinutes(Long deltaT){
        return convertNanosecondsToSeconds(deltaT) / 60;
    }

    static Float convertMillisecondsToMinutes(Long elapsedTime) {
        return elapsedTime / 60000f;
    }
}
