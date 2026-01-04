package net.vvxzv.cuisinetfc;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
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
