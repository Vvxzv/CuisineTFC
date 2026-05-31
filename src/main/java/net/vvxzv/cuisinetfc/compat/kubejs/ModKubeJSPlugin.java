package net.vvxzv.cuisinetfc.compat.kubejs;

import dev.latvian.mods.kubejs.event.EventGroupRegistry;
import dev.latvian.mods.kubejs.generator.KubeDataGenerator;
import dev.latvian.mods.kubejs.plugin.ClassFilter;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import net.vvxzv.cuisinetfc.CuisineTFC;
import net.vvxzv.cuisinetfc.NeoForgeEventHandler;
import net.vvxzv.cuisinetfc.compat.kubejs.event.CuisineTFCDataEvent;
import net.vvxzv.cuisinetfc.compat.kubejs.event.CuisineTFCEventHandler;

public class ModKubeJSPlugin implements KubeJSPlugin {

    @Override
    public void registerClasses(ClassFilter filter) {
        filter.deny(CuisineTFCEventHandler.class);
        filter.deny(CuisineTFC.class.getPackageName() + ".mixin");
        filter.deny(NeoForgeEventHandler.class);
    }

    @Override
    public void registerEvents(EventGroupRegistry registry) {
        registry.register(CuisineTFCEventHandler.eventHandler);
    }

    @Override
    public void generateData(KubeDataGenerator generator) {
        if (CuisineTFCEventHandler.data.hasListeners()) {
            CuisineTFCEventHandler.data.post(new CuisineTFCDataEvent(generator));
        }
    }
}
