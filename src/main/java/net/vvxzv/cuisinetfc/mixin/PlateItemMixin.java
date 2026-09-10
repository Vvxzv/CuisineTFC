package net.vvxzv.cuisinetfc.mixin;

import dev.xkmc.cuisinedelight.content.item.PlateItem;
import dev.xkmc.cuisinedelight.content.logic.CookedFoodData;
import dev.xkmc.cuisinedelight.init.registrate.PlateFood;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.vvxzv.cuisinetfc.Config;
import net.vvxzv.cuisinetfc.common.utils.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(PlateItem.class)
public class PlateItemMixin {

    @Unique
    private int getMaxFoodSize() {
        return Config.maxFoodSize;
    }

    @Unique
    private float factor() {
        return (float) (Config.bonus / this.getMaxFoodSize());
    }

    @Unique
    private int getMaxNutrient() {
        return Config.maxNutrient;
    }

    @Unique
    private float getSuspiciousMixFactor(){
        return (float) Config.suspiciousMixFactor;
    }

    @Redirect(method = "useOn", at = @At(value = "INVOKE", target = "Ldev/xkmc/cuisinedelight/content/item/PlateItem;giveBack(Lnet/minecraft/world/item/ItemStack;Ldev/xkmc/cuisinedelight/content/logic/CookedFoodData;Ldev/xkmc/cuisinedelight/content/item/PlateItem$ReturnTarget;)V", remap = false))
    private void usePlateTakeFood(PlateItem instance, ItemStack foodStack, CookedFoodData food, PlateItem.ReturnTarget target) {
        target.addExp(food.score * food.size / 100);

        List<ItemStack> itemStacks = Utils.getItemFromCookedFoodData(food);
        int count = Utils.getTotalItemCount(itemStacks);
        float[] totalNutrients = Utils.getTotalCookedFoodNutrients(itemStacks);
        float[] nutrients = Utils.calculateNutrients(totalNutrients, food.score, this.factor(), this.getMaxNutrient());

        if(foodStack.is(PlateFood.SUSPICIOUS_MIX.item.get())) {
            for (int i = 0; i < nutrients.length; i++) {
                nutrients[i] *= this.getSuspiciousMixFactor();
            }
        }

        CompoundTag stackTag = foodStack.getOrCreateTag();

        CompoundTag cookedFoodData = stackTag.getCompound("CookedFoodData");
        cookedFoodData.putInt("size", Math.min(count, this.getMaxFoodSize()));
        stackTag.put("CookedFoodData", cookedFoodData);

        CompoundTag foodData = new CompoundTag();
        foodData.putInt("food", 4);
        foodData.putFloat("water", (nutrients[1] + nutrients[2]) * 5);
        foodData.putFloat("sat", 0.8f * count);
        foodData.putFloat("grain", nutrients[0]);
        foodData.putFloat("fruit", nutrients[1]);
        foodData.putFloat("veg", nutrients[2]);
        foodData.putFloat("meat", nutrients[3]);
        foodData.putFloat("dairy", nutrients[4]);
        foodData.putFloat("decay", Utils.calculateDecayModifier(itemStacks));
        stackTag.put("foodData", foodData);

        foodStack.setTag(stackTag);

        target.addItem(foodStack);
    }
}
