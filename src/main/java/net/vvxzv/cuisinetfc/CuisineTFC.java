package net.vvxzv.cuisinetfc;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.vvxzv.cuisinetfc.network.PacketHandler;

@Mod(CuisineTFC.MODID)
public class CuisineTFC {
    public static final String MODID = "cuisinetfc";

    @SuppressWarnings("removal")
    public CuisineTFC() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        ForgeEventHandler.init();
        PacketHandler.init();
    }
}
