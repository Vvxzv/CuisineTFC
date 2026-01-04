package net.vvxzv.cuisinetfc;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;


@Mod.EventBusSubscriber(modid = CuisineTFC.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue COMMENT = BUILDER.comment(" ").comment("Nutrients calculation is (score / 100) * nutrient * (cuisineBonus / foodSize).").comment("营养值计算是 (分数 / 100) * 营养 * (炒菜加成 / 食物份量)").define("comment", true);

    private static final ForgeConfigSpec.IntValue FOOD_SIZE = BUILDER.comment(" ").comment("Food size.  (defaultValue 3)").comment("食物份量.  (默认数值 3)").defineInRange("foodSize", 3, 1, 54);

    private static final ForgeConfigSpec.DoubleValue CUISINE_BONUS = BUILDER.comment(" ").comment("Cuisine bonus.  (defaultValue 1.5)").comment("炒菜加成.  (默认数值 1.5)").defineInRange("cuisineBonus", 1.5, 1, 27);

    private static final ForgeConfigSpec.IntValue MAX_NUTRIENT = BUILDER.comment(" ").comment("The max nutrient.  (defaultValue 6)").comment("最大营养值.  (默认数值 6)").defineInRange("maxNutrient", 6, 1, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.DoubleValue SUSPICIOUS_MIX_FACTOR = BUILDER.comment(" ").comment("The nutrient factor of Suspicious Mix.  (defaultValue 0.8)").comment("大乱炖的营养系数.  (默认数值 0.8)").defineInRange("suspiciousMixFactor", 0.8, 0, 1);

    private static final ForgeConfigSpec.DoubleValue USED_ROTTEN_FOOD = BUILDER.comment(" ").comment("This is a factor related to the nutrients value of cooking rotten food.  (defaultValue 0.2)").comment("这是一个与烹饪腐烂食物的营养价值有关的因素.  (默认数值 0.2)").defineInRange("usedRottenFoodNutrientFactor", 0.2, 0, 1);

    private static final ForgeConfigSpec.DoubleValue USED_ROTTEN_FOOD_DECAYING_MODIFIER = BUILDER.comment(" ").comment("This is decaying modifier related to cook rotten food.  (defaultValue 999999)").comment("这是一个烹饪腐烂食物后对保质期修改的值，值越大保质期越短.  (默认数值 999999)").defineInRange("usedRottenFoodDecayingModifier", 999999, 0, Double.MAX_VALUE);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static int foodSize;
    public static double cuisineBonus;
    public static int maxNutrient;
    public static double suspiciousMixFactor;
    public static double usedRottenFoodNutrientFactor;
    public static double usedRottenFoodDecayingModifier;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event){
        foodSize = FOOD_SIZE.get();
        cuisineBonus = CUISINE_BONUS.get();
        maxNutrient = MAX_NUTRIENT.get();
        suspiciousMixFactor = SUSPICIOUS_MIX_FACTOR.get();
        usedRottenFoodNutrientFactor = USED_ROTTEN_FOOD.get();
        usedRottenFoodNutrientFactor = USED_ROTTEN_FOOD.get();
        usedRottenFoodDecayingModifier = USED_ROTTEN_FOOD_DECAYING_MODIFIER.get();
    }
}
