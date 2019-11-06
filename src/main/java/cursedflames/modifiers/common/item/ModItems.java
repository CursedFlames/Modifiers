package cursedflames.modifiers.common.item;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import cursedflames.modifiers.common.ModifiersMod;
import cursedflames.modifiers.common.modifier.Modifier;
import cursedflames.modifiers.common.modifier.Modifiers;
import cursedflames.modifiers.common.modifier.curio.ModifierHandlerCurio;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;

public class ModItems { // TODO update book textures to match 1.14 textures
	public static Item modifier_book_curio;
	public static Item modifier_book_tool;
	
	// TODO sort cre-tab/JEI
	public static final ItemGroup GROUP_MOD_CURIO = new ItemGroup(ModifiersMod.MODID+"_curio") {
		@Override
		@OnlyIn(Dist.CLIENT)
		public ItemStack createIcon() {
			return new ItemStack(modifier_book_curio);
		}
	};
//	public static final ItemGroup GROUP_MOD_TOOL = new ItemGroup(ModifiersMod.MODID+"_tool") {
//		@Override
//		@OnlyIn(Dist.CLIENT)
//		public ItemStack createIcon() {
//			return new ItemStack(modifier_book_tool);
//		}
//	};
	
	public static void registerItems(final RegistryEvent.Register<Item> event) {
		modifier_book_curio = new ItemModifierBook("modifier_book_curio",
				new Properties().group(GROUP_MOD_CURIO)) {
			@Override
			public ITextComponent getDisplayName(ItemStack stack) {
				ITextComponent base = super.getDisplayName(stack);
				if (!stack.hasTag() || !stack.getTag().contains("bookMod"))
					return base;
				Modifier mod = Modifiers.modifiers.get(
						new ResourceLocation(stack.getTag().getString("bookMod")));
				if (mod == null)
					return base;
				base.appendSibling(new StringTextComponent(": "));
				base.appendSibling(new TranslationTextComponent(ModifierHandlerCurio.getTranslationKey(mod)));
				return base;
			}
			
			@Override
			public void addInformation(ItemStack stack, @Nullable World worldIn,
					List<ITextComponent> tooltip, ITooltipFlag flagIn) {
				if (!stack.hasTag() || !stack.getTag().contains("bookMod"))
					return;
				Modifier mod = Modifiers.modifiers.get(
						new ResourceLocation(stack.getTag().getString("bookMod")));
				if (mod == null)
					return;
				tooltip.add(new TranslationTextComponent(ModifierHandlerCurio.getInfoTranslationKey(mod)));
				tooltip.add(new TranslationTextComponent(this.getTranslationKey()+".tooltip.0"));
				tooltip.add(new TranslationTextComponent(this.getTranslationKey()+".tooltip.1"));
			}
			
			@Override
			protected List<ItemStack> getStacks() {
				List<ItemStack> stacks = new ArrayList<>();
				for (Modifier mod : Modifiers.modifiers.values()) {
					ItemStack stack = new ItemStack(this);
					CompoundNBT tag = stack.getOrCreateTag();
					tag.putString("bookMod", mod.name.toString());
					stacks.add(stack);
				}
				return stacks;
			}
		};
		
//		modifier_book_tool = new ItemModifierBook("modifier_book_tool",
//				new Properties().group(GROUP_MOD_TOOL)) {
//			@Override
//			public ITextComponent getDisplayName(ItemStack stack) {
//				ITextComponent base = super.getDisplayName(stack);
//				IForgeRegistryEntry mod = getModifier(stack);
//				if (mod == null)
//					return base;
//				base.appendSibling(new StringTextComponent(": "));
//				base.appendSibling(component);
//				return base;
//			}
//			
//			@Override
//			public void addInformation(ItemStack stack, @Nullable World worldIn,
//					List<ITextComponent> tooltip, ITooltipFlag flagIn) {
//				
//			}
//			
//			@Override
//			protected List<ItemStack> getStacks() {
//				return Arrays.asList(new ItemStack(this)); //FIXME
//			}
//		};
		
		event.getRegistry().registerAll(modifier_book_curio/*, modifier_book_tool*/);
	}
}
