package cursedflames.modifiers.common.modifier.curio;

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
//		PlayerEntity player, ItemStack stack, String identifier, int slot
		LivingEntity entity = event.getEntityLiving();
		ItemStack from = event.getFrom();
		ItemStack to = event.getTo();
		ModifierHandlerCurio.genCurioModifier(to); // does nothing if it already has a modifier
		String identifier = event.getTypeIdentifier();
		int slot = event.getSlotIndex();
		IModifierCurio modFrom = ModifierHandlerCurio.getCurioModifier(from);
		IModifierCurio modTo = ModifierHandlerCurio.getCurioModifier(to);
		if (modFrom != null) {
//			ModifiersMod.logger.info(modFrom);
			modFrom.removeModifier(entity, from, identifier, slot);
		}
		if (modTo != null) {
//			ModifiersMod.logger.info(modTo);
			modTo.applyModifier(entity, to, identifier, slot);
		}
		
	}
	
	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void onTooltip(ItemTooltipEvent event) {
		ItemStack stack = event.getItemStack();
		IModifierCurio mod = ModifierHandlerCurio.getCurioModifier(stack);
		if (mod != null && mod != ModifierCurioRegistry.NONE) {
			ResourceLocation loc = mod.getRegistryName();
			event.getToolTip().add(new TranslationTextComponent(
					ModifierHandlerCurio.getInfoTranslationKey(mod))
					.setStyle(new Style().setColor(TextFormatting.BLUE)));
			
			ITextComponent name = event.getToolTip().get(0);
			TranslationTextComponent prefix = new TranslationTextComponent(
					ModifierHandlerCurio.getTranslationKey(mod));
			prefix.setStyle(name.getStyle().createDeepCopy());
			prefix.appendSibling(new StringTextComponent(" ")).appendSibling(name);
//			String colorCode = "";
//			while (name.length()>1&&name.charAt(0)=='\u00A7') {
//				colorCode += name.substring(0, 2);
//				name = name.substring(2);
//			}
////			if (colorCode.length() == 0) {
////				colorCode = "\u00A70";
////			}
			event.getToolTip().set(0, prefix);
		}
		// you better not ever leave this uncommented in builds, future self
//		CompoundNBT tag = stack.getTag();
//		if (tag != null) {
//			event.getToolTip().add(new StringTextComponent(tag.toString()));
//		}
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
//			
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
