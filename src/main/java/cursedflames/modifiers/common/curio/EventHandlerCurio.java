package cursedflames.modifiers.common.curio;

import cursedflames.modifiers.ModifiersMod;
import cursedflames.modifiers.common.modifier.Modifier;
import cursedflames.modifiers.common.modifier.ModifierHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.event.CurioChangeEvent;

public class EventHandlerCurio {
	@SubscribeEvent
	public static void onCurioChange(CurioChangeEvent event) {
		LivingEntity entity = event.getEntityLiving();
		ItemStack from = event.getFrom();
		ItemStack to = event.getTo();
		String identifier = event.getIdentifier();
		int slot = event.getSlotIndex();

		Modifier modFrom = ModifierHandler.getModifier(from);
		if (modFrom != null) {
			ModifierHandler.removeCurioModifier(entity, modFrom, identifier, slot);
		}

		Modifier modifier = ModifierHandler.getModifier(to);
		if (modifier == null) {
			modifier = ModifierHandler.rollModifier(to, entity.world.rand);
			if (modifier == null) return;
			ModifierHandler.setModifier(to, modifier);
		}
		ModifierHandler.applyCurioModifier(entity, modifier, identifier, slot);
	}
}
