package cursedflames.modifiers.common.modifier.curio;

import java.util.Set;

import cursedflames.modifiers.common.ModifierHandler;
import cursedflames.modifiers.common.ModifiersMod;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.CuriosAPI;
import top.theillusivec4.curios.api.event.LivingCurioChangeEvent;

public class EventHandlerCurio {
	@SubscribeEvent
	public static void onCurioChange(LivingCurioChangeEvent event) {
//		PlayerEntity player, ItemStack stack, String identifier, int slot
		LivingEntity entity = event.getEntityLiving();
		ItemStack from = event.getFrom();
		ItemStack to = event.getTo();
		String identifier = event.getTypeIdentifier();
		int slot = event.getSlotIndex();
		IModifierCurio modFrom = ModifierHandlerCurio.getCurioModifier(from);
		IModifierCurio modTo = ModifierHandlerCurio.getCurioModifier(to);
		if (modFrom != null) {
			modFrom.removeModifier(entity, from, identifier, slot);
		}
		if (modTo != null) {
			modTo.applyModifier(entity, to, identifier, slot);
		}
		
	}
	
	@SubscribeEvent
	public static void onTooltip(ItemTooltipEvent event) {
		ItemStack stack = event.getItemStack();
		CompoundNBT tag = stack.getTag();
		if (tag != null) {
			event.getToolTip().add(new StringTextComponent(tag.toString()));
		}
	}
	
	@SubscribeEvent(priority=EventPriority.LOW)
	public static void onAttachCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
//		ModifiersMod.logger.info("itemstack");
		ItemStack stack = event.getObject();
		if (ModifierHandler.canHaveModifier(stack)) {
//			ModifiersMod.logger.info("found item " + stack.getItem().getName());
			Item item = stack.getItem();
			Set<String> tags = CuriosAPI.getCurioTags(stack.getItem());
			
			if (!tags.isEmpty()) {
				ModifiersMod.logger.info("has tag");
				ModifierHandlerCurio.genCurioModifier(stack);
			} else {
//				ModifiersMod.logger.info("no tag");
				// TODO tool modifiers
			}
		}
	}
}
