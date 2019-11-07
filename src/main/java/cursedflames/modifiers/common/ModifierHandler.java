package cursedflames.modifiers.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import cursedflames.modifiers.common.config.Config;
import cursedflames.modifiers.common.item.ModItems;
import cursedflames.modifiers.common.modifier.Modifier;
import cursedflames.modifiers.common.modifier.Modifiers;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import top.theillusivec4.curios.api.CuriosAPI;

public class ModifierHandler {
	/**
	 * Determine whether or not an itemstack can have a modifier.
	 */
	public static boolean canHaveModifier(ItemStack stack) {
		if (stack.isEmpty()) return false;
		if (stack.getMaxStackSize() > 1) return false;
		if (stack.getItem() == ModItems.modifier_book) return false;
		return true;
	}
	
	public static boolean allowModifierInSlot(String identifier) {
		List<String> curioVals = (List<String>) Config.CURIO_SLOTS_ALLOWED.get();
		boolean inList = curioVals.contains(identifier);
		return inList == Config.CURIO_IS_WHITELIST.get(); // (a && b) || (!a && !b)
	}
	
//	public static boolean canHaveCurioModifier(ItemStack stack) {
//		Set<String> tags = CuriosAPI.getCurioTags(stack.getItem());
//		if (tags.isEmpty())
//			return false;
//		List<String> curioVals = (List<String>) Config.CURIO_SLOTS_ALLOWED.get();
//		boolean whitelist = Config.CURIO_IS_WHITELIST.get();
//		for (String curioVal : curioVals) {
//			if (tags.contains(curioVal) == whitelist) { // (a && b) || (!a && !b)
//				return true;
//			}
//		}
//		return false;
//	}
	
	public static List<Modifier> getPossibleModifiers(ItemStack stack) {
		List<Modifier> modifiers = new ArrayList<>();
		Set<String> curioTags = CuriosAPI.getCurioTags(stack.getItem());
		for (Modifier modifier : Modifiers.modifiers.values()) {
			if (modifier.canItemStackHaveModifier(stack, curioTags)) {
				modifiers.add(modifier);
			}
		}
		return modifiers;
	}
	
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
		if (!overwrite && stack.hasTag() && stack.getTag().contains("curioMod")) return;
		// FIXME check that there are modifiers available other than "none"
		List<Modifier> modifiers = getPossibleModifiers(stack);
		if (modifiers.size() < 2) return; // only "none" available
		CompoundNBT tag = stack.getOrCreateTag();
		// FIXME add chance of no modifier
		Modifier newModifier = selectModifier(stack, modifiers);
		tag.putString("curioMod", newModifier.name.toString());
	}
	
	private static int getTotalWeight(List<Modifier> modifiers) {
		int total = 0;
		for (Modifier mod : modifiers) {
			if (mod == null) continue; //shouldn't happen
			total += Math.max(0, mod.getWeight());
		}
		return total;
	}
	
	public static Modifier selectModifier(ItemStack stack, List<Modifier> possibleModifiers) {
		int rand = RANDOM.nextInt(getTotalWeight(possibleModifiers));
		int currentWeight = 0;
		for (Modifier mod : possibleModifiers) {
			currentWeight += Math.max(0, mod.getWeight());
			if (rand<currentWeight)
				return mod;
		}
		// this should be unreachable
		return null;
	}
}
