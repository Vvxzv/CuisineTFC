package net.vvxzv.cuisinetfc;

import dev.xkmc.cuisinedelight.content.item.BaseFoodItem;
import dev.xkmc.cuisinedelight.content.logic.CookedFoodData;
import dev.xkmc.cuisinedelight.init.registrate.PlateFood;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.common.capabilities.food.FoodData;
import net.dries007.tfc.common.capabilities.food.FoodHandler;
import net.dries007.tfc.common.capabilities.food.IFood;
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

    private static float getFactor(){
        return  (float) Config.cuisineBonus / Config.foodSize;
    };
    private static int getMaxNutrient() {
        return Config.maxNutrient;
    };
    private static float getSuspiciousMixFactor(){
        return (float) Config.suspiciousMixFactor;
    };

    private static float getRottenFoodDecayFactor() {
        return (float) Config.usedRottenFoodDecayingModifier;
    };

    private static float getPunishFactor(){
        return (float) Config.usedRottenFoodNutrientFactor;
    }

    public static void attachItemCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack stack = event.getObject();
        if (!stack.isEmpty() && stack.getItem() instanceof BaseFoodItem) {
            FoodHandler food = getHandler(stack);
            if(food != null){
                event.addCapability(FoodCapability.KEY, food);
            }
        }

    }

    public static FoodHandler getHandler(ItemStack stack) {
        CookedFoodData cookedFoodData = BaseFoodItem.getData(stack);
        if(cookedFoodData != null && !cookedFoodData.entries.isEmpty()){
            ItemStack[] itemStacks = new ItemStack[9];
            int[] indexHolder = {0};

            int hunger = cookedFoodData.size;
            float decayModifier = 2f;
            float[] nutrients = new float[]{0f, 0f, 0f, 0f, 0f};
            boolean hasRotten = false;

            cookedFoodData.entries.forEach(entry -> {
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
                    nutrients = addTFCNutrients(nutrients, foodNutrients);
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

            float[] nutrientsArray = calculateNutrients(nutrients, cookedFoodData.score / 100F, getFactor(), getMaxNutrient());

            for (int i = 0; i < nutrientsArray.length; i++) {
                if(stack.is(PlateFood.SUSPICIOUS_MIX.item.get())) {
                    nutrientsArray[i] *= getSuspiciousMixFactor();
                }
            }

            FoodData data = new FoodData(
                    hunger,
                    (nutrientsArray[1] + nutrientsArray[2]) * 5f,
                    0.6f * hunger,
                    nutrientsArray[0],
                    nutrientsArray[1],
                    nutrientsArray[2],
                    nutrientsArray[3],
                    nutrientsArray[4],
                    decayModifier
            );
            return new FoodHandler(data);
        }
        return null;
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