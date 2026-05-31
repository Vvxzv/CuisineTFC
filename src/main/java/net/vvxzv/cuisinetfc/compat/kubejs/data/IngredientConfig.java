package net.vvxzv.cuisinetfc.compat.kubejs.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import dev.xkmc.cuisinedelight.content.logic.FoodType;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public record IngredientConfig(IngredientConfigEntry[] entries) {
    public static final Codec<IngredientConfig> CODEC = RecordCodecBuilder.create(i -> i.group(
            IngredientConfigEntry.CODEC.listOf().fieldOf("entries").xmap(
                    list -> list.toArray(IngredientConfigEntry[]::new),
                    List::of
            ).forGetter(c -> c.entries)
    ).apply(i, IngredientConfig::new));

    public record IngredientConfigEntry(
            Ingredient ingredient,
            List<EffectData> effects,
            FoodType type,
            int min_time,
            int max_time,
            int stir_time,
            float raw_penalty,
            float overcook_penalty,
            int size,
            int nutrition
    ) {
        public static final Codec<FoodType> FOOD_TYPE_CODEC = Codec.STRING.xmap(FoodType::valueOf, FoodType::name);

        public static final Codec<IngredientConfigEntry> CODEC = RecordCodecBuilder.create(i -> i.group(
                Ingredient.CODEC.fieldOf("ingredient").forGetter(c -> c.ingredient),
                EffectData.CODEC.listOf().optionalFieldOf("effects", List.of()).forGetter(c -> c.effects),
                FOOD_TYPE_CODEC.fieldOf("type").forGetter(c -> c.type),
                Codec.INT.fieldOf("min_time").forGetter(c -> c.min_time),
                Codec.INT.fieldOf("max_time").forGetter(c -> c.max_time),
                Codec.INT.fieldOf("stir_time").forGetter(c -> c.stir_time),
                Codec.FLOAT.fieldOf("raw_penalty").forGetter(c -> c.raw_penalty),
                Codec.FLOAT.fieldOf("overcook_penalty").forGetter(c -> c.overcook_penalty),
                Codec.INT.fieldOf("size").forGetter(c -> c.size),
                Codec.INT.fieldOf("nutrition").forGetter(c -> c.nutrition)
        ).apply(i, IngredientConfigEntry::new));

    }

    public record EffectData(Holder<MobEffect> effect, int level, int time) {
        public static final Codec<EffectData> CODEC = RecordCodecBuilder.create(i -> i.group(
                BuiltInRegistries.MOB_EFFECT.holderByNameCodec().fieldOf("effect").forGetter(c -> c.effect),
                Codec.INT.fieldOf("level").forGetter(c -> c.level),
                Codec.INT.fieldOf("time").forGetter(c -> c.time)
        ).apply(i, EffectData::new));
    }


    public static class EntryBuilder {
        private final List<EffectData> effects;
        private final Ingredient ingredient;
        private FoodType type;
        private int minTime;
        private int maxTime;
        private int stirTime;
        private float rawPenalty;
        private float overcookPenalty;
        private int size;
        private int nutrition;

        public EntryBuilder(Ingredient ingredient) {
            this.effects = new ArrayList<>();
            this.ingredient = ingredient;
            this.type = FoodType.NONE;
            this.minTime = 0;
            this.maxTime = 300;
            this.stirTime = 60;
            this.rawPenalty = 0.5f;
            this.overcookPenalty = 0.5f;
            this.size = 1;
            this.nutrition = 1;
        }

        @HideFromJS
        public IngredientConfigEntry build() {
            return new IngredientConfigEntry(
                    ingredient,
                    effects,
                    type,
                    minTime,
                    maxTime,
                    stirTime,
                    rawPenalty,
                    overcookPenalty,
                    size,
                    nutrition
            );
        }

        public EntryBuilder type(FoodType type) {
            this.type = type;
            return this;
        }

        public EntryBuilder minTime(int minTime) {
            this.minTime = minTime;
            return this;
        }

        public EntryBuilder maxTime(int maxTime) {
            this.maxTime = maxTime;
            return this;
        }

        public EntryBuilder stirTime(int stirTime) {
            this.stirTime = stirTime;
            return this;
        }

        public EntryBuilder rawPenalty(float rawPenalty) {
            this.rawPenalty = rawPenalty;
            return this;
        }

        public EntryBuilder overcookPenalty(float overcookPenalty) {
            this.overcookPenalty = overcookPenalty;
            return this;
        }

        public EntryBuilder size(int size) {
            this.size = size;
            return this;
        }

        public EntryBuilder nutrition(int nutrition) {
            this.nutrition = nutrition;
            return this;
        }

        public EntryBuilder effects(Consumer<EffectBuilder> consumer) {
            EffectBuilder builder = new EffectBuilder(this);
            consumer.accept(builder);
            return this;
        }

        public static class EffectBuilder {
            private final EntryBuilder data;

            public EffectBuilder(EntryBuilder data) {
                this.data = data;
            }

            @Info("ResourceLocation effect, int level, int time")
            public EffectBuilder add(ResourceLocation effect, int level, int time) {
                Optional<Holder.Reference<MobEffect>> mobEffect = BuiltInRegistries.MOB_EFFECT.getHolder(effect);
                if(mobEffect.isPresent()) {
                    EffectData instance = new EffectData(
                            mobEffect.get(),
                            level,
                            time
                    );
                    data.effects.add(instance);
                }
                return this;
            }
        }
    }
}
