package net.vvxzv.cuisinetfc.common.data;

import net.dries007.tfc.util.data.DataManager;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;
import net.vvxzv.cuisinetfc.CuisineTFC;

public class DataManagers {
    public static final ResourceKey<Registry<DataManager<?>>> KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(CuisineTFC.MODID, "data_manager"));

    public static final Registry<DataManager<?>> REGISTRY = new RegistryBuilder<>(KEY).sync(true).create();
    public static final DeferredRegister<DataManager<?>> MANAGERS = DeferredRegister.create(KEY, CuisineTFC.MODID);

    static {
        MANAGERS.register("cooked_food", () -> CookedFood.MANAGER);
    }
}
