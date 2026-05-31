package net.vvxzv.cuisinetfc.common.data;

import com.google.gson.JsonObject;
import net.dries007.tfc.common.capabilities.food.Nutrient;
import net.dries007.tfc.network.DataManagerSyncPacket;
import net.dries007.tfc.util.DataManager;
import net.dries007.tfc.util.ItemDefinition;
import net.dries007.tfc.util.JsonHelpers;
import net.dries007.tfc.util.collections.IndirectHashCollection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.vvxzv.cuisinetfc.CuisineTFC;
import org.jetbrains.annotations.Nullable;

public class CookedFood extends ItemDefinition {
    public static final DataManager<CookedFood> MANAGER = new DataManager<>(
            ResourceLocation.fromNamespaceAndPath(CuisineTFC.MODID, "cooked_food"),
            "cooked_food", CookedFood::new, CookedFood::new, CookedFood::encode, Packet::new
    );

    private final float[] nutrients;

    public CookedFood(ResourceLocation id, JsonObject json) {
        super(id, Ingredient.fromJson(JsonHelpers.get(json, "ingredient")));
        this.nutrients = new float[]{0f, 0f, 0f, 0f, 0f};
        this.nutrients[0] = JsonHelpers.getAsFloat(json, "grain", 0);
        this.nutrients[1] = JsonHelpers.getAsFloat(json, "fruit", 0);
        this.nutrients[2] = JsonHelpers.getAsFloat(json, "vegetables", 0);
        this.nutrients[3] = JsonHelpers.getAsFloat(json, "protein", 0);
        this.nutrients[4] = JsonHelpers.getAsFloat(json, "dairy", 0);
    }

    public CookedFood(ResourceLocation id, FriendlyByteBuf buffer) {
        super(id, Ingredient.fromNetwork(buffer));
        this.nutrients = new float[]{0f, 0f, 0f, 0f, 0f};
        this.nutrients[0] = buffer.readFloat();
        this.nutrients[1] = buffer.readFloat();
        this.nutrients[2] = buffer.readFloat();
        this.nutrients[3] = buffer.readFloat();
        this.nutrients[4] = buffer.readFloat();
    }

    public void encode(FriendlyByteBuf buffer) {
        this.ingredient.toNetwork(buffer);
        buffer.writeFloat(this.nutrients[0]);
        buffer.writeFloat(this.nutrients[1]);
        buffer.writeFloat(this.nutrients[2]);
        buffer.writeFloat(this.nutrients[3]);
        buffer.writeFloat(this.nutrients[4]);
    }

    public Ingredient getIngredient() {
        return this.ingredient;
    }

    public static final IndirectHashCollection<Item, CookedFood> CACHE = IndirectHashCollection.create(
            ItemDefinition::getValidItems,
            MANAGER::getValues
    );

    public static @Nullable CookedFood get(ItemStack stack) {
        for (CookedFood cookedFood: CACHE.getAll(stack.getItem())) {
            if(cookedFood.matches(stack)){
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

    public static class Packet extends DataManagerSyncPacket<CookedFood> {
    }
}
