package net.vvxzv.cuisinetfc.mixin;

import dev.xkmc.cuisinedelight.content.block.CuisineSkilletBlockEntity;
import dev.xkmc.cuisinedelight.content.item.PlateItem;
import dev.xkmc.cuisinedelight.content.logic.CookedFoodData;
import dev.xkmc.cuisinedelight.content.logic.CookingData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.vvxzv.cuisinetfc.Config;
import net.vvxzv.cuisinetfc.common.TFCNutrientsHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PlateItem.class)
public class PlateItemMixin {
    private static int getFoodSize() {
        return Config.foodSize;
    }

    private static float getPunishFactor(){
        return (float) Config.usedRottenFood;
    }

    @Inject(
            method = "useOn",
            at = @At(
                    value = "INVOKE",
                    target = "Ldev/xkmc/cuisinedelight/content/item/PlateItem;" +
                            "giveBack(Lnet/minecraft/world/item/ItemStack;" +
                            "Ldev/xkmc/cuisinedelight/content/logic/CookedFoodData;" +
                            "Ldev/xkmc/cuisinedelight/content/item/PlateItem$ReturnTarget;)V",
                    shift = At.Shift.BEFORE,
                    remap = false
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void onPlateUseOnSkillet(
            UseOnContext ctx,
            CallbackInfoReturnable<InteractionResult> cir,
            Level level,
            Player player,
            CuisineSkilletBlockEntity be,
            CookingData data,
            CookedFoodData food,
            ItemStack foodStack
    ) {
        if (level.isClientSide() || !(be instanceof TFCNutrientsHolder holder)) return;

        float[] nutrients = holder.getTFCNutrients();

        CompoundTag stackTag = foodStack.getOrCreateTag();
        CompoundTag CookedFoodData = stackTag.getCompound("CookedFoodData");

        int hunger = CookedFoodData.getInt("size");

        if(holder.hasRottenFood()){
            for(int i = 0; i < nutrients.length; i++){
                nutrients[i] = nutrients[i] * getPunishFactor();
            }
            hunger = Mth.floor(hunger * getPunishFactor() * 3);
            if (hunger == 0) hunger = 1;
            stackTag.putBoolean("decay", true);
        }

        CompoundTag nutrientsTag = new CompoundTag();

        nutrientsTag.putFloat("grain", nutrients[0]);
        nutrientsTag.putFloat("fruit", nutrients[1]);
        nutrientsTag.putFloat("veg", nutrients[2]);
        nutrientsTag.putFloat("meat", nutrients[3]);
        nutrientsTag.putFloat("dairy", nutrients[4]);

        stackTag.putInt("hunger", hunger);
        stackTag.put("nutrients", nutrientsTag);
        CookedFoodData.putInt("size", getFoodSize());
        foodStack.setTag(stackTag);

        holder.reset();
    }
}
