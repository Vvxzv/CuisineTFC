package net.vvxzv.cuisinetfc.common.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.dries007.tfc.common.component.food.FoodData;
import net.dries007.tfc.common.component.food.Nutrient;
import net.dries007.tfc.common.recipes.RecipeHelpers;
import net.dries007.tfc.util.collections.IndirectHashCollection;
import net.dries007.tfc.util.data.DataManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.vvxzv.cuisinetfc.CuisineTFC;
import org.jetbrains.annotations.Nullable;

public record CookedFood(Ingredient ingredient, float[] nutrients) {
    public static final Codec<CookedFood> CODEC = RecordCodecBuilder.create(i -> i.group(
            Ingredient.CODEC.fieldOf("ingredient").forGetter(c -> c.ingredient),
            FoodData.NUTRITION_CODEC.forGetter(c -> c.nutrients)
    ).apply(i, CookedFood::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, CookedFood> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, c -> c.ingredient,
            FoodData.NUTRITION_STREAM_CODEC, c -> c.nutrients,
            CookedFood::new
    );

    public static final DataManager<CookedFood> MANAGER = new DataManager<>(
            ResourceLocation.fromNamespaceAndPath(CuisineTFC.MODID, "cooked_food"),
            CODEC,
            STREAM_CODEC
    );

    public static final IndirectHashCollection<Item, CookedFood> CACHE = IndirectHashCollection.create(
            c -> RecipeHelpers.itemKeys(c.ingredient),
            MANAGER::getValues
    );

    public static @Nullable CookedFood get(ItemStack stack) {
        for (CookedFood cookedFood: CACHE.getAll(stack.getItem())) {
            if(cookedFood.ingredient.test(stack)) {
                return cookedFood;
            }
        }
        return null;
    }

    public float[] getNutrients() {
        return this.nutrients.clone();
    }

    public float nutrient(Nutrient nutrient) {
        float[] nutrients = this.getNutrients();
        return switch (nutrient) {
            case GRAIN -> nutrients[0];
            case FRUIT -> nutrients[1];
            case VEGETABLES -> nutrients[2];
            case PROTEIN -> nutrients[3];
            case DAIRY -> nutrients[4];
        };
    }
}
