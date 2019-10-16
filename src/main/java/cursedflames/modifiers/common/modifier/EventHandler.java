package cursedflames.modifiers.common.modifier;

import java.util.Set;

import cursedflames.modifiers.common.ModifierHandler;
import cursedflames.modifiers.common.ModifiersMod;
import cursedflames.modifiers.common.item.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.CuriosAPI;

public class EventHandler {
	@SubscribeEvent
	public static void onAnvilUpdate(AnvilUpdateEvent event) {
//		ModifiersMod.logger.info("AnvilUpdate");
		ItemStack left = event.getLeft();
		ItemStack right = event.getRight();
		if (!ModifierHandler.canHaveModifier(left))
			return;
		if (!right.hasTag())
			return;
//		ModifiersMod.logger.info("acceptible.");
		if (right.getItem() == ModItems.modifier_book_curio) {
			if (!ModifierHandler.canHaveCurioModifier(left))
				return;
			ItemStack output = left.copy();
			CompoundNBT tag = output.getOrCreateTag();
			tag.putString("curioMod", right.getTag().getString("bookMod"));
			event.setOutput(output);
			event.setMaterialCost(1);
			event.setCost(1); // Minecraft doesn't like free anvil recipes apparently
//			ModifiersMod.logger.info("set output");
		} else if (right.getItem() == ModItems.modifier_book_tool) {
			Set<String> tags = CuriosAPI.getCurioTags(left.getItem());
			if (!tags.isEmpty()) // don't allow non-curio mods on curios
				return;
		}
//		if (recipe != null) {
//			event.setOutput(recipe.result.copy());
//			event.setCost(recipe.xpLevelCost);
//			event.setMaterialCost(recipe.materialCost);
//		}
	}
}
