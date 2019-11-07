package cursedflames.modifiers.common.modifier.curio;

import cursedflames.modifiers.common.ModifierHandler;
import cursedflames.modifiers.common.ModifiersMod;
import cursedflames.modifiers.common.modifier.Modifier;
import cursedflames.modifiers.common.modifier.Modifiers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.event.LivingCurioChangeEvent;

public class EventHandlerCurio {
	@SubscribeEvent
	public static void onCurioChange(LivingCurioChangeEvent event) {
		// TODO might also want to call removeModifier on player leave? in case of missing modifiers
		LivingEntity entity = event.getEntityLiving();
		ItemStack from = event.getFrom();
		ItemStack to = event.getTo();
		String identifier = event.getTypeIdentifier();
		int slot = event.getSlotIndex();

		if (from.hasTag() && from.getTag().contains("modState")) {
//			ModifiersMod.logger.info("ASDFQWERT");
			from.getTag().putInt("modState", 0);
			from.getTag().remove("modState");
		}
		
		Modifier modFrom = ModifierHandler.getCurioModifier(from);
		boolean allowInSlot = ModifierHandler.allowModifierInSlot(identifier);
		if (allowInSlot) {
			ModifierHandler.genCurioModifier(to); // does nothing if it already has a modifier
		}
		Modifier modTo = ModifierHandler.getCurioModifier(to);
		
		if (modFrom != null) {
//			ModifiersMod.logger.info(modFrom);
			modFrom.removeModifier(entity, from, identifier, slot);
		}
		if (modTo != null) {
			if (allowInSlot) {
//				ModifiersMod.logger.info(modTo);
				modTo.applyModifier(entity, to, identifier, slot);
//				to.getTag().putInt("modState", 2);
			} else {
//				to.getTag().putInt("modState", 1);
			}
		}
		
	}
	
	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void onTooltip(ItemTooltipEvent event) {
		// TODO indicate valid slots and whether active
		ItemStack stack = event.getItemStack();
		Modifier mod = ModifierHandler.getCurioModifier(stack);
		if (mod != null && mod != Modifiers.NONE) {
			ResourceLocation loc = mod.name;
			ITextComponent modInfo = new TranslationTextComponent(
					ModifierHandler.getInfoTranslationKey(mod))
					.setStyle(new Style().setColor(TextFormatting.BLUE));
			int modState = stack.getTag().getInt("modState");
			if (modState != 0) {
				modInfo.appendSibling(new StringTextComponent(" ["));
				if (modState == 1) {
					modInfo.appendSibling(new TranslationTextComponent(
							ModifiersMod.MODID+".curio.wrong_slot")
							.setStyle(new Style().setColor(TextFormatting.RED)));
				} else {
					modInfo.appendSibling(new TranslationTextComponent(
							ModifiersMod.MODID+".curio.active")
							.setStyle(new Style().setColor(TextFormatting.GREEN)));
				}
				modInfo.appendSibling(new StringTextComponent("]"));
			}
			event.getToolTip().add(modInfo);
			
			ITextComponent name = event.getToolTip().get(0);
			TranslationTextComponent prefix = new TranslationTextComponent(
					ModifierHandler.getTranslationKey(mod));
			prefix.setStyle(name.getStyle().createDeepCopy());
			prefix.appendSibling(new StringTextComponent(" ")).appendSibling(name);
			
			event.getToolTip().set(0, prefix);
		}
		// you better not ever leave this uncommented in builds, future self
		// FIXME testing only
		CompoundNBT tag = stack.getTag();
		if (tag != null) {
			event.getToolTip().add(new StringTextComponent(tag.toString()));
		}
	}
	// disabled due to issues with items in JEI and creative tabs
//	@SubscribeEvent(priority=EventPriority.LOW)
//	public static void onAttachCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
////		ModifiersMod.logger.info("itemstack");
//		ItemStack stack = event.getObject();
//		if (ModifierHandler.canHaveModifier(stack)) {
////			ModifiersMod.logger.info("found item " + stack.getItem().getName());
//			Item item = stack.getItem();
//			Set<String> tags = CuriosAPI.getCurioTags(stack.getItem());
//			// TODO do check against slot type
//			if (!tags.isEmpty()) {
//				ModifiersMod.logger.info("has tag");
//				ModifierHandlerCurio.genCurioModifier(stack);
//			} else {
////				ModifiersMod.logger.info("no tag");
//				// TODO tool modifiers
//			}
//		}
//	}
	
//	@SubscribeEvent(priority=EventPriority.HIGHEST)
//	public static void onAboutToStart(FMLServerAboutToStartEvent event) {
//		ModifiersMod.logger.info("server start ASDFASDFASDF");
//	}
//	
//	@SubscribeEvent(priority=EventPriority.LOWEST)
//	public static void onStarted(FMLServerStartedEvent event) {
//		ModifiersMod.logger.info("server started ASDFASDFASDF");
//	}
}
