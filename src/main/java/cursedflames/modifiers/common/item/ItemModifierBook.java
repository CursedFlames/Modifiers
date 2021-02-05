package cursedflames.modifiers.common.item;

import cursedflames.modifiers.ModifiersMod;
import cursedflames.modifiers.common.modifier.Modifier;
import cursedflames.modifiers.common.modifier.ModifierHandler;
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
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ItemModifierBook extends Item {
	public ItemModifierBook() {
		super(new Properties().rarity(Rarity.EPIC).group(ModifiersMod.GROUP_BOOKS));
	}

	@Override
	public boolean hasEffect(ItemStack stack) {
		if (!stack.hasTag()) return false;
		CompoundNBT tag = stack.getTag();
		return tag.contains(ModifierHandler.bookTagName) && !tag.getString(ModifierHandler.bookTagName).equals("modifiers:none");
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
		TranslationTextComponent base = (TranslationTextComponent) super.getDisplayName(stack);
		if (!stack.hasTag() || !stack.getTag().contains(ModifierHandler.bookTagName))
			return base;
		Modifier mod = Modifiers.modifiers.get(
				new ResourceLocation(stack.getTag().getString(ModifierHandler.bookTagName)));
		if (mod == null)
			return base;
		base.appendString(": ");
		base.append(mod.getFormattedName());
		return base;
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn,
							   List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		if (!stack.hasTag() || !stack.getTag().contains(ModifierHandler.bookTagName))
			return;
		Modifier mod = Modifiers.modifiers.get(
				new ResourceLocation(stack.getTag().getString(ModifierHandler.bookTagName)));
		if (mod == null)
			return;
		tooltip.addAll(mod.getInfoLines());
		tooltip.add(new TranslationTextComponent(this.getTranslationKey()+".tooltip.0"));
		tooltip.add(new TranslationTextComponent(this.getTranslationKey()+".tooltip.1"));
	}

	protected List<ItemStack> getStacks() {
		List<Modifier> modifiers = new ArrayList<>();
		modifiers.add(Modifiers.NONE);
		modifiers.addAll(Modifiers.curio_pool.modifiers);
		modifiers.addAll(Modifiers.tool_pool.modifiers);

		List<ItemStack> stacks = new ArrayList<>();
		for (Modifier mod : modifiers) {
			ItemStack stack = new ItemStack(this);
			CompoundNBT tag = stack.getOrCreateTag();
			tag.putString(ModifierHandler.bookTagName, mod.name.toString());
			stacks.add(stack);
		}
		return stacks;
	}
}
