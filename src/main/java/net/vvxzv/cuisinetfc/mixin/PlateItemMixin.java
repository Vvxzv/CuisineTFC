package net.vvxzv.cuisinetfc.mixin;

import dev.xkmc.cuisinedelight.content.block.CuisineSkilletBlockEntity;
import dev.xkmc.cuisinedelight.content.item.PlateItem;
import dev.xkmc.cuisinedelight.content.logic.CookedFoodData;
import dev.xkmc.cuisinedelight.content.logic.CookingData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.vvxzv.cuisinetfc.TfcNutritionHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PlateItem.class)
public class PlateItemMixin {
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
        if (level.isClientSide() || !(be instanceof TfcNutritionHolder holder)) return;

        // 1. 获取营养数据
        float[] nutrients = holder.getTfcNutrition();

        CompoundTag stackTag = foodStack.getOrCreateTag();
        CompoundTag CookedFoodData = stackTag.getCompound("CookedFoodData");

        CompoundTag nutrientsTag = new CompoundTag();

        nutrientsTag.putFloat("grain", nutrients[0]);
        nutrientsTag.putFloat("fruit", nutrients[1]);
        nutrientsTag.putFloat("veg", nutrients[2]);
        nutrientsTag.putFloat("meat", nutrients[3]);
        nutrientsTag.putFloat("dairy", nutrients[4]);

        stackTag.putInt("hunger", CookedFoodData.getInt("size"));
        stackTag.put("nutrients", nutrientsTag);
        CookedFoodData.putInt("size", 3);
        foodStack.setTag(stackTag);

        holder.resetTfcNutrition();
    }
}
