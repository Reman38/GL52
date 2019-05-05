package fr.utbm.gl52.droneSimulator.model;

import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

public class RandomHelper {
    public static Boolean getRandBool() {
        Boolean bool = false;
        if (getRandInt(0, 1) == 1) {
            bool = true;
        }
        return bool;
    }

    public static Integer getRandInt(Integer min, Integer max) {
        Integer nb = 0;
        try {
            nb = ThreadLocalRandom.current().nextInt(min, max + 1);
        } catch (Exception e) {
        }
        return nb;
    }

    public static double getRandDouble(double min, double max) {
        double nb = 0;
        try {
            nb = ThreadLocalRandom.current().nextDouble(min, max + 1);
        } catch (Exception e) {
        }
        return nb;
    }

    public static Float getRandFloat(Float min, Float max) {
        Float nb = 0;
        try {
            nb = min + (max - min) * (ThreadLocalRandom.current().nextFloat());
        } catch (Exception e) {
        }

        return nb;
    }

    public static String getRandValueOf(String[] ts) {
        return ts[getRandInt(0, ts.length - 1)];
    }

    public static String getRandStringOf(Vector<String> v) {
        return v.get(getRandInt(0, v.size() - 1));
    }
}
