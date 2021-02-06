package cursedflames.modifiers.common.modifier;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static net.minecraft.item.ItemStack.MODIFIER_FORMAT;

public class Modifier {
	public final Identifier name;
	public final String debugName;
	public final int weight;
	public final ModifierType type;
	public final List<Pair<EntityAttribute, AttributeModifierSupplier>> modifiers;

	private Modifier(Identifier name, String debugName, int weight, ModifierType type, List<Pair<EntityAttribute, AttributeModifierSupplier>> modifiers) {
		this.name = name;
		this.debugName = debugName;
		this.weight = weight;
		this.type = type;
		this.modifiers = modifiers;
	}

	public TranslatableText getFormattedName() {
		return new TranslatableText("modifier."+name.getNamespace()+"."+name.getPath());
	}

	@Nullable
	private static MutableText getModifierDescription(Pair<EntityAttribute, AttributeModifierSupplier> entry) {
		AttributeModifierSupplier modifier = entry.getValue();
		double d0 = modifier.amount;

		double d1;
		if (modifier.operation == EntityAttributeModifier.Operation.ADDITION) {
			if (entry.getKey().equals(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE)) {
				d1 = d0 * 10.0D;
			} else {
				d1 = d0;
			}
		} else {
			d1 = d0 * 100.0D;
		}

		if (d0 > 0.0D) {
			return new TranslatableText("attribute.modifier.plus." + modifier.operation.getId(), MODIFIER_FORMAT.format(d1),
					new TranslatableText(entry.getKey().getTranslationKey())).formatted(Formatting.BLUE);
		} else if (d0 < 0.0D) {
			d1 = d1 * -1.0D;
			return new TranslatableText("attribute.modifier.take." + modifier.operation.getId(), MODIFIER_FORMAT.format(d1),
					new TranslatableText(entry.getKey().getTranslationKey())).formatted(Formatting.RED);
		}
		return null;
	}

	public List<MutableText> getInfoLines() {
		// TODO: probably want a "no effect" description rather than just not showing a description for modifiers with no effect
		//       also maybe "no modifier" for None, idk
		List<MutableText> lines = new ArrayList<>();
		int size = modifiers.size();
		if (size < 1) {
			return lines;
		}
		if (size == 1) {
			MutableText description = getModifierDescription(modifiers.get(0));
			if (description == null) return lines;
			lines.add(getFormattedName().append(": ").formatted(Formatting.GRAY).append(description));
		} else {
			lines.add(getFormattedName().append(":").formatted(Formatting.GRAY));
			for (Pair<EntityAttribute, AttributeModifierSupplier> entry : modifiers) {
				MutableText description = getModifierDescription(entry);
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
		final Identifier name;
		final String debugName;
		final ModifierType type;
		List<Pair<EntityAttribute, AttributeModifierSupplier>> modifiers = new ArrayList<>();

		public ModifierBuilder(Identifier name, String debugName, ModifierType type) {
			this.name = name;
			this.debugName = debugName;
			this.type = type;
		}

		public ModifierBuilder setWeight(int weight) {
			this.weight = Math.max(0, weight);
			return this;
		}

		public ModifierBuilder addModifier(EntityAttribute attribute, AttributeModifierSupplier modifier) {
			modifiers.add(new ImmutablePair<>(attribute, modifier));
			return this;
		}

		public Modifier build() {
			return new Modifier(name, debugName, weight, type, modifiers);
		}
	}

	public static class AttributeModifierSupplier {
		public final double amount;
		public final EntityAttributeModifier.Operation operation;

		public AttributeModifierSupplier(double amount, EntityAttributeModifier.Operation operation) {
			this.amount = amount;
			this.operation = operation;
		}

		public EntityAttributeModifier getAttributeModifier(UUID id, String name) {
			return new EntityAttributeModifier(id, name, amount, operation);
		}
	}
}
