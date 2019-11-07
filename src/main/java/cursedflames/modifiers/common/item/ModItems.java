package cursedflames.modifiers.common.item;

import cursedflames.modifiers.common.ModifiersMod;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;

public class ModItems { // TODO update book texture to match 1.14 textures
	public static Item modifier_book;
	
	// TODO sort cre-tab/JEI
	public static final ItemGroup GROUP_BOOKS = new ItemGroup(ModifiersMod.MODID+"_books") {
		@Override
		@OnlyIn(Dist.CLIENT)
		public ItemStack createIcon() {
			return new ItemStack(modifier_book);
		}
	};
	
	public static void registerItems(final RegistryEvent.Register<Item> event) {
		modifier_book = new ItemModifierBook("modifier_book",
				new Properties().group(GROUP_BOOKS));
		
		event.getRegistry().register(modifier_book);
	}
}
