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

package cursedflames.modifiers.common.network;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.thread.ThreadExecutor;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class NetworkHandler {
	private static NetworkHandlerProxy proxy;

	// Should only be called during mod clinit
	public static void setProxy(NetworkHandlerProxy proxy) {
		NetworkHandler.proxy = proxy;
	}

	public static void register() {
		// Commented out example until I actually add any packets
//        registerMessage("foo", PacketFoo.class, PacketFoo::encode,
//            PacketFoo::new, mainThreadHandler(PacketFoo.Handler::handle));
	}

	private static <T> BiConsumer<T, PacketContext> mainThreadHandler(Consumer<? super T> handler) {
		return (packet, ctx) -> ctx.threadExecutor.submit(() -> handler.accept(packet));
	}

//    private static <T> BiConsumer<T, PacketContext> mainThreadHandler(BiConsumer<? super T, ? super Level> handler) {
//        return (packet, ctx) -> ctx.getTaskQueue().submit(() -> handler.accept(packet, Minecraft.getInstance().level));
//    }


	/** id only used on fabric, discrim only used on forge */
	public static <MSG> void registerMessage(Identifier id, int discrim, NetworkHandler.Side side,
							   Class<MSG> clazz,
							   BiConsumer<MSG, PacketByteBuf> encode,
							   Function<PacketByteBuf, MSG> decode,
							   BiConsumer<MSG, NetworkHandler.PacketContext> handler) {
		proxy.registerMessage(id, discrim, side, clazz, encode, decode, handler);
	}

	public static <MSG> void sendToServer(MSG packet) {
		proxy.sendToServer(packet);
	}
	public static <MSG> void sendTo(MSG packet, ServerPlayerEntity player) {
		proxy.sendTo(packet, player);
	}
	public static <MSG> void sendToAllPlayers(MSG packet) {
		proxy.sendToAllPlayers(packet);
	}

	// Based on Fabric's PacketContext
	public static class PacketContext {
		@Nullable public final PlayerEntity player;
		public final ThreadExecutor<?> threadExecutor;
		public PacketContext(@Nullable PlayerEntity player, ThreadExecutor<?> threadExecutor) {
			this.player = player;
			this.threadExecutor = threadExecutor;
		}
	}

	public enum Side {
		ServerToClient, ClientToServer
	}
}
