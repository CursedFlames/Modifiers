package cursedflames.modifiers.common.modifier.curio;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.IForgeRegistryEntry;

public interface IModifierEffect extends IForgeRegistryEntry<IModifierEffect> {
	/**
	 * note: does not guarantee that the modifier is not currently applied.
	 * 
	 * @param player
	 * @param stack
	 * @param slot
	 */
	default public void applyModifier(LivingEntity entity, ItemStack stack, String identifier, int slot) {}

	/**
	 * note: does not guarantee that the modifier is currently applied.
	 * 
	 * @param player
	 * @param stack
	 * @param slot
	 */
	default public void removeModifier(LivingEntity entity, ItemStack stack, String identifier, int slot) {}
}
