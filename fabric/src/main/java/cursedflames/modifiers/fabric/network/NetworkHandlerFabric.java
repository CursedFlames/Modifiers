/*
 * Based on the network implementation in Cubic Chunks
 * (see https://github.com/OpenCubicChunks/CubicChunks/blob/b4a0e252496a65831947b6086c923481aa8f4c05/src/main/java/io/github/opencubicchunks/cubicchunks/network/PacketDispatcher.java)
 *
 * The MIT License (MIT)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package cursedflames.modifiers.fabric.network;

import cursedflames.modifiers.common.network.NetworkHandler;
import cursedflames.modifiers.common.network.NetworkHandlerProxy;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class NetworkHandlerFabric implements NetworkHandlerProxy {
	private static final Map<Class<?>, BiConsumer<?, FriendlyByteBuf>> ENCODERS = new ConcurrentHashMap<>();
	private static final Map<Class<?>, ResourceLocation> PACKET_IDS = new ConcurrentHashMap<>();

	@Override public <MSG> void registerMessage(ResourceLocation id, int discrim, NetworkHandler.Side side,
							   Class<MSG> clazz,
							   BiConsumer<MSG, FriendlyByteBuf> encode,
							   Function<FriendlyByteBuf, MSG> decode,
							   BiConsumer<MSG, NetworkHandler.PacketContext> handler) {
		ENCODERS.put(clazz, encode);
		PACKET_IDS.put(clazz, id);
		if (side == NetworkHandler.Side.ClientToServer) {
			ServerPlayNetworking.registerGlobalReceiver(
					id, (MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler_, FriendlyByteBuf buf, PacketSender responseSender) -> {
						MSG packet = decode.apply(buf);
						handler.accept(packet, new NetworkHandler.PacketContext(player, server));
					}
			);
		} else {
			ClientPlayNetworking.registerGlobalReceiver(
					id, (Minecraft client, ClientPacketListener handler_, FriendlyByteBuf buf, PacketSender responseSender) -> {
						MSG packet = decode.apply(buf);
						handler.accept(packet, new NetworkHandler.PacketContext(client.player, client));
					}
			);
		}
	}

	@Override public <MSG> void sendToServer(MSG packet) {
		ResourceLocation packetId = PACKET_IDS.get(packet.getClass());
		@SuppressWarnings("unchecked")
		BiConsumer<MSG, FriendlyByteBuf> encoder = (BiConsumer<MSG, FriendlyByteBuf>) ENCODERS.get(packet.getClass());
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		encoder.accept(packet, buf);
        ClientPlayNetworking.send(packetId, buf);
	}
	@Override public <MSG> void sendTo(MSG packet, ServerPlayer player) {
		ResourceLocation packetId = PACKET_IDS.get(packet.getClass());
		@SuppressWarnings("unchecked")
		BiConsumer<MSG, FriendlyByteBuf> encoder = (BiConsumer<MSG, FriendlyByteBuf>) ENCODERS.get(packet.getClass());
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		encoder.accept(packet, buf);
		ServerPlayNetworking.send(player, packetId, buf);
	}
	@Override public <MSG> void sendToAllPlayers(MSG packet) {
		throw new UnsupportedOperationException();
	}
}
