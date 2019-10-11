package cursedflames.modifiers.common;

import net.minecraft.item.ItemStack;

public class ModifierHandler {
	/**
	 * Determine whether or not an itemstack can have a modifier.
	 * 
	 * Does not check capabilities for curio modifiers, as the capability may not be attached yet.
	 */
	public static boolean canHaveModifier(ItemStack stack) {
		if (stack.isEmpty()) return false;
		if (stack.getMaxStackSize() > 1) return false;
		return true;
	}
}
