package cursedflames.modifiers.forge.network;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

import cursedflames.modifiers.common.network.NetworkHandler;
import cursedflames.modifiers.common.network.NetworkHandlerProxy;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.thread.BlockableEventLoop;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkHandlerForge implements NetworkHandlerProxy {
	private static final SimpleChannel channel = NetworkRegistry.newSimpleChannel(
			new ResourceLocation("modifiers", "net"), ()->"", (a)->true, (a)->true
	);

	private static NetworkHandler.PacketContext convertContext(NetworkEvent.Context context) {
		BlockableEventLoop<?> executor = LogicalSidedProvider.WORKQUEUE.get(context.getDirection().getReceptionSide());
		return new NetworkHandler.PacketContext(context.getSender(), executor);
	}

	@Override public <MSG> void registerMessage(ResourceLocation id, int discrim, NetworkHandler.Side side, Class<MSG> clazz, BiConsumer<MSG, FriendlyByteBuf> encode,
												Function<FriendlyByteBuf, MSG> decode,
												BiConsumer<MSG, NetworkHandler.PacketContext> handler) {
		Optional<NetworkDirection> networkDirection = Optional.of(side == NetworkHandler.Side.ClientToServer ? NetworkDirection.PLAY_TO_SERVER : NetworkDirection.PLAY_TO_CLIENT);
		channel.registerMessage(discrim, clazz, encode, decode, (msg, context) -> handler.accept(msg, convertContext(context.get())), networkDirection);
	}

	@Override public <MSG> void sendToServer(MSG packet) {
		channel.sendToServer(packet);
	}

	@Override public <MSG> void sendTo(MSG packet, ServerPlayer player) {
		channel.send(PacketDistributor.PLAYER.with(()->player), packet);
	}

	@Override public <MSG> void sendToAllPlayers(MSG packet) {
		channel.send(PacketDistributor.ALL.noArg(), packet);
	}
}
