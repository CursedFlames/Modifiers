package cursedflames.modifiers.common.proxy;

import cursedflames.modifiers.client.gui.ScreenReforge;
import cursedflames.modifiers.common.reforge.ContainerReforge;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ClientProxy implements IProxy {
	@Override
	public void init(FMLCommonSetupEvent event) {
		ScreenManager.registerFactory(ContainerReforge.CONTAINER_REFORGE, ScreenReforge::new);
	}
	
	@Override
	public World getClientWorld() {
		return Minecraft.getInstance().world;
	}
	
	@Override	
	public PlayerEntity getClientPlayer() {
		return Minecraft.getInstance().player;
	}
}
