package cursedflames.modifiers.common.modifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import cursedflames.modifiers.common.ModifiersMod;
import cursedflames.modifiers.common.modifier.curio.modifiers.AttributeModifierProvider;
import cursedflames.modifiers.common.modifier.effect.EffectAttributeModifier;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class Modifiers {
	public static Map<ResourceLocation, Modifier> modifiers = new HashMap<>();
	public static final Modifier NONE = new Modifier(
			new ResourceLocation(ModifiersMod.MODID, "none"), 0, new ArrayList<>()) {
		@Override
		public boolean canItemStackHaveModifier(ItemStack stack, Set<String> curioTags) {
			return true; // Allow "none" modifier on any item
		}
	};
	
	private static void register(String name,
			IAttribute attribute, double amount, Operation operation, int weight) {
		Multimap<String, AttributeModifierProvider> map = HashMultimap.create();
		map.put(attribute.getName(), new AttributeModifierProvider(
				attribute.getName(), name, amount, operation));
		EffectAttributeModifier effect = new EffectAttributeModifier(map);
		ResourceLocation loc = new ResourceLocation(ModifiersMod.MODID, name);
		Modifier mod = new Modifier(loc, weight, effect);
		modifiers.put(loc, mod);
	}
	
	public static void init() {
		modifiers.put(NONE.name, NONE);
		register("half_hearted", SharedMonsterAttributes.MAX_HEALTH, 1, Operation.ADDITION, 3);
		register("hearty", SharedMonsterAttributes.MAX_HEALTH, 2, Operation.ADDITION, 1);
		register("hard", SharedMonsterAttributes.ARMOR, 1, Operation.ADDITION, 3);
		register("guarding", SharedMonsterAttributes.ARMOR, 1.5, Operation.ADDITION, 2);
		register("armored", SharedMonsterAttributes.ARMOR, 2, Operation.ADDITION, 1);
		register("warding", SharedMonsterAttributes.ARMOR_TOUGHNESS, 1, Operation.ADDITION, 2);
		register("jagged", SharedMonsterAttributes.ATTACK_DAMAGE, 0.02, Operation.MULTIPLY_TOTAL, 2);
		register("spiked", SharedMonsterAttributes.ATTACK_DAMAGE, 0.04, Operation.MULTIPLY_TOTAL, 2);
		register("angry", SharedMonsterAttributes.ATTACK_DAMAGE, 0.06, Operation.MULTIPLY_TOTAL, 1);
		register("menacing", SharedMonsterAttributes.ATTACK_DAMAGE, 0.08, Operation.MULTIPLY_TOTAL, 1);
		register("brisk", SharedMonsterAttributes.MOVEMENT_SPEED, 0.01, Operation.MULTIPLY_TOTAL, 2);
		register("fleeting", SharedMonsterAttributes.MOVEMENT_SPEED, 0.02, Operation.MULTIPLY_TOTAL, 2);
		register("hasty", SharedMonsterAttributes.MOVEMENT_SPEED, 0.03, Operation.MULTIPLY_TOTAL, 1);
		register("quick", SharedMonsterAttributes.MOVEMENT_SPEED, 0.04, Operation.MULTIPLY_TOTAL, 1);
		register("wild", SharedMonsterAttributes.ATTACK_SPEED, 0.02, Operation.MULTIPLY_TOTAL, 2);
		register("rash", SharedMonsterAttributes.ATTACK_SPEED, 0.04, Operation.MULTIPLY_TOTAL, 2);
		register("intrepid", SharedMonsterAttributes.ATTACK_SPEED, 0.06, Operation.MULTIPLY_TOTAL, 1);
		register("violent", SharedMonsterAttributes.ATTACK_SPEED, 0.08, Operation.MULTIPLY_TOTAL, 1);
	}
}
