package net.vvxzv.cuisinetfc;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.vvxzv.cuisinetfc.common.data.DataManagers;
import net.vvxzv.cuisinetfc.network.PacketHandler;

@Mod(CuisineTFC.MODID)
public class CuisineTFC {
    public static final String MODID = "cuisinetfc";

    public CuisineTFC(IEventBus modEventBus, ModContainer modContainer) {
        DataManagers.MANAGERS.register(modEventBus);
        modEventBus.addListener(this::registerRegistries);
        modEventBus.addListener(PacketHandler::setup);

        NeoForgeEventHandler.init();

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    public void registerRegistries(NewRegistryEvent event) {
        event.register(DataManagers.REGISTRY);
    }
}
