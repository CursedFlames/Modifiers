package cursedflames.modifiers.fabric;

import cursedflames.modifiers.ModifiersMod;
import cursedflames.modifiers.common.curio.ICurioProxy;
import cursedflames.modifiers.common.item.ItemModifierBook;
import cursedflames.modifiers.common.network.NetworkHandler;
import cursedflames.modifiers.fabric.network.NetworkHandlerFabric;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ModifiersModFabric extends ModifiersMod implements ModInitializer {
    @Override public void onInitialize() {
        NetworkHandler.register();
        modifier_book = new ItemModifierBook();
		GROUP_BOOKS = FabricItemGroup
				.builder()
				.title(Component.translatable("itemGroup.modifiers_books"))
				.icon(() -> new ItemStack(modifier_book))
				.displayItems((params, output) -> output.acceptAll(modifier_book.getStacksForCreativeTab()))
				.build();
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(MODID, "modifier_book"), modifier_book);
		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, new ResourceLocation(MODID, "books"), GROUP_BOOKS);
    }

    static {
        NetworkHandler.setProxy(new NetworkHandlerFabric());

        // Always dummy proxy here since we don't support curios on fabric anyway
        curioProxy = new ICurioProxy() {};
    }
}
