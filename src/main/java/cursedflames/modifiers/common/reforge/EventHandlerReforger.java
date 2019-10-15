package cursedflames.modifiers.common.reforge;

import cursedflames.modifiers.common.ModifiersMod;
import cursedflames.modifiers.common.config.Config;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.NetworkHooks;

public class EventHandlerReforger {
	@SuppressWarnings("deprecation")
	@SubscribeEvent
	public static void onBlockRightClick(RightClickBlock event) {
		if (!Config.REFORGE_ENABLED.get()) return;
		PlayerEntity player = event.getPlayer();
		if (player.isSneaking()) return; // allow regular block interaction
		
		BlockPos pos = event.getPos();
		World world = player.world;
		if (world.isBlockLoaded(pos)) {
			BlockState state = world.getBlockState(pos);
			Block block = state.getBlock();
			if (block == Blocks.SMITHING_TABLE) {
				ModifiersMod.logger.info("smithing table clicked");
				event.setCanceled(true);
				if (!world.isRemote) {
					INamedContainerProvider containerProvider = new INamedContainerProvider() {
						@Override
						public Container createMenu(int windowId, PlayerInventory playerInventory,
								PlayerEntity player) {
							return new ContainerReforge(windowId, world, pos, playerInventory, player);
						}

						@Override
						public ITextComponent getDisplayName() {
							return new TranslationTextComponent("modifiers.todokey");
						}
						
					};
					NetworkHooks.openGui((ServerPlayerEntity) player, containerProvider, pos);
				}
			}
		}
	}
}
