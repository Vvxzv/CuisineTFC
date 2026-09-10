package net.vvxzv.cuisinetfc;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = CuisineTFC.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue COMMENT = BUILDER.comment(" ", "Nutrients calculation is (score / 100) * nutrient * bonus / maxFoodSize.", "营养值计算是 (分数 / 100) * 营养 * 额外营养倍率 / 最大食用次数").define("comment", true);

    private static final ForgeConfigSpec.IntValue MAX_NUTRIENT = BUILDER.comment(" ", "The max nutrient.  (defaultValue 6)", "最大营养值.  (默认数值 6)").defineInRange("maxNutrient", 6, 1, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue MAX_FOOD_SIZE = BUILDER.comment(" ", "The maximum number of times food can be consumed", "食物可以食用的次数", "Default 3").defineInRange("maxFoodSize", 3, 1, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.DoubleValue BONUS = BUILDER.comment(" ", "Extra nutrient bonus.", "额外营养倍率", "Default 1.5").defineInRange("bonus", 1.5, 0, Double.MAX_VALUE);

    private static final ForgeConfigSpec.DoubleValue SUSPICIOUS_MIX_FACTOR = BUILDER.comment(" ", "The nutrient factor of Suspicious Mix.  (defaultValue 0.8)", "大乱炖的营养系数.  (默认数值 0.8)").defineInRange("suspiciousMixFactor", 0.8, 0, 1);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static int maxNutrient;
    public static int maxFoodSize;
    public static double bonus;
    public static double suspiciousMixFactor;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event){
        maxNutrient = MAX_NUTRIENT.get();
        maxFoodSize = MAX_FOOD_SIZE.get();
        bonus = BONUS.get();
        suspiciousMixFactor = SUSPICIOUS_MIX_FACTOR.get();
    }
}
