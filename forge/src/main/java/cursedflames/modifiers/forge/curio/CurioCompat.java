package cursedflames.modifiers.forge.curio;

import cursedflames.modifiers.common.curio.ICurioProxy;
import cursedflames.modifiers.common.modifier.Modifier;
import cursedflames.modifiers.common.modifier.ModifierHandler;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
//import top.theillusivec4.curios.api.CuriosApi;
//import top.theillusivec4.curios.api.event.CurioChangeEvent;

public class CurioCompat implements ICurioProxy {
//	@SubscribeEvent
//	public void onCurioChange(CurioChangeEvent event) {
//		LivingEntity entity = event.getEntityLiving();
//		ItemStack from = event.getFrom();
//		ItemStack to = event.getTo();
//		String identifier = event.getIdentifier();
//		int slot = event.getSlotIndex();
//
//		Modifier modFrom = ModifierHandler.getModifier(from);
//		if (modFrom != null) {
//			ModifierHandler.removeCurioModifier(entity, modFrom, identifier, slot);
//		}
//
//		Modifier modifier = ModifierHandler.getModifier(to);
//		if (modifier == null) {
//			modifier = ModifierHandler.rollModifier(to, entity.world.random);
//			if (modifier == null) return;
//			ModifierHandler.setModifier(to, modifier);
//		}
//		ModifierHandler.applyCurioModifier(entity, modifier, identifier, slot);
//	}
//
//	@Override public boolean isModifiableCurio(ItemStack stack) {
//		Item item = stack.getItem();
//		// TODO we'll want to check configs to make sure there's at least one curio type in the tags that accepts modifiers
//		return !CuriosApi.getCuriosHelper().getCurioTags(item).isEmpty();
//	}
}
