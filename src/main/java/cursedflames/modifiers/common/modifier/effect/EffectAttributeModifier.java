package cursedflames.modifiers.common.modifier.effect;

import java.util.Map.Entry;
import java.util.UUID;

import com.google.common.collect.Multimap;

import cursedflames.modifiers.common.modifier.curio.ModifierBase;
import cursedflames.modifiers.common.modifier.curio.modifiers.AttributeModifierProvider;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.item.ItemStack;

public class EffectAttributeModifier extends ModifierBase {
	public final Multimap<String, AttributeModifierProvider> attributes;
	
	public EffectAttributeModifier(Multimap<String, AttributeModifierProvider> attributes) {
		this.attributes = attributes;
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
