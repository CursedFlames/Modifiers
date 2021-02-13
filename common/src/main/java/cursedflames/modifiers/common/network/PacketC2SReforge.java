package cursedflames.modifiers.common.network;

import cursedflames.modifiers.common.reforge.SmithingScreenHandlerReforge;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;

public class PacketC2SReforge {
	public PacketC2SReforge() {}

	PacketC2SReforge(PacketByteBuf buf) {}

	void encode(PacketByteBuf buf) {}

	public static class Handler {
		public static void handle(PacketC2SReforge packet, NetworkHandler.PacketContext context) {
			PlayerEntity player = context.player;
			if (player != null && player.currentScreenHandler instanceof SmithingScreenHandlerReforge) {
				((SmithingScreenHandlerReforge) player.currentScreenHandler).tryReforge();
			}
		}
	}
}
