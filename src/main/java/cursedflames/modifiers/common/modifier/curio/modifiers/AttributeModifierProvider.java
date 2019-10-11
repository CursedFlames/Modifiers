package cursedflames.modifiers.common.modifier.curio.modifiers;

import java.util.UUID;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;

public class AttributeModifierProvider {
	public static final long COMMON_SEGMENT = (0x7a6ca76c) << 32;
	public final String attributeName;
	public final String name;
	public final double amount;
	public final Operation operation;
	
	public AttributeModifierProvider(String attributeName, String name, double amount, Operation operation) {
		this.attributeName = attributeName;
		this.name = name;
		this.amount = amount;
		this.operation = operation;
	}
	
	public UUID getUUID(String identifier, int slot) {
		// There's probably a smarter way to do deterministic UUIDs but whatever :shrug:
		long second = (((long) this.attributeName.hashCode())<<32) | ((long) identifier.hashCode());
		long first = COMMON_SEGMENT | ((long) slot);
		return new UUID(first, second);
	}
	
	public AttributeModifier getModifier(String identifier, int slot) {
		return new AttributeModifier(getUUID(identifier, slot), name, amount, operation);
	}
}
