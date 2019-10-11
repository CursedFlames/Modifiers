package cursedflames.modifiers.common.modifier.curio;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.IForgeRegistryEntry;

public interface IModifierCurio extends IForgeRegistryEntry<IModifierCurio> {
	//TODO canApply(ItemStack)?
	/**
	 * @return a weight value used when generating or rerolling modifiers.
	 *         Higher weights mean a modifier is more likely to appear, and a
	 *         weight of 0 will never appear. (The "None" modifier is special
	 *         cased to appear regardless of weight)
	 */
	public int getWeight();

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
