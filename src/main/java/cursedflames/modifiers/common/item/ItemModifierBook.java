package cursedflames.modifiers.common.item;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import cursedflames.modifiers.common.ModifierHandler;
import cursedflames.modifiers.common.ModifiersMod;
import cursedflames.modifiers.common.modifier.Modifier;
import cursedflames.modifiers.common.modifier.Modifiers;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class ItemModifierBook extends Item {
	public ItemModifierBook(String name, Properties properties) {
		super(properties.maxStackSize(1).rarity(Rarity.EPIC));
		setRegistryName(ModifiersMod.MODID, name);
	}

	@Override
	public boolean hasEffect(ItemStack stack) {
		// TODO quark-style enchant colors?
		if (!stack.hasTag()) return false;
		CompoundNBT tag = stack.getTag();
		return tag.contains("bookMod") && !tag.getString("bookMod").equals("modifiers:none");
	}
	
	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		// FIXME make variants display in JEI
		if (this.isInGroup(group)) {
			items.addAll(getStacks());
		}
	}
	
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
		base.appendSibling(new TranslationTextComponent(ModifierHandler.getTranslationKey(mod)));
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
		tooltip.add(new TranslationTextComponent(ModifierHandler.getInfoTranslationKey(mod)));
		tooltip.add(new TranslationTextComponent(this.getTranslationKey()+".tooltip.0"));
		tooltip.add(new TranslationTextComponent(this.getTranslationKey()+".tooltip.1"));
	}
	
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
}
