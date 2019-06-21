package fr.utbm.gl52.droneSimulator.model;

import java.util.concurrent.ThreadLocalRandom;

public abstract class RandomHelper {

    static Integer getRandInt(Integer min, Integer max) {
        int nb = 0;
        try {
            nb = ThreadLocalRandom.current().nextInt(min, max + 1);
        } catch (Exception ignored) {
        }
        return nb;
    }

    static Float getRandFloat(Float min, Float max) {
        if (min >= max)
            throw new IllegalArgumentException("max must be greater than min");
        float result = ThreadLocalRandom.current().nextFloat() * (max - min) + min;
        if (result >= max) // correct for rounding
            result = Float.intBitsToFloat(Float.floatToIntBits(max) - 1);
        return result;
    }
}
