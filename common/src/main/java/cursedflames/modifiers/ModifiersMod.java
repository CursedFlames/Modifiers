package cursedflames.modifiers;

import cursedflames.modifiers.common.curio.ICurioProxy;
import cursedflames.modifiers.common.item.ItemModifierBook;
import cursedflames.modifiers.common.recipe.ReforgingRecipe;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeType;

public class ModifiersMod {
    public static final String MODID = "modifiers";

    public static ICurioProxy curioProxy;

    public static ItemModifierBook modifier_book;
	public static Item reforge_template;

    public static CreativeModeTab GROUP_BOOKS;

	public static RecipeType<ReforgingRecipe> RECIPE_TYPE;
}
