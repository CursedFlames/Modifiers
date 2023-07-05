package cursedflames.modifiers.common.item;

import cursedflames.modifiers.ModifiersMod;
import cursedflames.modifiers.common.modifier.Modifier;
import cursedflames.modifiers.common.modifier.ModifierHandler;
import cursedflames.modifiers.common.modifier.Modifiers;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Rarity;
import net.minecraft.core.NonNullList;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ItemModifierBook extends Item {
	public ItemModifierBook() {
		// TODO item group
		super(new Properties().rarity(Rarity.EPIC));
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		if (!stack.hasTag()) return false;
		CompoundTag tag = stack.getTag();
		return tag.contains(ModifierHandler.bookTagName) && !tag.getString(ModifierHandler.bookTagName).equals("modifiers:none");
	}

	@Override
	public Component getName(ItemStack stack) {
		var base = super.getName(stack);
		if (!stack.hasTag() || !stack.getTag().contains(ModifierHandler.bookTagName))
			return base;
		Modifier mod = Modifiers.modifiers.get(
				new ResourceLocation(stack.getTag().getString(ModifierHandler.bookTagName)));
		if (mod == null)
			return base;
		return Component.translatable("misc.modifiers.modifier_prefix").append(mod.getFormattedName());
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn,
							   List<Component> tooltip, TooltipFlag flagIn) {
		if (stack.hasTag() && stack.getTag().contains(ModifierHandler.bookTagName)) {
			Modifier mod = Modifiers.modifiers.get(
					new ResourceLocation(stack.getTag().getString(ModifierHandler.bookTagName)));
			if (mod != null) {
				tooltip.addAll(mod.getInfoLines());
				tooltip.add(Component.translatable(this.getDescriptionId()+".tooltip.0"));
				tooltip.add(Component.translatable(this.getDescriptionId()+".tooltip.1"));
				return;
			}
		}
		tooltip.add(Component.translatable(this.getDescriptionId()+".tooltip.invalid"));
	}

	public List<ItemStack> getStacksForCreativeTab() {
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
