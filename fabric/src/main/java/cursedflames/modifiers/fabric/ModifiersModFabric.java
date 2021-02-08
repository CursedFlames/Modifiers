package cursedflames.modifiers.fabric;

import cursedflames.modifiers.ModifiersMod;
import cursedflames.modifiers.common.curio.ICurioProxy;
import cursedflames.modifiers.common.item.ItemModifierBook;
import cursedflames.modifiers.common.network.NetworkHandler;
import cursedflames.modifiers.fabric.network.NetworkHandlerFabric;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModifiersModFabric extends ModifiersMod implements ModInitializer {
    @Override public void onInitialize() {
        modifier_book = new ItemModifierBook();
        Registry.register(Registry.ITEM, new Identifier(MODID, "modifier_book"), modifier_book);
    }

    static {
        NetworkHandler.setProxy(new NetworkHandlerFabric());

        GROUP_BOOKS = FabricItemGroupBuilder.build(
            new Identifier(MODID, "books"),
            () -> new ItemStack(modifier_book));
        // Always dummy proxy here since we don't support curios on fabric anyway
        curioProxy = new ICurioProxy() {};
    }
}
