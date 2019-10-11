package cursedflames.modifiers.common.item;

import java.util.List;

import cursedflames.modifiers.common.ModifiersMod;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;

public abstract class ItemModifierBook extends Item {
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
	
	abstract protected List<ItemStack> getStacks();
	
	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		// FIXME make variants display in JEI
		if (this.isInGroup(group)) {
			items.addAll(getStacks());
		}
	}
}
