package cursedflames.modifiers.common.network;

import cursedflames.modifiers.common.reforge.SmithingScreenHandlerReforge;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.FriendlyByteBuf;

public class PacketC2SReforge {
	public PacketC2SReforge() {}

	PacketC2SReforge(FriendlyByteBuf buf) {}

	void encode(FriendlyByteBuf buf) {}

	public static class Handler {
		public static void handle(PacketC2SReforge packet, NetworkHandler.PacketContext context) {
			Player player = context.player;
			if (player != null && player.containerMenu instanceof SmithingScreenHandlerReforge) {
				((SmithingScreenHandlerReforge) player.containerMenu).tryReforge();
			}
		}
	}
}
