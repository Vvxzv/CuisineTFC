package net.vvxzv.cuisinetfc;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public class Utils {
    public static CompoundTag getOrCreateNbt(ItemStack stack) {
        if (stack.isEmpty()) return new CompoundTag();
        var component = stack.get(DataComponents.CUSTOM_DATA);
        if (component == null) return new CompoundTag();
        return component.copyTag();
    }

    public static void setNbt(ItemStack stack, CompoundTag nbt) {
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));
    }

    public static float[] addTFCNutrients(float[] nutrients, float[] foodNutrients) {
        if(nutrients.length != 5 || foodNutrients.length != 5) return nutrients;
        float[] outputNutrients = nutrients.clone();
        for (int i = 0; i < 5; i++) {
            outputNutrients[i] += foodNutrients[i];
        }
        return outputNutrients;
    }


    public static float[] calculateNutrients(float[] inputArray, float quality, float factor, float maxNutrient) {
        int minIndex = 0;
        for (int i = 1; i < inputArray.length; i++) {
            if (inputArray[i] < inputArray[minIndex]) {
                minIndex = i;
            }
        }

        float[] resultArray = inputArray.clone();

        resultArray[minIndex] = 0f;

        for (int i = 0; i < resultArray.length; i++) {
            resultArray[i] = resultArray[i] * factor * quality;
            if(resultArray[i] > maxNutrient) resultArray[i] = maxNutrient;
        }

        return resultArray;
    }
}
