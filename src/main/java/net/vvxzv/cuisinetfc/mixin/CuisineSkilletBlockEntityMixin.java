package net.vvxzv.cuisinetfc.mixin;

import dev.xkmc.cuisinedelight.content.block.CuisineSkilletBlockEntity;
import dev.xkmc.l2core.base.tile.BaseBlockEntity;
import net.dries007.tfc.common.component.food.Nutrient;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.vvxzv.cuisinetfc.common.TFCNutrientsHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Arrays;

@Mixin(CuisineSkilletBlockEntity.class)
public class CuisineSkilletBlockEntityMixin implements TFCNutrientsHolder {
    @Unique
    private final float[] tfcNutrients = new float[Nutrient.TOTAL];
    @Unique
    private boolean hasRottenFood = false;

    @Override
    public void addTFCNutrients(float[] nutrients) {
        for (int i = 0; i < Math.min(this.tfcNutrients.length, nutrients.length); i++) {
            this.tfcNutrients[i] += nutrients[i];
        }
    }

    @Override
    public float[] getTFCNutrients() {
        return Arrays.copyOf(tfcNutrients, tfcNutrients.length);
    }

    @Override
    public void reset() {
        Arrays.fill(tfcNutrients, 0);
        hasRottenFood = false;
    }

    @Override
    public void addRottenFood(boolean rotten) {
        hasRottenFood = hasRottenFood || rotten;
    }

    public boolean hasRottenFood(){
        return hasRottenFood;
    }
}
