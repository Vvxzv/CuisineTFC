package net.vvxzv.cuisinetfc.mixin;

import dev.xkmc.cuisinedelight.content.block.CuisineSkilletBlock;
import dev.xkmc.cuisinedelight.content.block.CuisineSkilletBlockEntity;
import dev.xkmc.cuisinedelight.content.logic.IngredientConfig;
import dev.xkmc.cuisinedelight.init.data.CDConfig;
import dev.xkmc.l2core.init.reg.ench.EnchHelper;
import net.dries007.tfc.common.component.food.FoodCapability;
import net.dries007.tfc.common.component.food.IFood;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
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
public class CuisineSkilletBlockMixin extends SkilletBlock {

    public CuisineSkilletBlockMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "useItemOn", at = @At("RETURN"), cancellable = true)
    public void onAddFoodToSkillet(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<ItemInteractionResult> cir) {
        ItemStack heldStack = player.getItemInHand(hand);
        IngredientConfig.IngredientEntry config = IngredientConfig.get().getEntry(heldStack);
        if(config == null){
            cir.setReturnValue(ItemInteractionResult.SUCCESS);
            return;
        }

        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof CuisineSkilletBlockEntity skillet) {
            IFood tfcFood = FoodCapability.get(heldStack);
            if(tfcFood != null){
                float[] nutrients = tfcFood.getData().nutrients();
                if(heldStack.is(Items.EGG)){
                    nutrients[3] = 1.5f;
                    nutrients[4] = 0.3f;
                }
                int allowInputCount = 1 + EnchHelper.getLv(skillet.baseItem, Enchantments.EFFICIENCY);
                int stackCount = heldStack.getCount();
                int count = Math.min(stackCount, allowInputCount);
                if(skillet.cookingData.contents.size() >= (Integer)CDConfig.SERVER.maxIngredient.get()){
                    cir.setReturnValue(ItemInteractionResult.FAIL);
                    return;
                }
                if (skillet instanceof TFCNutrientsHolder holder) {
                    for (int i = 0; i < count; i++){
                        holder.addTFCNutrients(nutrients);
                    }
                    holder.addRottenFood(tfcFood.isRotten());
                }
            }
        }
    }
}
