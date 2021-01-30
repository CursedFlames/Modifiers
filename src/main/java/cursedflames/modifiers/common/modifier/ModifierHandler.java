package cursedflames.modifiers.common.modifier;

import cursedflames.modifiers.ModifiersMod;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.UUID;

public class ModifierHandler {
	public static final long COMMON_SEGMENT_CURIO = (0x7a6ca76cL) << 32;
	public static final long COMMON_SEGMENT_EQUIPMENT = 0x9225d5c4fd8d434bL;
	public static final String tagName = "itemModifier";

	public static boolean canHaveModifiers(ItemStack stack) {
		return !stack.isEmpty() && stack.getMaxStackSize() <= 1 /* && stack.getItem() != ModItems.modifier_book*/;
	}

	@Nullable public static Modifier rollModifier(ItemStack stack, Random random) {
		if (!canHaveModifiers(stack)) return null;
		// TODO check all pools
		if (Modifiers.curio_pool.isApplicable.test(stack)) {
			return Modifiers.curio_pool.roll(random);
		}
		return null;
	}

	public static void removeModifier(ItemStack stack) {
		if (stack.hasTag()) {
			stack.getTag().remove(tagName);
		}
	}

	public static void setModifier(ItemStack stack, Modifier modifier) {
		CompoundNBT tag = stack.getOrCreateTag();
		tag.putString(tagName, modifier.name.toString());
	}

	public static boolean hasModifier(ItemStack stack) {
		CompoundNBT tag = stack.getTag();
		return tag != null && tag.contains(tagName);
	}

	@Nullable public static Modifier getModifier(ItemStack stack) {
		CompoundNBT tag = stack.getTag();
		if (tag == null) return null;
		if (!tag.contains(tagName)) return null;
		return Modifiers.modifiers.get(new ResourceLocation(tag.getString(tagName)));
	}

	public static UUID getCurioUuid(String identifier, int slot, int attributeIndex) {
		// There's probably a smarter way to do deterministic UUIDs but whatever :shrug:
		long second = (((long) attributeIndex)<<32) | ((long) identifier.hashCode());
		long first = COMMON_SEGMENT_CURIO | (((long) slot) << 32);
		return new UUID(first, second);
	}

	public static void applyCurioModifier(LivingEntity entity, Modifier modifier, String slotIdentifier, int index) {
		for (int i = 0; i < modifier.modifiers.size(); i++) {
			Pair<Attribute, Modifier.AttributeModifierSupplier> entry = modifier.modifiers.get(i);
			UUID id = getCurioUuid(slotIdentifier, index, i);
			ModifiableAttributeInstance instance = entity.getAttribute(entry.getKey());
			if (instance != null) {
				instance.applyNonPersistentModifier(entry.getValue().getAttributeModifier(id, "curio_modifier_"+modifier.debugName));
			}
		}
	}

	public static void removeCurioModifier(LivingEntity entity, Modifier modifier, String slotIdentifier, int index) {
		for (int i = 0; i < modifier.modifiers.size(); i++) {
			Pair<Attribute, Modifier.AttributeModifierSupplier> entry = modifier.modifiers.get(i);
			UUID id = getCurioUuid(slotIdentifier, index, i);
			ModifiableAttributeInstance instance = entity.getAttribute(entry.getKey());
			if (instance != null) {
				instance.removeModifier(id);
			}
		}
	}

	public static UUID getEquipmentUuid(EquipmentSlotType slot, int attributeIndex) {
		long second = (((long) attributeIndex)<<32) | ((long) slot.hashCode());
		return new UUID(COMMON_SEGMENT_EQUIPMENT, second);
	}

	public static void applyEquipmentModifier(LivingEntity entity, Modifier modifier, EquipmentSlotType type) {
		for (int i = 0; i < modifier.modifiers.size(); i++) {
			Pair<Attribute, Modifier.AttributeModifierSupplier> entry = modifier.modifiers.get(i);
			UUID id = getEquipmentUuid(type, i);
			ModifiableAttributeInstance instance = entity.getAttribute(entry.getKey());
			if (instance != null) {
				instance.applyNonPersistentModifier(entry.getValue().getAttributeModifier(id, "equipment_modifier_"+modifier.debugName));
			}
		}
	}

	public static void removeEquipmentModifier(LivingEntity entity, Modifier modifier, EquipmentSlotType type) {
		for (int i = 0; i < modifier.modifiers.size(); i++) {
			Pair<Attribute, Modifier.AttributeModifierSupplier> entry = modifier.modifiers.get(i);
			UUID id = getEquipmentUuid(type, i);
			ModifiableAttributeInstance instance = entity.getAttribute(entry.getKey());
			if (instance != null) {
				instance.removeModifier(id);
			}
		}
	}
}
