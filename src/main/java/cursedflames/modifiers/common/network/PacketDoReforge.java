package cursedflames.modifiers.common.network;

import java.util.function.Supplier;

import cursedflames.modifiers.common.ModifiersMod;
import cursedflames.modifiers.common.reforge.ContainerReforge;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketDoReforge {
	public PacketDoReforge() {
		
	}
	
	public void encode(PacketBuffer buffer) {
		// No data to encode :shrug:
	}
	
	public static PacketDoReforge decode(PacketBuffer buffer) {
		return new PacketDoReforge();
	}
	
	public void handleMessage(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			// Work that needs to be threadsafe (most work)
			ServerPlayerEntity sender = ctx.get().getSender(); // the client that sent this packet
			// do stuff
//			ModifiersMod.logger.info(sender);
			Container cont = sender.openContainer;
			if (cont instanceof ContainerReforge) {
//				ModifiersMod.logger.info("reforger");
				((ContainerReforge) cont).tryDoReforge();
			}
			
		});
		ctx.get().setPacketHandled(true);
	}
}
