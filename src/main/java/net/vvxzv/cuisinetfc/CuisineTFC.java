package net.vvxzv.cuisinetfc;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.vvxzv.cuisinetfc.common.EventHandler;
import org.slf4j.Logger;

@Mod(CuisineTFC.MODID)
public class CuisineTFC {
    public static final String MODID = "cuisinetfc";
    private static final Logger LOGGER = LogUtils.getLogger();

    @SuppressWarnings("removal")
    public CuisineTFC() {
        LOGGER.info("HELLO Cuisine TFC! ");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        EventHandler.init();

    }
}
