package cursedflames.modifiers.common.network;

import java.util.function.BiConsumer;
import java.util.function.Function;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;

public interface NetworkHandlerProxy {
    /** id only used on fabric, discrim only used on forge */
    <MSG> void registerMessage(ResourceLocation id, int discrim, NetworkHandler.Side side,
                             Class<MSG> clazz,
                             BiConsumer<MSG, FriendlyByteBuf> encode,
                             Function<FriendlyByteBuf, MSG> decode,
                             BiConsumer<MSG, NetworkHandler.PacketContext> handler);

    <MSG> void sendToServer(MSG packet);
    <MSG> void sendTo(MSG packet, ServerPlayer player);
    <MSG> void sendToAllPlayers(MSG packet);
}
