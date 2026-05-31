package net.vvxzv.cuisinetfc.common.utils;

import dev.xkmc.cuisinedelight.content.logic.CookedFoodData;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.common.capabilities.food.IFood;
import net.minecraft.world.item.ItemStack;
import net.vvxzv.cuisinetfc.common.data.CookedFood;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static List<ItemStack> getItemFromCookedFoodData(CookedFoodData cookedFoodData) {
        List<ItemStack> itemStacks = new ArrayList<>();
        for (CookedFoodData.Entry entry: cookedFoodData.entries) {
            itemStacks.add(entry.stack());
        }
        return itemStacks;
    }

    public static float[] getTotalCookedFoodNutrients(List<ItemStack> itemStacks) {
        float[] totalNutrients = new float[]{0f, 0f, 0f, 0f, 0f};
        for (ItemStack itemStack : itemStacks) {
            if (itemStack != null) {
                float[] foodNutrients = new float[]{0f, 0f, 0f, 0f, 0f};

                IFood iFood = FoodCapability.get(itemStack);
                if (iFood != null){
                    foodNutrients = iFood.getData().nutrients();
                }

                CookedFood cookedFood = CookedFood.get(itemStack);
                if(cookedFood != null) {
                    foodNutrients = cookedFood.getNutrients();
                }

                for (int i = 0; i < totalNutrients.length; i++) {
                    totalNutrients[i] += (foodNutrients[i] * itemStack.getCount());
                }
            }
        }

        return totalNutrients;
    }

    public static float[] calculateNutrients(float[] inputArray, float score, float factor, float maxNutrient) {
        float minValue = inputArray[0];
        for (float num : inputArray) {
            if (num < minValue) {
                minValue = num;
            }
        }

        int minIndex = 0;
        for (int i = 0; i < inputArray.length; i++) {
            if (inputArray[i] == minValue) {
                minIndex = i;
            }
        }

        float[] resultArray = inputArray.clone();
        resultArray[minIndex] = 0f;

        for (int i = 0; i < resultArray.length; i++) {
            resultArray[i] = resultArray[i] * factor * score / 100F;
            if(resultArray[i] > maxNutrient) resultArray[i] = maxNutrient;
        }

        return resultArray;
    }

    public static float calculateDecayModifier(List<ItemStack> itemStacks) {
        float decayModifier = 0f;
        for (ItemStack itemStack : itemStacks) {
            if (itemStack != null) {
                IFood iFood = FoodCapability.get(itemStack);
                if (iFood != null){
                    decayModifier = Math.max(decayModifier, iFood.getData().decayModifier());
                }
            }
        }

        return decayModifier * 0.8f;
    }

    public static int getTotalItemCount(List<ItemStack> itemStacks) {
        int count = 0;
        for (ItemStack itemStack : itemStacks) {
            if (itemStack != null) {
                count += itemStack.getCount();
            }
        }

        return count;
    }

}
