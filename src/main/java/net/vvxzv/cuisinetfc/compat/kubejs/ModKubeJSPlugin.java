package net.vvxzv.cuisinetfc.compat.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import net.vvxzv.cuisinetfc.compat.kubejs.data.CookedFoodData;
import net.vvxzv.cuisinetfc.compat.kubejs.data.CuisineConfigData;
import net.vvxzv.cuisinetfc.compat.kubejs.data.CuisineTransformData;

public class ModKubeJSPlugin extends KubeJSPlugin {

    @Override
    public void registerBindings(BindingsEvent event) {
        event.add("CookedFoodData", CookedFoodData.class);
        event.add("CuisineConfigData", CuisineConfigData.class);
        event.add("CuisineTransformData", CuisineTransformData.class);
    }
}
