package cursedflames.modifiers.common.modifier;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static net.minecraft.world.item.ItemStack.ATTRIBUTE_MODIFIER_FORMAT;

public class Modifier {
	public final ResourceLocation name;
	public final String debugName;
	public final int weight;
	public final ModifierType type;
	public final List<Pair<Attribute, AttributeModifierSupplier>> modifiers;

	private Modifier(ResourceLocation name, String debugName, int weight, ModifierType type, List<Pair<Attribute, AttributeModifierSupplier>> modifiers) {
		this.name = name;
		this.debugName = debugName;
		this.weight = weight;
		this.type = type;
		this.modifiers = modifiers;
	}

	public MutableComponent getFormattedName() {
		return Component.translatable("modifier."+name.getNamespace()+"."+name.getPath());
	}

	@Nullable
	private static MutableComponent getModifierDescription(Pair<Attribute, AttributeModifierSupplier> entry) {
		AttributeModifierSupplier modifier = entry.getValue();
		double d0 = modifier.amount;

		double d1;
		if (modifier.operation == AttributeModifier.Operation.ADDITION) {
			if (entry.getKey().equals(Attributes.KNOCKBACK_RESISTANCE)) {
				d1 = d0 * 10.0D;
			} else {
				d1 = d0;
			}
		} else {
			d1 = d0 * 100.0D;
		}

		if (d0 > 0.0D) {
			return Component.translatable("attribute.modifier.plus." + modifier.operation.toValue(), ATTRIBUTE_MODIFIER_FORMAT.format(d1),
					Component.translatable(entry.getKey().getDescriptionId())).withStyle(ChatFormatting.BLUE);
		} else if (d0 < 0.0D) {
			d1 = d1 * -1.0D;
			return Component.translatable("attribute.modifier.take." + modifier.operation.toValue(), ATTRIBUTE_MODIFIER_FORMAT.format(d1),
					Component.translatable(entry.getKey().getDescriptionId())).withStyle(ChatFormatting.RED);
		}
		return null;
	}

	public List<MutableComponent> getInfoLines() {
		// TODO: probably want a "no effect" description rather than just not showing a description for modifiers with no effect
		//       also maybe "no modifier" for None, idk
		List<MutableComponent> lines = new ArrayList<>();
		int size = modifiers.size();
		if (size < 1) {
			return lines;
		}
		if (size == 1) {
			MutableComponent description = getModifierDescription(modifiers.get(0));
			if (description == null) return lines;
			lines.add(getFormattedName().append(": ").withStyle(ChatFormatting.GRAY).append(description));
		} else {
			lines.add(getFormattedName().append(":").withStyle(ChatFormatting.GRAY));
			for (Pair<Attribute, AttributeModifierSupplier> entry : modifiers) {
				MutableComponent description = getModifierDescription(entry);
				if (description != null) {
					lines.add(description);
				}
			}
			if (lines.size() == 1) {
				lines.clear();
			}
		}
		return lines;
	}

	// TODO might want to distinguish between curio slots and armor slots in future, too
	public enum ModifierType {
		EQUIPPED, HELD, BOTH
	}

	public static class ModifierBuilder {
		int weight = 100;
		final ResourceLocation name;
		final String debugName;
		final ModifierType type;
		List<Pair<Attribute, AttributeModifierSupplier>> modifiers = new ArrayList<>();

		public ModifierBuilder(ResourceLocation name, String debugName, ModifierType type) {
			this.name = name;
			this.debugName = debugName;
			this.type = type;
		}

		public ModifierBuilder setWeight(int weight) {
			this.weight = Math.max(0, weight);
			return this;
		}

		public ModifierBuilder addModifier(Attribute attribute, AttributeModifierSupplier modifier) {
			modifiers.add(new ImmutablePair<>(attribute, modifier));
			return this;
		}

		public Modifier build() {
			return new Modifier(name, debugName, weight, type, modifiers);
		}
	}

	public static class AttributeModifierSupplier {
		public final double amount;
		public final AttributeModifier.Operation operation;

		public AttributeModifierSupplier(double amount, AttributeModifier.Operation operation) {
			this.amount = amount;
			this.operation = operation;
		}

		public AttributeModifier getAttributeModifier(UUID id, String name) {
			return new AttributeModifier(id, name, amount, operation);
		}
	}
}
