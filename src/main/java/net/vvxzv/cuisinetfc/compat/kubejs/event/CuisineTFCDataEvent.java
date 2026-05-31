package net.vvxzv.cuisinetfc.compat.kubejs.event;

import dev.latvian.mods.kubejs.generator.KubeResourceGenerator;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.util.KubeResourceLocation;
import dev.latvian.mods.rhino.util.RemapForJS;
import dev.xkmc.cuisinedelight.content.logic.transform.Stage;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.vvxzv.cuisinetfc.common.data.CookedFood;
import net.vvxzv.cuisinetfc.compat.kubejs.data.IngredientConfig;
import net.vvxzv.cuisinetfc.compat.kubejs.data.TransformConfig;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class CuisineTFCDataEvent extends KubeJSDataEvent {
    public CuisineTFCDataEvent(KubeResourceGenerator gen) {
        super(gen);
    }

    @Info("cookedFood(Ingredient ingredient, float[] nutrients, @Nullable KubeResourceLocation id)")
    public void cookedFood(Ingredient ingredient, float[] nutrients, @Nullable KubeResourceLocation id) {
        this.cookedFood(new CookedFood(ingredient, nutrients), id);
    }

    @Info("cookedFood(Ingredient ingredient, float[] nutrients)")
    public void cookedFood(Ingredient ingredient, float[] nutrients) {
        this.cookedFood(ingredient, nutrients, null);
    }

    @RemapForJS("cookedFoodJson")
    @Info("cookedFood(CookedFood cookedFood, @Nullable KubeResourceLocation id)")
    public void cookedFood(CookedFood cookedFood, @Nullable KubeResourceLocation id) {
        this.add(cookedFood, CookedFood.CODEC, id, "cuisinetfc/cooked_food");
    }

    @RemapForJS("cookedFoodJson")
    @Info("cookedFood(CookedFood cookedFood)")
    public void cookedFood(CookedFood cookedFood) {
        this.cookedFood(cookedFood, null);
    }

    @Info("ingredientConfig(Ingredient ingredient, Consumer<IngredientConfig.EntryBuilder> entry, @Nullable KubeResourceLocation id)")
    public void ingredientConfig(Ingredient ingredient, Consumer<IngredientConfig.EntryBuilder> entry, @Nullable KubeResourceLocation id) {
        IngredientConfig.IngredientConfigEntry configEntry = Util.make(new IngredientConfig.EntryBuilder(ingredient), entry).build();
        List<IngredientConfig.IngredientConfigEntry> entryList = new ArrayList<>();
        entryList.add(configEntry);
        this.ingredientConfig(new IngredientConfig(entryList.toArray(IngredientConfig.IngredientConfigEntry[]::new)), id);
    }

    @Info("ingredientConfig(Ingredient ingredient, Consumer<IngredientConfig.EntryBuilder> entry)")
    public void ingredientConfig(Ingredient ingredient, Consumer<IngredientConfig.EntryBuilder> entry) {
        this.ingredientConfig(ingredient, entry, null);
    }

    @RemapForJS("ingredientConfigJson")
    @Info("ingredientConfig(IngredientConfig config, @Nullable KubeResourceLocation id)")
    public void ingredientConfig(IngredientConfig config, @Nullable KubeResourceLocation id) {
        this.add(config, IngredientConfig.CODEC, id, "cuisinedelight_config/ingredient");
    }

    @RemapForJS("ingredientConfigJson")
    @Info("ingredientConfig(IngredientConfig config)")
    public void ingredientConfig(IngredientConfig config) {
        this.ingredientConfig(config, null);
    }

    @Info("fluidTransform(ResourceLocation itemId, int color, @Nullable KubeResourceLocation id)")
    public void fluidTransform(ResourceLocation itemId, int color, @Nullable KubeResourceLocation id) {
        Map<ResourceLocation, TransformConfig.FluidTransformData> map = new HashMap<>();
        map.put(itemId, new TransformConfig.FluidTransformData(color));
        this.transform(new TransformConfig(map, new HashMap<>()), id);
    }

    @Info("fluidTransform(ResourceLocation itemId, int color)")
    public void fluidTransform(ResourceLocation itemId, int color) {
        this.fluidTransform(itemId, color, null);
    }

    @Info("itemTransform(ResourceLocation itemId, ResourceLocation next, Stage stage, @Nullable KubeResourceLocation id)")
    public void itemTransform(ResourceLocation itemId, ResourceLocation next, Stage stage, @Nullable KubeResourceLocation id) {
        Map<ResourceLocation, TransformConfig.ItemTransformData> map = new HashMap<>();
        map.put(itemId, new TransformConfig.ItemTransformData(next, stage));
        this.transform(new TransformConfig(new HashMap<>(), map), id);
    }

    @Info("itemTransform(ResourceLocation itemId, ResourceLocation next, Stage stage)")
    public void itemTransform(ResourceLocation itemId, ResourceLocation next, Stage stage) {
        this.itemTransform(itemId, next, stage, null);
    }

    @RemapForJS("transformJson")
    @Info("transform(TransformConfig config, @Nullable KubeResourceLocation id)")
    public void transform(TransformConfig config, @Nullable KubeResourceLocation id) {
        this.add(config, TransformConfig.CODEC, id, "cuisinedelight_config/transform");
    }

    @RemapForJS("transformJson")
    @Info("transform(TransformConfig config)")
    public void transform(TransformConfig config) {
        this.transform(config, null);
    }
}
