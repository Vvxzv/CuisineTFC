package net.vvxzv.cuisinetfc;

import dev.xkmc.cuisinedelight.content.block.CuisineSkilletBlock;
import dev.xkmc.cuisinedelight.content.item.BaseFoodItem;
import dev.xkmc.cuisinedelight.content.logic.CookedFoodData;
import dev.xkmc.cuisinedelight.content.logic.IngredientConfig;
import net.dries007.tfc.common.capabilities.food.*;
import net.dries007.tfc.util.Helpers;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.PacketDistributor;
import net.vvxzv.cuisinetfc.common.data.CookedFood;
import net.vvxzv.cuisinetfc.network.PacketHandler;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ForgeEventHandler {

    public static void init(){
        IEventBus bus = MinecraftForge.EVENT_BUS;
        bus.addListener(ForgeEventHandler::addReloadListeners);
        bus.addListener(ForgeEventHandler::onDataPackSync);
        bus.addListener(ForgeEventHandler::cantCookRottenFood);
        bus.addListener(ForgeEventHandler::onTooltip);
        bus.addGenericListener(ItemStack.class, ForgeEventHandler::attachItemCapabilities);
    }

    public static void addReloadListeners(AddReloadListenerEvent event) {
        event.addListener(CookedFood.MANAGER);
    }

    public static void onDataPackSync(OnDatapackSyncEvent event) {
        ServerPlayer player = event.getPlayer();
        PacketDistributor.PacketTarget target = player == null ? PacketDistributor.ALL.noArg() : PacketDistributor.PLAYER.with(() -> player);
        PacketHandler.send(target, CookedFood.MANAGER.createSyncPacket());
    }

    public static void cantCookRottenFood(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        Player player = event.getEntity();
        InteractionHand hand = event.getHand();
        ItemStack stack = player.getItemInHand(hand);
        IFood food = FoodCapability.get(stack);
        if(food != null && food.isRotten()) {
            BlockState state = level.getBlockState(event.getPos());
            if(state.getBlock() instanceof CuisineSkilletBlock) {
                event.setCanceled(true);
            }
        }
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
            CompoundTag tag = stack.getOrCreateTag();
            CompoundTag foodData = tag.getCompound("foodData");
            FoodData data = FoodData.read(foodData);
            return new FoodHandler(data);
        }

        return null;
    }

    public static void onTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.isEmpty()) return;

        IngredientConfig.IngredientEntry config = IngredientConfig.get().getEntry(stack);
        if (config == null) return;

        if (!Screen.hasShiftDown()) return;

        IFood food = FoodCapability.get(stack);
        if(food != null && food.isRotten()) {
            return;
        }

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
            if (food != null && !food.isRotten()) {
                for (Nutrient nutrient : Nutrient.VALUES) {
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