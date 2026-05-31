package net.vvxzv.cuisinetfc.compat.kubejs.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import dev.xkmc.cuisinedelight.content.logic.FoodType;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CuisineConfigData {
    private final Ingredient ingredient;
    private final List<MobEffectInstance> effects;
    private FoodType type;
    private int minTime;
    private int maxTime;
    private int stirTime;
    private float rawPenalty;
    private float overcookPenalty;
    private int size;
    private int nutrition;

    public CuisineConfigData(Ingredient ingredient) {
        this.ingredient = ingredient;
        this.type = FoodType.NONE;
        this.minTime = 0;
        this.maxTime = 300;
        this.stirTime = 60;
        this.rawPenalty = this.overcookPenalty = 0.5f;
        this.size = 1;
        this.nutrition = 1;
        this.effects = new ArrayList<>();
    }

    public static JsonObject create(CuisineConfigData... datas) {
        JsonObject object = new JsonObject();
        JsonArray array = new JsonArray();
        for (CuisineConfigData data: datas) {
            array.add(data.toJson());
        }
        object.add("entries", array);
        return object;
    }

    public static CuisineConfigData data(Ingredient ingredient, Consumer<CuisineConfigData> consumer) {
        return Util.make(new CuisineConfigData(ingredient), consumer);
    }

    public CuisineConfigData type(FoodType type) {
        this.type = type;
        return this;
    }

    public CuisineConfigData minTime(int minTime) {
        this.minTime = minTime;
        return this;
    }

    public CuisineConfigData maxTime(int maxTime) {
        this.maxTime = maxTime;
        return this;
    }

    public CuisineConfigData stirTime(int stirTime) {
        this.stirTime = stirTime;
        return this;
    }

    public CuisineConfigData rawPenalty(float rawPenalty) {
        this.rawPenalty = rawPenalty;
        return this;
    }

    public CuisineConfigData overcookPenalty(float overcookPenalty) {
        this.overcookPenalty = overcookPenalty;
        return this;
    }

    public CuisineConfigData size(int size) {
        this.size = size;
        return this;
    }

    public CuisineConfigData nutrition(int nutrition) {
        this.nutrition = nutrition;
        return this;
    }

    public CuisineConfigData effects(Consumer<EffectBuilder> consumer) {
        EffectBuilder builder = new EffectBuilder(this);
        consumer.accept(builder);
        return this;
    }

    private JsonArray getEffects() {
        JsonArray array = new JsonArray();
        for (MobEffectInstance mobEffectInstance: this.effects) {
            JsonObject jsonObject = new JsonObject();
            ResourceLocation effectId = ForgeRegistries.MOB_EFFECTS.getKey(mobEffectInstance.getEffect());
            if(effectId != null) {
                jsonObject.addProperty("effect", effectId.toString());
                jsonObject.addProperty("level", mobEffectInstance.getAmplifier());
                jsonObject.addProperty("time", mobEffectInstance.getDuration());
            }
            array.add(jsonObject);
        }
        return array;
    }

    @HideFromJS
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type", this.type.name());
        json.add("effects", this.getEffects());
        json.add("ingredient", this.ingredient.toJson());
        json.addProperty("min_time", this.minTime);
        json.addProperty("max_time", this.maxTime);
        json.addProperty("stir_time", this.stirTime);
        json.addProperty("raw_penalty", this.rawPenalty);
        json.addProperty("overcook_penalty", this.overcookPenalty);
        json.addProperty("size", this.size);
        json.addProperty("nutrition", this.nutrition);
        return json;
    }

    public static class EffectBuilder {
        private final CuisineConfigData data;

        public EffectBuilder(CuisineConfigData data) {
            this.data = data;
        }

        @Info("ResourceLocation effect, int level, int time")
        public EffectBuilder add(ResourceLocation effect, int level, int time) {
            MobEffect mobEffect = ForgeRegistries.MOB_EFFECTS.getValue(effect);
            if(mobEffect != null) {
                MobEffectInstance instance = new MobEffectInstance(
                        mobEffect,
                        time,
                        level
                );
                data.effects.add(instance);
            }
            return this;
        }
    }
}
