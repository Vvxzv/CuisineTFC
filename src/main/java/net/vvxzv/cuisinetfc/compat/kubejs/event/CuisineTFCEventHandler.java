package net.vvxzv.cuisinetfc.compat.kubejs.event;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

public class CuisineTFCEventHandler {
    public static final EventGroup eventHandler = EventGroup.of("CuisineTFCEvent");
    public static final EventHandler data = eventHandler.server("data", () -> CuisineTFCDataEvent.class);
}
