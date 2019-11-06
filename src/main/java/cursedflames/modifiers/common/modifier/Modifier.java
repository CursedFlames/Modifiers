package cursedflames.modifiers.common.modifier;

import java.util.Arrays;
import java.util.List;

import cursedflames.modifiers.common.modifier.curio.IModifierEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class Modifier {
	public final ResourceLocation name;
	private int weight;
	private List<IModifierEffect> effects;
	
	public Modifier(ResourceLocation name, int weight, IModifierEffect effect) {
		this(name, weight, Arrays.asList(effect));
	}
	
	public Modifier(ResourceLocation name, int weight, List<IModifierEffect> effects) {
		this.name = name;
		this.weight = weight;
		this.effects = effects;
	}
	//TODO canApply(ItemStack)?
	/**
	 * @return a weight value used when generating or rerolling modifiers.
	 *         Higher weights mean a modifier is more likely to appear, and a
	 *         weight of 0 will never appear. (The "None" modifier is special
	 *         cased to appear regardless of weight)
	 */
	public int getWeight() {return weight;}
	
	public List<IModifierEffect> getEffects() {return effects;}
	
	/**
	 * note: does not guarantee that the modifier is not currently applied.
	 */
	public void applyModifier(LivingEntity entity, ItemStack stack, String identifier, int slot) {
		for (IModifierEffect effect : effects) {
			effect.applyModifier(entity, stack, identifier, slot);
		}
	}

	/**
	 * note: does not guarantee that the modifier is currently applied.
	 */
	public void removeModifier(LivingEntity entity, ItemStack stack, String identifier, int slot) {
		for (IModifierEffect effect : effects) {
			effect.removeModifier(entity, stack, identifier, slot);
		}
	}
}
