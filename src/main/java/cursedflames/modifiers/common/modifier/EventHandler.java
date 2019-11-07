package cursedflames.modifiers.common.modifier;

import cursedflames.modifiers.common.ModifierHandler;
import cursedflames.modifiers.common.item.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHandler {
	@SubscribeEvent
	public static void onAnvilUpdate(AnvilUpdateEvent event) {
//		ModifiersMod.logger.info("AnvilUpdate");
		ItemStack left = event.getLeft();
		ItemStack right = event.getRight();
		if (right.getItem() != ModItems.modifier_book) return;
		if (!right.hasTag()) return;
		if (!ModifierHandler.canHaveModifier(left)) return;
//		ModifiersMod.logger.info("acceptible.");
		// FIXME check if modifier allowed on item
		ItemStack output = left.copy();
		CompoundNBT tag = output.getOrCreateTag();
		tag.putString("curioMod", right.getTag().getString("bookMod"));
		event.setOutput(output);
		event.setMaterialCost(1);
		event.setCost(1); // Minecraft doesn't like free anvil recipes apparently
//		ModifiersMod.logger.info("set output");
	}
}
