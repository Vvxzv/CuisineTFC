package net.vvxzv.cuisinetfc.mixin;

import dev.xkmc.cuisinedelight.content.block.CuisineSkilletBlockEntity;
import dev.xkmc.l2library.base.tile.BaseBlockEntity;
import net.dries007.tfc.common.capabilities.food.Nutrient;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.vvxzv.cuisinetfc.TfcNutritionHolder;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Arrays;

@Mixin(CuisineSkilletBlockEntity.class)
public abstract class CuisineSkilletBlockEntityMixin extends BaseBlockEntity implements TfcNutritionHolder {
    private final float[] tfcNutrients = new float[Nutrient.TOTAL];

    public CuisineSkilletBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addTfcNutrition(float[] nutrients) {
        for (int i = 0; i < Math.min(this.tfcNutrients.length, nutrients.length); i++) {
            this.tfcNutrients[i] += nutrients[i];
        }
    }

    @Override
    public float[] getTfcNutrition() {
        return Arrays.copyOf(tfcNutrients, tfcNutrients.length);
    }

    @Override
    public void resetTfcNutrition() {
        Arrays.fill(tfcNutrients, 0);
    }
}