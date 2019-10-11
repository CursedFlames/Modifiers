package cursedflames.modifiers.common.modifier.curio;

import java.util.Random;

import cursedflames.modifiers.common.ModifiersMod;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

public class ModifierHandlerCurio {
	private static final Random RANDOM = new Random();
	
	public static IModifierCurio getCurioModifier(ItemStack stack) {
		CompoundNBT tag = stack.getTag();
		if (tag == null) return null;
		if (!tag.contains("curioMod")) return null;
		return ModifierCurioRegistry.MODIFIERS_CURIO.getValue(new ResourceLocation(tag.getString("curioMod")));
	}
	
	public static void genCurioModifier(ItemStack stack) {
		CompoundNBT tag = stack.getOrCreateTag();
		// FIXME add chance of no modifier
		// FIXME allow force generation (e.g. reforge)
		if (tag.contains("curioMod")) return;
		IModifierCurio newModifier = selectModifier(stack);
		tag.putString("curioMod", newModifier.getRegistryName().toString());
	}
	
	private static int getTotalWeight() {
		int total = 0;
		ModifiersMod.logger.info(ModifierCurioRegistry.MODIFIERS_CURIO);
		for (IModifierCurio mod : ModifierCurioRegistry.MODIFIERS_CURIO) {
			if (mod == null) continue; //shouldn't happen
			total += Math.max(0, mod.getWeight());
		}
		return total;
	}
	
	public static IModifierCurio selectModifier(ItemStack stack) {
		int rand = RANDOM.nextInt(getTotalWeight());
		int currentWeight = 0;
		for (IModifierCurio mod : ModifierCurioRegistry.MODIFIERS_CURIO) {
			currentWeight += Math.max(0, mod.getWeight());
			if (rand<currentWeight)
				return mod;
		}
		// this should be unreachable
		return null;
	}
}
