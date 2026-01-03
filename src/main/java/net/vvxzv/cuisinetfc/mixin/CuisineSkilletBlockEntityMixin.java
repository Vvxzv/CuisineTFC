package net.vvxzv.cuisinetfc.mixin;

import dev.xkmc.cuisinedelight.content.block.CuisineSkilletBlockEntity;
import net.dries007.tfc.common.capabilities.food.Nutrient;
import net.vvxzv.cuisinetfc.common.TFCNutrientsHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Arrays;

@Mixin(CuisineSkilletBlockEntity.class)
public abstract class CuisineSkilletBlockEntityMixin implements TFCNutrientsHolder {
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