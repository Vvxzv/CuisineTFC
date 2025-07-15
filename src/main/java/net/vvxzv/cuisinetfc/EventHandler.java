package net.vvxzv.cuisinetfc;

import dev.xkmc.cuisinedelight.content.item.BaseFoodItem;
import dev.xkmc.cuisinedelight.content.logic.CookedFoodData;
import net.dries007.tfc.common.capabilities.egg.EggCapability;
import net.dries007.tfc.common.capabilities.egg.EggHandler;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.common.capabilities.food.FoodData;
import net.dries007.tfc.common.capabilities.food.FoodHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class EventHandler {
    public static void init(){
        IEventBus bus = MinecraftForge.EVENT_BUS;
        bus.addGenericListener(ItemStack.class, EventHandler::attachItemCapabilities);
    }

    public static void attachItemCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack stack = event.getObject();
        if (!stack.isEmpty() && stack.getItem() instanceof BaseFoodItem) {

            FoodHandler food = getHandler(stack);
            if(food != null){
                event.addCapability(FoodCapability.KEY, food);
            }
            if (stack.getItem() == Items.EGG) {
                event.addCapability(EggCapability.KEY, new EggHandler(stack));
            }
        }

    }

    public static FoodHandler getHandler(ItemStack stack) {
        if(stack != null){
            CookedFoodData cookedFoodData = BaseFoodItem.getData(stack);
            if(cookedFoodData != null && !cookedFoodData.entries.isEmpty()){
                CompoundTag stackTag = stack.getOrCreateTag();
                CompoundTag nutrients = stackTag.getCompound("nutrients");
                int hunger = stackTag.getInt("hunger");
                float quality = cookedFoodData.score / 100f;
                float grain = nutrients.getFloat("grain");
                float fruit = nutrients.getFloat("fruit");
                float veg = nutrients.getFloat("veg");
                float meat = nutrients.getFloat("meat");
                float dairy = nutrients.getFloat("dairy");
                float[] nutrientsArray = calculateNutrients(grain, fruit, veg, meat, dairy, quality);
                var data = new FoodData(
                        hunger,
                        nutrientsArray[1] + nutrientsArray[2],
                        0.6f * hunger,
                        nutrientsArray[0],
                        nutrientsArray[1],
                        nutrientsArray[2],
                        nutrientsArray[3],
                        nutrientsArray[4],
                        2
                );
                return new FoodHandler(data);
            }
            return null;
        }
        return null;
    }

    public static float[] calculateNutrients(float arg0, float arg1, float arg2, float arg3, float arg4, float quality) {

        float[] inputArray = {arg0, arg1, arg2, arg3, arg4};

        int minIndex = 0;
        for (int i = 1; i < inputArray.length; i++) {
            if (inputArray[i] < inputArray[minIndex]) {
                minIndex = i;
            }
        }

        float[] resultArray = inputArray.clone();

        resultArray[minIndex] = 0f;

        for (int i = 0; i < resultArray.length; i++) {
            resultArray[i] = resultArray[i] * 0.5f * quality;
            if(resultArray[i] > 6) resultArray[i] = 6;
        }

        return resultArray;
    }
}