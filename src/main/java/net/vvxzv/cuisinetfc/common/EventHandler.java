package net.vvxzv.cuisinetfc.common;

import net.vvxzv.cuisinetfc.Config;

public class EventHandler {
    private static float factor;
    private static int maxNutrient;
    private static float suspiciousMixFactor;
    private static float rottenFoodDecayFactor;

    static {
        updateConfigValues();
    }

    public static void updateConfigValues() {
        factor = (float) Config.cuisineBonus / Config.foodSize;
        maxNutrient = Config.maxNutrient;
        suspiciousMixFactor = (float) Config.suspiciousMixFactor;
        rottenFoodDecayFactor = (float) Config.usedRottenFood;
    }
}
