package cursedflames.modifiers.common.modifier;

import cursedflames.modifiers.ModifiersMod;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.resources.ResourceLocation;
import java.util.HashMap;
import java.util.Map;

import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.*;

public class Modifiers {
	public static Map<ResourceLocation, Modifier> modifiers = new HashMap<>();

	public static Modifier NONE = new Modifier.ModifierBuilder(new ResourceLocation(ModifiersMod.MODID, "none"), "modifier_none", Modifier.ModifierType.BOTH).setWeight(0).build();
	static {
		modifiers.put(NONE.name, NONE);
	}

	public static ModifierPool curio_pool = new ModifierPool(stack -> {
		// TODO find a better way of determining if an item is armor
		if (stack.getItem() instanceof ArmorItem) return true;
		return ModifiersMod.curioProxy.isModifiableCurio(stack);
	});

	// TODO sub-pools for different weapon types? melee/ranged
	public static ModifierPool tool_pool = new ModifierPool(stack -> {
		// TODO find a better way of determining if an item is a tool
		// TODO probably want modifiers on tridents and ranged weapons too
		if (stack.getItem() instanceof SwordItem) return true;
		return stack.getItem() instanceof DiggerItem;
	});

	private static Modifier.ModifierBuilder curio(String name) {
		return new Modifier.ModifierBuilder(new ResourceLocation(ModifiersMod.MODID, name), "modifier_" + name, Modifier.ModifierType.EQUIPPED);
	}

	private static Modifier.ModifierBuilder both(String name) {
		return new Modifier.ModifierBuilder(new ResourceLocation(ModifiersMod.MODID, name), "modifier_" + name, Modifier.ModifierType.BOTH);
	}

	private static Modifier.ModifierBuilder tool(String name) {
		return new Modifier.ModifierBuilder(new ResourceLocation(ModifiersMod.MODID, name), "modifier_" + name, Modifier.ModifierType.HELD);
	}

	private static void addCurio(Modifier modifier) {
		modifiers.put(modifier.name, modifier);
		curio_pool.add(modifier);
	}

	private static void addTool(Modifier modifier) {
		modifiers.put(modifier.name, modifier);
		tool_pool.add(modifier);
	}

	private static Modifier.AttributeModifierSupplier mod(double amount, Operation op) {
		return new Modifier.AttributeModifierSupplier(amount, op);
	}

	public static void init() {
		addCurio(curio("half_hearted").setWeight(300).addModifier(Attributes.MAX_HEALTH, mod(1, ADDITION)).build());
		addCurio(curio("hearty").setWeight(100).addModifier(Attributes.MAX_HEALTH, mod(2, ADDITION)).build());
		addCurio(curio("hard").setWeight(300).addModifier(Attributes.ARMOR, mod(1, ADDITION)).build());
		addCurio(curio("guarding").setWeight(200).addModifier(Attributes.ARMOR, mod(1.5, ADDITION)).build());
		addCurio(curio("armored").setWeight(100).addModifier(Attributes.ARMOR, mod(2, ADDITION)).build());
		addCurio(curio("warding").setWeight(200).addModifier(Attributes.ARMOR_TOUGHNESS, mod(1, ADDITION)).build());
		addCurio(curio("jagged").setWeight(200).addModifier(Attributes.ATTACK_DAMAGE, mod(0.01, MULTIPLY_TOTAL)).build());
		addCurio(curio("spiked").setWeight(200).addModifier(Attributes.ATTACK_DAMAGE, mod(0.02, MULTIPLY_TOTAL)).build());
		addCurio(curio("angry").setWeight(100).addModifier(Attributes.ATTACK_DAMAGE, mod(0.03, MULTIPLY_TOTAL)).build());
		addCurio(curio("menacing").setWeight(100).addModifier(Attributes.ATTACK_DAMAGE, mod(0.04, MULTIPLY_TOTAL)).build());
		addCurio(curio("brisk").setWeight(200).addModifier(Attributes.MOVEMENT_SPEED, mod(0.01, MULTIPLY_TOTAL)).build());
		addCurio(curio("fleeting").setWeight(200).addModifier(Attributes.MOVEMENT_SPEED, mod(0.02, MULTIPLY_TOTAL)).build());
		addCurio(curio("hasty").setWeight(100).addModifier(Attributes.MOVEMENT_SPEED, mod(0.03, MULTIPLY_TOTAL)).build());
		addCurio(curio("quick").setWeight(100).addModifier(Attributes.MOVEMENT_SPEED, mod(0.04, MULTIPLY_TOTAL)).build());
		addCurio(curio("wild").setWeight(200).addModifier(Attributes.ATTACK_SPEED, mod(0.01, MULTIPLY_TOTAL)).build());
		addCurio(curio("rash").setWeight(200).addModifier(Attributes.ATTACK_SPEED, mod(0.02, MULTIPLY_TOTAL)).build());
		addCurio(curio("intrepid").setWeight(100).addModifier(Attributes.ATTACK_SPEED, mod(0.03, MULTIPLY_TOTAL)).build());
		addCurio(curio("violent").setWeight(100).addModifier(Attributes.ATTACK_SPEED, mod(0.04, MULTIPLY_TOTAL)).build());

		addTool(tool("legendary").setWeight(30)
				.addModifier(Attributes.ATTACK_DAMAGE, mod(0.15, MULTIPLY_TOTAL))
				.addModifier(Attributes.ATTACK_SPEED, mod(0.1, MULTIPLY_TOTAL))
				.addModifier(Attributes.MOVEMENT_SPEED, mod(0.05, MULTIPLY_TOTAL))
				.build());

		addTool(tool("deadly").setWeight(100)
				.addModifier(Attributes.ATTACK_DAMAGE, mod(0.15, MULTIPLY_TOTAL))
				.build());

		addTool(tool("vicious").setWeight(100)
				.addModifier(Attributes.ATTACK_DAMAGE, mod(0.1, MULTIPLY_TOTAL))
				.build());

		addTool(tool("sharp").setWeight(100)
				.addModifier(Attributes.ATTACK_DAMAGE, mod(0.05, MULTIPLY_TOTAL))
				.build());

		addTool(tool("broken").setWeight(70)
				.addModifier(Attributes.ATTACK_DAMAGE, mod(-0.2, MULTIPLY_TOTAL))
				.build());

		addTool(tool("damaged").setWeight(100)
				.addModifier(Attributes.ATTACK_DAMAGE, mod(-0.1, MULTIPLY_TOTAL))
				.build());

		addTool(tool("agile").setWeight(100)
				.addModifier(Attributes.ATTACK_SPEED, mod(0.05, MULTIPLY_TOTAL))
				.addModifier(Attributes.MOVEMENT_SPEED, mod(0.1, MULTIPLY_TOTAL))
				.build());

		addTool(tool("swift").setWeight(100)
				.addModifier(Attributes.ATTACK_SPEED, mod(0.1, MULTIPLY_TOTAL))
				.build());

		addTool(tool("sluggish").setWeight(100)
				.addModifier(Attributes.ATTACK_SPEED, mod(-0.05, MULTIPLY_TOTAL))
				.addModifier(Attributes.MOVEMENT_SPEED, mod(-0.1, MULTIPLY_TOTAL))
				.build());

		addTool(tool("slow").setWeight(100)
				.addModifier(Attributes.ATTACK_SPEED, mod(-0.15, MULTIPLY_TOTAL))
				.build());

		addTool(tool("light").setWeight(100)
				.addModifier(Attributes.ATTACK_DAMAGE, mod(-0.1, MULTIPLY_TOTAL))
				.addModifier(Attributes.ATTACK_SPEED, mod(0.15, MULTIPLY_TOTAL))
				.build());

		addTool(tool("heavy").setWeight(100)
				.addModifier(Attributes.ATTACK_DAMAGE, mod(0.2, MULTIPLY_TOTAL))
				.addModifier(Attributes.ATTACK_SPEED, mod(-0.15, MULTIPLY_TOTAL))
				.addModifier(Attributes.MOVEMENT_SPEED, mod(-0.05, MULTIPLY_TOTAL))
				.build());
	}

	static {
		init();
	}
}
