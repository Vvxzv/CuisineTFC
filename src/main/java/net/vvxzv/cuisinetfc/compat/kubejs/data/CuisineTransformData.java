package net.vvxzv.cuisinetfc.compat.kubejs.data;

import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import dev.xkmc.cuisinedelight.content.logic.transform.Stage;

import java.util.function.Consumer;

public class CuisineTransformData {
    private final JsonObject itemTransformJson;
    private final JsonObject fluidTransformJson;

    public CuisineTransformData() {
        this.itemTransformJson = new JsonObject();
        this.fluidTransformJson = new JsonObject();
    }

    public static JsonObject create(Consumer<CuisineTransformData> consumer) {
        CuisineTransformData data = new CuisineTransformData();
        consumer.accept(data);
        return data.toJson();
    }

    @Info("String itemId, String nextId, Stage stage")
    public CuisineTransformData itemTransform(String itemId, String nextId, Stage stage) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("next", nextId);
        jsonObject.addProperty("stage", stage.name());
        this.itemTransformJson.add(itemId, jsonObject);
        return this;
    }

    @Info("String itemId, int color")
    public CuisineTransformData fluidTransform(String itemId, int color) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("color", color);
        this.fluidTransformJson.add(itemId, jsonObject);
        return this;
    }


    @HideFromJS
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.add("fluidTransform", this.fluidTransformJson);
        json.add("itemTransform", this.itemTransformJson);
        return json;
    }
}
