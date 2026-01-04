package net.vvxzv.cuisinetfc.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.xkmc.cuisinedelight.content.item.PlateItem;
import dev.xkmc.cuisinedelight.content.logic.CookedFoodData;
import dev.xkmc.cuisinedelight.init.registrate.CDItems;
import dev.xkmc.cuisinedelight.init.registrate.PlateFood;
import net.dries007.tfc.common.component.food.FoodCapability;
import net.dries007.tfc.common.component.food.FoodData;
import net.dries007.tfc.common.component.food.IFood;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.vvxzv.cuisinetfc.Config;
import net.vvxzv.cuisinetfc.Utils;
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
        return (float) Config.usedRottenFoodDecayingModifier;
    }
    @Unique
    private static float getPunishFactor(){
        return (float) Config.usedRottenFoodNutrientFactor;
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
            @Local(name = "food") CookedFoodData food,
            @Local(name = "foodStack") ItemStack foodStack
    ) {
        if (level.isClientSide()) return;

        int hunger = food.size();
        float decayModifier = 2f;
        float[] nutrients = new float[]{0f, 0f, 0f, 0f, 0f};
        boolean hasRotten = false;

        int[] indexHolder = {0};

        ItemStack[] itemStacks = new ItemStack[9];

        food.entries().forEach(entry -> {
            int currentIndex = indexHolder[0];
            if (currentIndex < itemStacks.length) {
                itemStacks[currentIndex] = entry.stack();
                indexHolder[0]++;
            }
        });

        for (ItemStack itemStack : itemStacks) {
            if(itemStack == null) continue;
            IFood iFood = FoodCapability.get(itemStack);
            if (iFood != null){
                int count = itemStack.getCount();
                float[] foodNutrients = iFood.getData().nutrients();
                if(itemStack.is(Items.EGG)){
                    foodNutrients[3] = 1.5f;
                    foodNutrients[4] = 0.3f;
                }
                for (int i = 0; i < nutrients.length; i++) {
                    foodNutrients[i] *= count;
                }
                nutrients = Utils.addTFCNutrients(nutrients, foodNutrients);
                if(iFood.isRotten()) hasRotten = true;
            }
        }

        if(hasRotten){
            for(int i = 0; i < nutrients.length; i++){
                nutrients[i] = nutrients[i] * getPunishFactor();
            }
            hunger = (int) (hunger * getPunishFactor() * 3f);
            if (hunger == 0) hunger = 1;
            decayModifier = getRottenFoodDecayFactor();

        }

        foodStack.set(CDItems.COOKED, new CookedFoodData(food.total(), getFoodSize(), food.nutrition(), food.score(), food.types(), food.entries()));

        float[] nutrientsArray = Utils.calculateNutrients(nutrients, food.score() / 100F, getFactor(), getMaxNutrient());

        for (int i = 0; i < nutrientsArray.length; i++) {
            if(foodStack.is(PlateFood.SUSPICIOUS_MIX.item.get())) {
                nutrientsArray[i] *= getSuspiciousMixFactor();
            }

        }

        FoodData tfcFoodData = new FoodData(hunger, nutrientsArray[1] + nutrientsArray[2], 0.6f * hunger, 0, nutrientsArray, decayModifier);

        FoodCapability.setFoodForDynamicItemOnCreate(foodStack, tfcFoodData);
    }
}
