package cursedflames.modifiers.common.curio;

import net.minecraft.item.ItemStack;

// Used for soft dependency on Curios.
public interface ICurioProxy {
	default boolean isModifiableCurio(ItemStack stack) {
		return false;
	}
}
