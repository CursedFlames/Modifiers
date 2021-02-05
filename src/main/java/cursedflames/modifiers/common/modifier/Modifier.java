package cursedflames.modifiers.common.modifier;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static net.minecraft.item.ItemStack.DECIMALFORMAT;

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

	public TranslationTextComponent getFormattedName() {
		return new TranslationTextComponent("modifier."+name.getNamespace()+"."+name.getPath());
	}

	@Nullable
	private static IFormattableTextComponent getModifierDescription(Pair<Attribute, AttributeModifierSupplier> entry) {
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
			return new TranslationTextComponent("attribute.modifier.plus." + modifier.operation.getId(), DECIMALFORMAT.format(d1),
					new TranslationTextComponent(entry.getKey().getAttributeName())).mergeStyle(TextFormatting.BLUE);
		} else if (d0 < 0.0D) {
			d1 = d1 * -1.0D;
			return new TranslationTextComponent("attribute.modifier.take." + modifier.operation.getId(), DECIMALFORMAT.format(d1),
					new TranslationTextComponent(entry.getKey().getAttributeName())).mergeStyle(TextFormatting.RED);
		}
		return null;
	}

	public List<IFormattableTextComponent> getInfoLines() {
		// TODO: probably want a "no effect" description rather than just not showing a description for modifiers with no effect
		//       also maybe "no modifier" for None, idk
		List<IFormattableTextComponent> lines = new ArrayList<>();
		int size = modifiers.size();
		if (size < 1) {
			return lines;
		}
		if (size == 1) {
			IFormattableTextComponent description = getModifierDescription(modifiers.get(0));
			if (description == null) return lines;
			lines.add(getFormattedName().appendString(": ").mergeStyle(TextFormatting.GRAY).append(description));
		} else {
			lines.add(getFormattedName().appendString(":").mergeStyle(TextFormatting.GRAY));
			for (Pair<Attribute, AttributeModifierSupplier> entry : modifiers) {
				IFormattableTextComponent description = getModifierDescription(entry);
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
