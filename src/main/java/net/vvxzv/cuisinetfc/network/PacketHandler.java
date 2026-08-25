package net.vvxzv.cuisinetfc.network;

import net.dries007.tfc.network.DataManagerSyncPacket;
import net.dries007.tfc.util.DataManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.vvxzv.cuisinetfc.CuisineTFC;
import net.vvxzv.cuisinetfc.common.data.CookedFood;
import org.apache.commons.lang3.mutable.MutableInt;

public final class PacketHandler {
    private static final String VERSION = ModList.get().getModFileById(CuisineTFC.MODID).versionString();
    private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(CuisineTFC.MODID, "network"),
            () -> VERSION,
            VERSION::equals,
            VERSION::equals
    );
    private static final MutableInt ID = new MutableInt(0);

    public static void send(PacketDistributor.PacketTarget target, Object message) {
        CHANNEL.send(target, message);
    }

    public static void init() {
        registerDataManager(CookedFood.Packet.class, CookedFood.MANAGER);
    }

    public static <T extends DataManagerSyncPacket<E>, E> void registerDataManager(Class<T> cls, DataManager<E> manager, SimpleChannel channel, int id) {
        channel.registerMessage(id, cls, (packet, buffer) -> packet.encode(manager, buffer), (buffer) -> {
            T packet = (T)manager.createEmptyPacket();
            packet.decode(manager, buffer);
            return packet;
        }, (packet, context) -> {
            context.get().setPacketHandled(true);
            context.get().enqueueWork(() -> packet.handle(context.get(), manager));
        });
    }

    private static <T extends DataManagerSyncPacket<E>, E> void registerDataManager(Class<T> cls, DataManager<E> manager) {
        registerDataManager(cls, manager, CHANNEL, ID.getAndIncrement());
    }
}
