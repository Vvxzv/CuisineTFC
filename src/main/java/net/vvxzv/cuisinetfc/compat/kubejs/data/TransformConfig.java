package net.vvxzv.cuisinetfc.compat.kubejs.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.cuisinedelight.content.logic.transform.Stage;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public record TransformConfig(
        Map<ResourceLocation, FluidTransformData> fluidTransform,
        Map<ResourceLocation, ItemTransformData> itemTransform
) {
    public static final Codec<Stage> SATGE_CODEC = Codec.STRING.xmap(Stage::valueOf, Stage::name);

    public static final Codec<TransformConfig> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.unboundedMap(ResourceLocation.CODEC, FluidTransformData.CODEC)
                    .optionalFieldOf("fluidTransform", Map.of())
                    .forGetter(c -> c.fluidTransform),
            Codec.unboundedMap(ResourceLocation.CODEC, ItemTransformData.CODEC)
                    .optionalFieldOf("itemTransform", Map.of())
                    .forGetter(c -> c.itemTransform)
    ).apply(i, TransformConfig::new));

    public record FluidTransformData(int color) {
        public static final Codec<FluidTransformData> CODEC = RecordCodecBuilder.create(i -> i.group(
                Codec.INT.fieldOf("color").forGetter(c -> c.color)
        ).apply(i, FluidTransformData::new));
    }

    public record ItemTransformData(ResourceLocation next, Stage stage) {
        public static final Codec<ItemTransformData> CODEC = RecordCodecBuilder.create(i -> i.group(
                ResourceLocation.CODEC.fieldOf("next").forGetter(c -> c.next),
                SATGE_CODEC.fieldOf("stage").forGetter(c -> c.stage)
        ).apply(i, ItemTransformData::new));
    }
}