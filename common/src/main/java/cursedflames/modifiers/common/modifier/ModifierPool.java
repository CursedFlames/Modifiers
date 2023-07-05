package cursedflames.modifiers.common.modifier;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ModifierPool {
	public Predicate<ItemStack> isApplicable;
	public int totalWeight = 0;
	// Pool weight, for choosing which pool to use when multiple are applicable.
	public int poolWeight = 0;
	public List<Modifier> modifiers = new ArrayList<>();

	public ModifierPool(Predicate<ItemStack> isApplicable) {
		this.isApplicable = isApplicable;
	}

	public void add(Modifier mod) {
		modifiers.add(mod);
		totalWeight += mod.weight;
	}

	public Modifier roll(RandomSource random) {
		if (totalWeight == 0 || modifiers.isEmpty()) return null;
		int i = random.nextInt(totalWeight);
		int j = 0;
		for (Modifier modifier : modifiers) {
			j += modifier.weight;
			if (i < j) {
				return modifier;
			}
		}
		// This shouldn't happen
		return null;
	}
}
