package net.vvxzv.cuisinetfc;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = CuisineTFC.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue COMMENT = BUILDER.comment("Nutrients calculation is (score / 100) * nutrient * (cuisineBonus / foodSize).").comment("营养值计算是 (分数 / 100) * 营养 * (炒菜加成 / 食物份量)").define("comment", true);

    private static final ForgeConfigSpec.IntValue FOOD_SIZE = BUILDER.comment(" ").comment(" ").comment("Food size.  (defaultValue 3)").comment("食物份量.  (默认数值 3)").defineInRange("foodSize", 3, 1, 54);

    private static final ForgeConfigSpec.DoubleValue CUISINE_BONUS = BUILDER.comment(" ").comment("Cuisine bonus.  (defaultValue 1.5)").comment("炒菜加成.  (默认数值 1.5)").defineInRange("cuisineBonus", 1.5, 1, 27);

    private static final ForgeConfigSpec.IntValue MAX_NUTRIENT = BUILDER.comment(" ").comment("The max nutrient.  (defaultValue 6)").comment("最大营养值.  (默认数值 6)").defineInRange("maxNutrient", 6, 1, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.DoubleValue SUSPICIOUS_MIX_FACTOR = BUILDER.comment(" ").comment("The nutrient factor of Suspicious Mix.  (defaultValue 0.8)").comment("大乱炖的营养系数.  (默认数值 0.8)").defineInRange("suspiciousMixFactor", 0.8, 0, 1);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static int foodSize;
    public static double cuisineBonus;
    public static int maxNutrient;
    public static double suspiciousMixFactor;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event){
        foodSize = FOOD_SIZE.get();
        cuisineBonus = CUISINE_BONUS.get();
        maxNutrient = MAX_NUTRIENT.get();
        suspiciousMixFactor = SUSPICIOUS_MIX_FACTOR.get();

        EventHandler.updateConfigValues();
    }
}
