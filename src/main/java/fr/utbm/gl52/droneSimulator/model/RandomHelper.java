package fr.utbm.gl52.droneSimulator.model;

import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom; // TODO revoir l'utilité

public abstract class RandomHelper {
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

    public static Double getRandDouble(Double min, Double max) {
        Double nb = 0d;
        try {
            nb = ThreadLocalRandom.current().nextDouble(min, max + 1);
        } catch (Exception e) {
        }
        return nb;
    }

    public static Float getRandFloat(Float min, Float max) {
        if (min >= max)
            throw new IllegalArgumentException("max must be greater than min");
        float result = ThreadLocalRandom.current().nextFloat() * (max - min) + min;
        if (result >= max) // correct for rounding
            result = Float.intBitsToFloat(Float.floatToIntBits(max) - 1);
        return result;
    }

    public static String getRandValueOf(String[] ts) {
        return ts[getRandInt(0, ts.length - 1)];
    }

    public static String getRandStringOf(Vector<String> v) {
        return v.get(getRandInt(0, v.size() - 1));
    }
}
