package cursedflames.modifiers.common.modifier;

import cursedflames.modifiers.ModifiersMod;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

import static net.minecraft.entity.ai.attributes.AttributeModifier.Operation;

public class Modifiers {
	public static Map<ResourceLocation, Modifier> modifiers = new HashMap<>();

	public static Modifier NONE = builder("none").setWeight(0).build();

	public static ModifierPool curio_pool = new ModifierPool(stack -> {
		// TODO check if curios loaded and item has curio tags
		return true;
	});

	private static Modifier.ModifierBuilder builder(String name) {
		return new Modifier.ModifierBuilder(new ResourceLocation(ModifiersMod.MODID, name), "modifier_" + name);
	}

	private static void curio(Modifier modifier) {
		modifiers.put(modifier.name, modifier);
		curio_pool.add(modifier);
	}

	private static Modifier.AttributeModifierSupplier mod(double amount, Operation op) {
		return new Modifier.AttributeModifierSupplier(amount, op);
	}

	public static void init() {
		curio(builder("half_hearted").setWeight(300).addModifier(Attributes.MAX_HEALTH, mod(1, Operation.ADDITION)).build());
		curio(builder("hearty").setWeight(100).addModifier(Attributes.MAX_HEALTH, mod(2, Operation.ADDITION)).build());
		curio(builder("hard").setWeight(300).addModifier(Attributes.ARMOR, mod(1, Operation.ADDITION)).build());
		curio(builder("guarding").setWeight(200).addModifier(Attributes.ARMOR, mod(1.5, Operation.ADDITION)).build());
		curio(builder("armored").setWeight(100).addModifier(Attributes.ARMOR, mod(2, Operation.ADDITION)).build());
		curio(builder("warding").setWeight(200).addModifier(Attributes.ARMOR_TOUGHNESS, mod(1, Operation.ADDITION)).build());
		curio(builder("jagged").setWeight(200).addModifier(Attributes.ATTACK_DAMAGE, mod(0.02, Operation.MULTIPLY_TOTAL)).build());
		curio(builder("spiked").setWeight(200).addModifier(Attributes.ATTACK_DAMAGE, mod(0.04, Operation.MULTIPLY_TOTAL)).build());
		curio(builder("angry").setWeight(100).addModifier(Attributes.ATTACK_DAMAGE, mod(0.06, Operation.MULTIPLY_TOTAL)).build());
		curio(builder("menacing").setWeight(100).addModifier(Attributes.ATTACK_DAMAGE, mod(0.08, Operation.MULTIPLY_TOTAL)).build());
		curio(builder("brisk").setWeight(200).addModifier(Attributes.MOVEMENT_SPEED, mod(0.01, Operation.MULTIPLY_TOTAL)).build());
		curio(builder("fleeting").setWeight(200).addModifier(Attributes.MOVEMENT_SPEED, mod(0.02, Operation.MULTIPLY_TOTAL)).build());
		curio(builder("hasty").setWeight(100).addModifier(Attributes.MOVEMENT_SPEED, mod(0.03, Operation.MULTIPLY_TOTAL)).build());
		curio(builder("quick").setWeight(100).addModifier(Attributes.MOVEMENT_SPEED, mod(0.04, Operation.MULTIPLY_TOTAL)).build());
		curio(builder("wild").setWeight(200).addModifier(Attributes.ATTACK_SPEED, mod(0.02, Operation.MULTIPLY_TOTAL)).build());
		curio(builder("rash").setWeight(200).addModifier(Attributes.ATTACK_SPEED, mod(0.04, Operation.MULTIPLY_TOTAL)).build());
		curio(builder("intrepid").setWeight(100).addModifier(Attributes.ATTACK_SPEED, mod(0.06, Operation.MULTIPLY_TOTAL)).build());
		curio(builder("violent").setWeight(100).addModifier(Attributes.ATTACK_SPEED, mod(0.08, Operation.MULTIPLY_TOTAL)).build());
		// FIXME remove this one before release
		curio(builder("debug").setWeight(0)
				.addModifier(Attributes.MAX_HEALTH, mod(1, Operation.ADDITION))
				.addModifier(Attributes.MOVEMENT_SPEED, mod(2.0, Operation.MULTIPLY_BASE))
				.build());
	}

	static {
		init();
	}
}
