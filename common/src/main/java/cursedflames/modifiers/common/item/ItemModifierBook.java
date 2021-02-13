package cursedflames.modifiers.common.item;

import cursedflames.modifiers.ModifiersMod;
import cursedflames.modifiers.common.modifier.Modifier;
import cursedflames.modifiers.common.modifier.ModifierHandler;
import cursedflames.modifiers.common.modifier.Modifiers;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ItemModifierBook extends Item {
	public ItemModifierBook() {
		super(new Settings().rarity(Rarity.EPIC).group(ModifiersMod.GROUP_BOOKS));
	}

	@Override
	public boolean hasGlint(ItemStack stack) {
		if (!stack.hasTag()) return false;
		CompoundTag tag = stack.getTag();
		return tag.contains(ModifierHandler.bookTagName) && !tag.getString(ModifierHandler.bookTagName).equals("modifiers:none");
	}

	@Override
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> items) {
		// FIXME make variants display in JEI
		if (this.isIn(group)) {
			items.addAll(getStacks());
		}
	}

	@Override
	public Text getName(ItemStack stack) {
		TranslatableText base = (TranslatableText) super.getName(stack);
		if (!stack.hasTag() || !stack.getTag().contains(ModifierHandler.bookTagName))
			return base;
		Modifier mod = Modifiers.modifiers.get(
				new Identifier(stack.getTag().getString(ModifierHandler.bookTagName)));
		if (mod == null)
			return base;
		return new TranslatableText("misc.modifiers.modifier_prefix").append(mod.getFormattedName());
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World worldIn,
							   List<Text> tooltip, TooltipContext flagIn) {
		if (stack.hasTag() && stack.getTag().contains(ModifierHandler.bookTagName)) {
			Modifier mod = Modifiers.modifiers.get(
					new Identifier(stack.getTag().getString(ModifierHandler.bookTagName)));
			if (mod != null) {
				tooltip.addAll(mod.getInfoLines());
				tooltip.add(new TranslatableText(this.getTranslationKey()+".tooltip.0"));
				tooltip.add(new TranslatableText(this.getTranslationKey()+".tooltip.1"));
				return;
			}
		}
		tooltip.add(new TranslatableText(this.getTranslationKey()+".tooltip.invalid"));
	}

	protected List<ItemStack> getStacks() {
		List<Modifier> modifiers = new ArrayList<>();
		modifiers.add(Modifiers.NONE);
		modifiers.addAll(Modifiers.curio_pool.modifiers);
		modifiers.addAll(Modifiers.tool_pool.modifiers);

		List<ItemStack> stacks = new ArrayList<>();
		for (Modifier mod : modifiers) {
			ItemStack stack = new ItemStack(this);
			CompoundTag tag = stack.getOrCreateTag();
			tag.putString(ModifierHandler.bookTagName, mod.name.toString());
			stacks.add(stack);
		}
		return stacks;
	}
}
