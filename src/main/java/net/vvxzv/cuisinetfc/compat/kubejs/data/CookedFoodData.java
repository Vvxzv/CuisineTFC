package net.vvxzv.cuisinetfc.compat.kubejs.data;

import com.google.gson.JsonObject;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.Util;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Consumer;

public class CookedFoodData {
    private final Ingredient ingredient;
    private float grain;
    private float fruit;
    private float vegetables;
    private float protein;
    private float dairy;

    public static JsonObject create(Ingredient ingredient, Consumer<CookedFoodData> consumer) {
        return Util.make(new CookedFoodData(ingredient), consumer).toJson();
    }

    public CookedFoodData(Ingredient ingredient) {
        this.ingredient = ingredient;
        this.grain = 0;
        this.fruit = 0;
        this.vegetables = 0;
        this.protein = 0;
        this.dairy = 0;
    }

    public CookedFoodData grain(float n) {
        this.grain = n;
        return this;
    }

    public CookedFoodData fruit(float n) {
        this.fruit = n;
        return this;
    }

    public CookedFoodData vegetables(float n) {
        this.vegetables = n;
        return this;
    }

    public CookedFoodData protein(float n) {
        this.protein = n;
        return this;
    }

    public CookedFoodData dairy(float n) {
        this.dairy = n;
        return this;
    }

    public CookedFoodData nutrients(float grain, float fruit, float vegetables, float protein, float dairy) {
        return this.grain(grain).fruit(fruit).vegetables(vegetables).protein(protein).dairy(dairy);
    }

    @HideFromJS
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.add("ingredient", this.ingredient.toJson());

        if (this.grain > 0) {
            json.addProperty("grain", this.grain);
        }

        if (this.fruit > 0) {
            json.addProperty("fruit", this.fruit);
        }

        if (this.vegetables > 0) {
            json.addProperty("vegetables", this.vegetables);
        }

        if (this.protein > 0) {
            json.addProperty("protein", this.protein);
        }

        if (this.dairy > 0) {
            json.addProperty("dairy", this.dairy);
        }

        return json;
    }
}
