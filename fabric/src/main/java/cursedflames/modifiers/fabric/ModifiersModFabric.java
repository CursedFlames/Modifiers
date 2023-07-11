package cursedflames.modifiers.fabric;

import cursedflames.modifiers.ModifiersMod;
import cursedflames.modifiers.common.curio.ICurioProxy;
import cursedflames.modifiers.common.item.ItemModifierBook;
import cursedflames.modifiers.common.recipe.ReforgingRecipe;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;

import static net.minecraft.world.level.storage.loot.BuiltInLootTables.VILLAGE_ARMORER;
import static net.minecraft.world.level.storage.loot.BuiltInLootTables.VILLAGE_WEAPONSMITH;

public class ModifiersModFabric extends ModifiersMod implements ModInitializer {
    @Override public void onInitialize() {
        modifier_book = new ItemModifierBook();
		GROUP_BOOKS = FabricItemGroup
				.builder()
				.title(Component.translatable("itemGroup.modifiers_books"))
				.icon(() -> new ItemStack(modifier_book))
				.displayItems((params, output) -> output.acceptAll(modifier_book.getStacksForCreativeTab()))
				.build();
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(MODID, "modifier_book"), modifier_book);
		Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(MODID, "reforge_template"), reforge_template = new Item(new Item.Properties()));
		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, new ResourceLocation(MODID, "books"), GROUP_BOOKS);
		Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, new ResourceLocation(MODID, "smithing_reforge"), ReforgingRecipe.Serializer.INSTANCE);

		LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
			if (source.isBuiltin() && (VILLAGE_WEAPONSMITH.equals(id) || VILLAGE_ARMORER.equals(id))) {
				var pool = LootPool.lootPool()
						.with(LootItem.lootTableItem(reforge_template).build())
						.build();
				tableBuilder.pool(pool);
			}
		});
    }

    static {
        // Always dummy proxy here since we don't support curios on fabric anyway
        curioProxy = new ICurioProxy() {};
    }
}
