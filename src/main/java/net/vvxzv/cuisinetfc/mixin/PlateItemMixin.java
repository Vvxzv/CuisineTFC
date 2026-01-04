package net.vvxzv.cuisinetfc.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.xkmc.cuisinedelight.content.block.CuisineSkilletBlockEntity;
import dev.xkmc.cuisinedelight.content.item.PlateItem;
import dev.xkmc.cuisinedelight.content.logic.CookedFoodData;
import dev.xkmc.cuisinedelight.init.registrate.CDItems;
import net.dries007.tfc.common.component.food.FoodCapability;
import net.dries007.tfc.common.component.food.FoodData;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.vvxzv.cuisinetfc.Config;
import net.vvxzv.cuisinetfc.common.TFCNutrientsHolder;
import net.vvxzv.cuisinetfc.common.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlateItem.class)
public class PlateItemMixin {
    @Unique
    private static int getFoodSize(){
        return Config.foodSize;
    }
    @Unique
    private static float getFactor() {
        return  (float) Config.cuisineBonus / Config.foodSize;
    };
    @Unique
    private static int getMaxNutrient() {
        return Config.maxNutrient;
    }
    @Unique
    private static float getSuspiciousMixFactor() {
        return (float) Config.suspiciousMixFactor;
    }
    @Unique
    private static float getRottenFoodDecayFactor() {
        return 200000F / (float) Config.usedRottenFood;
    }

    @Unique
    private static float getPunishFactor(){
        return (float) Config.usedRottenFood;
    }

    @Inject(
            method = "useOn",
            at = @At(
                    value = "INVOKE",
                    target = "Ldev/xkmc/cuisinedelight/content/item/PlateItem;giveBack(Lnet/minecraft/world/item/ItemStack;Ldev/xkmc/cuisinedelight/content/logic/CookedFoodData;Ldev/xkmc/cuisinedelight/content/item/PlateItem$ReturnTarget;)V",
                    remap = false
            )
    )
    public void onPlateUseOnSkillet(
            UseOnContext ctx,
            CallbackInfoReturnable<InteractionResult> cir,
            @Local(name = "level") Level level,
            @Local(name = "be") CuisineSkilletBlockEntity be,
            @Local(name = "food") CookedFoodData food,
            @Local(name = "foodStack") ItemStack foodStack
    ) {
        if (level.isClientSide() || !(be instanceof TFCNutrientsHolder holder)) return;

        float[] nutrients = holder.getTFCNutrients();
        int hunger = food.size();
        float decayModifier = 2F;

        if(holder.hasRottenFood()){
            for(int i = 0; i < nutrients.length; i++){
                nutrients[i] = nutrients[i] * getPunishFactor();
            }
            hunger = (int) (hunger * getPunishFactor() * 3F);
            if (hunger == 0) hunger = 1;
            decayModifier = getRottenFoodDecayFactor();
        }

        //System.out.println(decayModifier);

        CookedFoodData foodData = new CookedFoodData(food.total(), getFoodSize(), food.nutrition(), food.score(), food.types(), food.entries());
        foodStack.set(CDItems.COOKED, foodData);

        float[] nutrientsArray = Utils.calculateNutrients(foodStack, nutrients, foodData.score() / 100F, getFactor(), getMaxNutrient(), getSuspiciousMixFactor());

        //System.out.println(" "+nutrientsArray[0]+", "+nutrientsArray[1]+", "+nutrientsArray[2]+", "+nutrientsArray[3]+", "+nutrientsArray[4]);

        FoodData tfcFoodData = new FoodData(hunger, nutrientsArray[1] + nutrientsArray[2], 0.6f * hunger, 0, nutrientsArray, decayModifier);

        FoodCapability.setFoodForDynamicItemOnCreate(foodStack, tfcFoodData);

        holder.reset();
    }
}
