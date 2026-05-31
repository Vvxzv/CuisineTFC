package net.vvxzv.cuisinetfc;

import dev.xkmc.cuisinedelight.content.logic.IngredientConfig;
import net.dries007.tfc.common.component.food.FoodCapability;
import net.dries007.tfc.common.component.food.IFood;
import net.dries007.tfc.common.component.food.Nutrient;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.data.DataManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.vvxzv.cuisinetfc.common.data.CookedFood;
import net.vvxzv.cuisinetfc.common.data.DataManagers;

import java.util.Arrays;
import java.util.List;

public class NeoForgeEventHandler {
    public static void init() {
        IEventBus bus = NeoForge.EVENT_BUS;
        bus.addListener(NeoForgeEventHandler::addReloadListeners);
        bus.addListener(NeoForgeEventHandler::onTooltip);
    }

    public static void addReloadListeners(AddReloadListenerEvent event) {
        Registry<DataManager<?>> managers = DataManagers.REGISTRY;
        managers.forEach(event::addListener);
    }

    public static void onTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.isEmpty()) return;

        IngredientConfig.IngredientEntry config = IngredientConfig.get().getEntry(stack);
        if (config == null) return;

        if (!Screen.hasShiftDown()) return;

        List<Component> tooltip = event.getToolTip();
        CookedFood cookedFood = CookedFood.get(stack);

        tooltip.add(Component.translatable("cuisinetfc.tooltip.nutrition").withStyle(ChatFormatting.GRAY));

        if (cookedFood != null && !Arrays.equals(cookedFood.getNutrients(), new float[]{0, 0, 0, 0, 0})) {
            for (Nutrient nutrient : Nutrient.VALUES) {
                float value = cookedFood.nutrient(nutrient);
                if (value > 0) {
                    tooltip.add(Component.literal(" - ")
                            .append(Helpers.translateEnum(nutrient))
                            .append(": " + String.format("%.1f", value))
                            .withStyle(nutrient.getColor()));
                }
            }
        } else {
            tooltip.add(Component.translatable("cuisinetfc.tooltip.nutrition.default").withStyle(ChatFormatting.GRAY));

            boolean any = false;
            IFood food = FoodCapability.get(stack);
            if(food != null && !food.isRotten()) {
                for(Nutrient nutrient : Nutrient.VALUES) {
                    float value = food.getData().nutrient(nutrient);
                    if (value > 0) {
                        tooltip.add(Component.literal(" - ")
                                .append(Helpers.translateEnum(nutrient))
                                .append(": " + String.format("%.1f", value))
                                .withStyle(nutrient.getColor()));
                        any = true;
                    }
                }
            }

            if(!any) {
                tooltip.add(Component.translatable("tfc.tooltip.nutrition_none").withStyle(ChatFormatting.GRAY));
            }
        }
    }
}
