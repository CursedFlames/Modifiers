package cursedflames.modifiers.common.modifier.curio.modifiers;

import java.util.Map.Entry;
import java.util.UUID;

import com.google.common.collect.Multimap;

import cursedflames.modifiers.common.ModifiersMod;
import cursedflames.modifiers.common.modifier.curio.ModifierBase;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.item.ItemStack;

public class ModifierAttributeModifier extends ModifierBase {
	public final Multimap<String, AttributeModifierProvider> attributes;
	public final int weight;
	
	public ModifierAttributeModifier(Multimap<String, AttributeModifierProvider> attributes, int weight) {
		this.attributes = attributes;
		this.weight = weight;
	}
	
	@Override
	public int getWeight() {
		return weight;
	}
	
	@Override
	public void applyModifier(LivingEntity player, ItemStack stack, String identifier, int slot) {
		AbstractAttributeMap playerAttributes = player.getAttributes();
		
		for (Entry<String, AttributeModifierProvider> entry : attributes.entries()) {
			IAttributeInstance attribute = playerAttributes.getAttributeInstanceByName(entry.getKey());
			AttributeModifierProvider mod = entry.getValue();
			AttributeModifier modifier = mod.getModifier(identifier, slot);
			if (!attribute.hasModifier(modifier)) {
				attribute.applyModifier(modifier);
			}
		}
	}
	
	@Override
	public void removeModifier(LivingEntity player, ItemStack stack, String identifier, int slot) {
		AbstractAttributeMap playerAttributes = player.getAttributes();
		
		for (Entry<String, AttributeModifierProvider> entry : attributes.entries()) {
			IAttributeInstance attribute = playerAttributes.getAttributeInstanceByName(entry.getKey());
			AttributeModifierProvider mod = entry.getValue();
			UUID id = mod.getUUID(identifier, slot);
			AttributeModifier modifier = attribute.getModifier(id);
			if (modifier != null) {
				attribute.removeModifier(modifier);
			}
		}
	}
}
