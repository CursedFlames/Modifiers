package cursedflames.modifiers.common.modifier.curio;

import java.util.Random;

import cursedflames.modifiers.common.modifier.Modifier;
import cursedflames.modifiers.common.modifier.Modifiers;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

public class ModifierHandlerCurio {
	private static final Random RANDOM = new Random();
	
	public static String getModifierTranslationKey(ResourceLocation modifier) {
		return modifier.getNamespace()+".modifier_curio."+modifier.getPath();
	}
	
	public static String getTranslationKey(Modifier modifier) {
		ResourceLocation loc = modifier.name;
		return getModifierTranslationKey(loc);
	}
	
	public static String getInfoTranslationKey(Modifier modifier) {
		ResourceLocation loc = modifier.name;
		return loc.getNamespace()+".modifier_curio."+loc.getPath()+".info";
	}
	
	public static Modifier getCurioModifier(ItemStack stack) {
		CompoundNBT tag = stack.getTag();
		if (tag == null) return null;
		if (!tag.contains("curioMod")) return null;
		return Modifiers.modifiers.get(new ResourceLocation(tag.getString("curioMod")));
	}
	
	public static void genCurioModifier(ItemStack stack) {
		genCurioModifier(stack, false);
	}

	public static void genCurioModifier(ItemStack stack, boolean overwrite) {
		CompoundNBT tag = stack.getOrCreateTag();
		// FIXME add chance of no modifier
		if (!overwrite && tag.contains("curioMod")) return;
		Modifier newModifier = selectModifier(stack);
		tag.putString("curioMod", newModifier.name.toString());
	}
	
	private static int getTotalWeight() {
		int total = 0;
		for (Modifier mod : Modifiers.modifiers.values()) {
			if (mod == null) continue; //shouldn't happen
			total += Math.max(0, mod.getWeight());
		}
		return total;
	}
	
	public static Modifier selectModifier(ItemStack stack) {
		int rand = RANDOM.nextInt(getTotalWeight());
		int currentWeight = 0;
		for (Modifier mod : Modifiers.modifiers.values()) {
			currentWeight += Math.max(0, mod.getWeight());
			if (rand<currentWeight)
				return mod;
		}
		// this should be unreachable
		return null;
	}
}
