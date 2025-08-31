package net.vvxzv.cuisinetfc.mixin;

import dev.xkmc.cuisinedelight.content.block.CuisineSkilletBlock;
import dev.xkmc.cuisinedelight.content.block.CuisineSkilletBlockEntity;
import dev.xkmc.cuisinedelight.content.logic.IngredientConfig;
import dev.xkmc.cuisinedelight.init.data.CDConfig;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.vvxzv.cuisinetfc.common.TFCNutrientsHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.block.SkilletBlock;

@Mixin(CuisineSkilletBlock.class)
public abstract class CuisineSkilletBlockMixin extends SkilletBlock {

    public CuisineSkilletBlockMixin(Properties properties) {
        super(properties);
    }

    @Inject(
            method = "use",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;getItemInHand" +
                            "(Lnet/minecraft/world/InteractionHand;)" +
                            "Lnet/minecraft/world/item/ItemStack;",
                    shift = At.Shift.AFTER
            )
    )
    public void onAddFoodToSkillet(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hit,
            CallbackInfoReturnable<InteractionResult> cir
    ) {
        ItemStack heldStack = player.getItemInHand(hand);
        IngredientConfig.IngredientEntry config = IngredientConfig.get().getEntry(heldStack);
        if(config == null) return;

        BlockEntity be = level.getBlockEntity(pos);

        if (be instanceof CuisineSkilletBlockEntity skillet) {
            heldStack.getCapability(FoodCapability.CAPABILITY).ifPresent(tfcFood -> {
                float[] nutrients = tfcFood.getData().nutrients();
                if(heldStack.is(Items.EGG)){
                    nutrients[3] = 1.5f;
                    nutrients[4] = 0.3f;
                }
                int allowInputCount = 1 + ((CuisineSkilletBlockEntity) be).baseItem.getEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY);
                int stackCount = heldStack.getCount();
                int count = Math.min(stackCount, allowInputCount);
                if(((CuisineSkilletBlockEntity) be).cookingData.contents.size() >= (Integer) CDConfig.COMMON.maxIngredient.get()) return;
                if (skillet instanceof TFCNutrientsHolder holder) {
                    for (int i = 0; i < count; i++){
                        holder.addTFCNutrients(nutrients);
                    }
                    holder.addRottenFood(tfcFood.isRotten());
                }
            });
        }
    }
}