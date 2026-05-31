package net.vvxzv.cuisinetfc;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@SuppressWarnings("removal")
@EventBusSubscriber(modid = CuisineTFC.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue COMMENT = BUILDER.comment(" ", "Nutrients calculation is (score / 100) * nutrient / foodSize.", "营养值计算是 (分数 / 100) * 营养 / 食物份量)").define("comment", true);

    private static final ModConfigSpec.IntValue FOOD_SIZE = BUILDER.comment(" ").comment("Food size.  (defaultValue 3)").comment("食物份量.  (默认数值 3)").defineInRange("foodSize", 3, 1, 54);

    private static final ModConfigSpec.IntValue MAX_NUTRIENT = BUILDER.comment(" ").comment("The max nutrient.  (defaultValue 6)").comment("最大营养值.  (默认数值 6)").defineInRange("maxNutrient", 6, 1, Integer.MAX_VALUE);

    private static final ModConfigSpec.DoubleValue SUSPICIOUS_MIX_FACTOR = BUILDER.comment(" ").comment("The nutrient factor of Suspicious Mix.  (defaultValue 0.8)").comment("大乱炖的营养系数.  (默认数值 0.8)").defineInRange("suspiciousMixFactor", 0.8, 0, 1);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static int foodSize;
    public static int maxNutrient;
    public static double suspiciousMixFactor;


    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        foodSize = FOOD_SIZE.get();
        maxNutrient = MAX_NUTRIENT.get();
        suspiciousMixFactor = SUSPICIOUS_MIX_FACTOR.get();
    }
}
