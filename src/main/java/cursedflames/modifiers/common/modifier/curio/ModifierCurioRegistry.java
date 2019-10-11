package cursedflames.modifiers.common.modifier.curio;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import cursedflames.modifiers.common.ModifiersMod;
import cursedflames.modifiers.common.modifier.curio.modifiers.AttributeModifierProvider;
import cursedflames.modifiers.common.modifier.curio.modifiers.ModifierAttributeModifier;
import cursedflames.modifiers.common.modifier.curio.modifiers.ModifierNone;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

public class ModifierCurioRegistry {
	public static IModifierCurio NONE;
	
	public static IForgeRegistry<IModifierCurio> MODIFIERS_CURIO = null;
	
	public static void createRegistry(RegistryEvent.NewRegistry event) {
		ModifiersMod.logger.info("CREATING REGISTRY. QWERT");
		MODIFIERS_CURIO = new RegistryBuilder<IModifierCurio>()
				.setName(new ResourceLocation(ModifiersMod.MODID, "modifiers_curio"))
				.setType(IModifierCurio.class).setIDRange(0, Integer.MAX_VALUE-1).create();
	}
	
	private static void register(IForgeRegistry<IModifierCurio> reg, String name,
			IAttribute attribute, double amount, Operation operation, int weight) {
		Multimap<String, AttributeModifierProvider> map = HashMultimap.create();
		map.put(attribute.getName(), new AttributeModifierProvider(
				attribute.getName(), name, amount, operation));
		
		reg.register(new ModifierAttributeModifier(map, weight)
				.setRegistryName(new ResourceLocation(ModifiersMod.MODID, name)));
	}
	
	public static void registerCurioModifiers(RegistryEvent.Register<IModifierCurio> event) {
		ModifiersMod.logger.info("registering modifiers HJKL");
		IForgeRegistry<IModifierCurio> reg = event.getRegistry();
		reg.register(NONE = new ModifierNone().setRegistryName(new ResourceLocation(ModifiersMod.MODID, "none")));
		//+10x speed is a good way to make sure it's working. :P
		register(reg, "speed", SharedMonsterAttributes.MOVEMENT_SPEED, 10, Operation.ADDITION, 0);
		
		register(reg, "half_hearted", SharedMonsterAttributes.MAX_HEALTH, 1, Operation.ADDITION, 3);
		register(reg, "hearty", SharedMonsterAttributes.MAX_HEALTH, 2, Operation.ADDITION, 1);
		register(reg, "hard", SharedMonsterAttributes.ARMOR, 1, Operation.ADDITION, 3);
		register(reg, "guarding", SharedMonsterAttributes.ARMOR, 1.5, Operation.ADDITION, 2);
		register(reg, "armored", SharedMonsterAttributes.ARMOR, 2, Operation.ADDITION, 1);
		register(reg, "warding", SharedMonsterAttributes.ARMOR_TOUGHNESS, 1, Operation.ADDITION, 2);
		register(reg, "jagged", SharedMonsterAttributes.ATTACK_DAMAGE, 0.02, Operation.MULTIPLY_TOTAL, 2);
		register(reg, "spiked", SharedMonsterAttributes.ATTACK_DAMAGE, 0.04, Operation.MULTIPLY_TOTAL, 2);
		register(reg, "angry", SharedMonsterAttributes.ATTACK_DAMAGE, 0.06, Operation.MULTIPLY_TOTAL, 1);
		register(reg, "menacing", SharedMonsterAttributes.ATTACK_DAMAGE, 0.08, Operation.MULTIPLY_TOTAL, 1);
		register(reg, "brisk", SharedMonsterAttributes.MOVEMENT_SPEED, 0.01, Operation.MULTIPLY_TOTAL, 2);
		register(reg, "fleeting", SharedMonsterAttributes.MOVEMENT_SPEED, 0.02, Operation.MULTIPLY_TOTAL, 2);
		register(reg, "hasty", SharedMonsterAttributes.MOVEMENT_SPEED, 0.03, Operation.MULTIPLY_TOTAL, 1);
		register(reg, "quick", SharedMonsterAttributes.MOVEMENT_SPEED, 0.04, Operation.MULTIPLY_TOTAL, 1);
		register(reg, "wild", SharedMonsterAttributes.ATTACK_SPEED, 0.02, Operation.MULTIPLY_TOTAL, 2);
		register(reg, "rash", SharedMonsterAttributes.ATTACK_SPEED, 0.04, Operation.MULTIPLY_TOTAL, 2);
		register(reg, "intrepid", SharedMonsterAttributes.ATTACK_SPEED, 0.06, Operation.MULTIPLY_TOTAL, 1);
		register(reg, "violent", SharedMonsterAttributes.ATTACK_SPEED, 0.08, Operation.MULTIPLY_TOTAL, 1);
	}
}
