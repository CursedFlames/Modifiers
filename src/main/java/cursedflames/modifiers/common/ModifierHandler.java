package cursedflames.modifiers.common;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import cursedflames.modifiers.common.config.Config;
import net.minecraft.item.ItemStack;
import top.theillusivec4.curios.api.CuriosAPI;

public class ModifierHandler {
	/**
	 * Determine whether or not an itemstack can have a modifier.
	 */
	public static boolean canHaveModifier(ItemStack stack) {
		if (stack.isEmpty()) return false;
		if (stack.getMaxStackSize() > 1) return false;
		return true;
	}
	
	public static boolean allowModifierInSlot(String identifier) {
		List<String> curioVals = (List<String>) Config.CURIO_SLOTS_ALLOWED.get();
		boolean inList = curioVals.contains(identifier);
		return inList == Config.CURIO_IS_WHITELIST.get(); // (a && b) || (!a && !b)
	}
	
	public static boolean canHaveCurioModifier(ItemStack stack) {
		Set<String> tags = CuriosAPI.getCurioTags(stack.getItem());
		if (tags.isEmpty())
			return false;
		List<String> curioVals = (List<String>) Config.CURIO_SLOTS_ALLOWED.get();
		boolean whitelist = Config.CURIO_IS_WHITELIST.get();
		for (String curioVal : curioVals) {
			if (tags.contains(curioVal) == whitelist) { // (a && b) || (!a && !b)
				return true;
			}
		}
		return false;
	}
}
