package cursedflames.modifiers.common.integration.jei;

import cursedflames.modifiers.ModifiersMod;
import cursedflames.modifiers.common.modifier.ModifierHandler;
import cursedflames.modifiers.common.recipe.ReforgingRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@mezz.jei.api.JeiPlugin
@ParametersAreNonnullByDefault
public class JeiPlugin implements IModPlugin {
	static final RecipeType<ReforgingRecipe> REFORGING_RECIPE_TYPE = RecipeType.create(ModifiersMod.MODID, "reforging", ReforgingRecipe.class);

	@Override
	public @NotNull ResourceLocation getPluginUid() {
		return new ResourceLocation(ModifiersMod.MODID, "jei_plugin");
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		registration.addRecipeCategories(new ReforgingRecipeCategory(registration.getJeiHelpers()));
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		var smithingRecipes = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(net.minecraft.world.item.crafting.RecipeType.SMITHING);
		registration.addRecipes(REFORGING_RECIPE_TYPE,
				smithingRecipes.stream()
						.flatMap(recipe -> {
							if (recipe instanceof ReforgingRecipe ref) {
								if (ref.useRepairMaterialCondition) {
									return flattenDynamicRecipe(ref);
								} else {
									return Stream.of(ref);
								}
							}
							return Stream.empty();
						})
						.filter(Objects::nonNull)
						.collect(Collectors.toList()));
	}

	static class ReforgingRecipeCategory implements IRecipeCategory<ReforgingRecipe> {
		private final IDrawable background;
		private final IDrawable icon;

		// does JEI expose this somewhere? not sure
		private static final ResourceLocation RECIPE_GUI_VANILLA = new ResourceLocation("jei", "textures/jei/gui/gui_vanilla.png");

		ReforgingRecipeCategory(IJeiHelpers helpers) {
			background = helpers.getGuiHelper().createDrawable(RECIPE_GUI_VANILLA, 0, 168, 108, 18);
			icon = helpers.getGuiHelper().createDrawableItemStack(new ItemStack(Blocks.SMITHING_TABLE));
		}
		@Override
		public @NotNull RecipeType<ReforgingRecipe> getRecipeType() {
			return REFORGING_RECIPE_TYPE;
		}

		@Override
		public @NotNull Component getTitle() {
			return Component.translatable("modifiers.jei.reforging");
		}

		@Override
		public @NotNull IDrawable getBackground() {
			return background;
		}

		@Override
		public @NotNull IDrawable getIcon() {
			return icon;
		}

		@Override
		public void setRecipe(IRecipeLayoutBuilder builder, ReforgingRecipe recipe, IFocusGroup focuses) {
			var templates = recipe.template;
			// Shouldn't happen, since we flatten these recipes while loading
			if (recipe.useRepairMaterialCondition) return;
			var bases = List.of(recipe.base.getItems());
			var adds = List.of(recipe.add.getItems());
			var outputs = bases.stream().map(stack -> {
				var stack2 = stack.copy();
				ModifierHandler.prepareReroll(stack2);
				return stack2;
			}).toList();
			builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredients(templates);
			builder.addSlot(RecipeIngredientRole.INPUT, 19, 1).addItemStacks(bases);
			builder.addSlot(RecipeIngredientRole.INPUT, 37, 1).addItemStacks(adds);
			builder.addSlot(RecipeIngredientRole.OUTPUT, 91, 1).addItemStacks(outputs);
		}
	}

	// Really wish JEI just made this available somehow
	record RepairData(Ingredient repairMaterial, List<ItemStack> stacks) {
		RepairData(Tiers tier, Item... items) {
			this(tier.getRepairIngredient(), Arrays.stream(items).map(ItemStack::new).collect(Collectors.toList()));
		}
		RepairData(ArmorMaterials material, Item... items) {
			this(material.getRepairIngredient(), Arrays.stream(items).map(ItemStack::new).collect(Collectors.toList()));
		}
	}

	// This isn't comprehensive, even for vanilla, but hopefully players get the idea
	private static Stream<RepairData> vanillaRepairData() {
		return Stream.of(
				new RepairData(Tiers.WOOD, Items.WOODEN_AXE, Items.WOODEN_HOE, Items.WOODEN_PICKAXE, Items.WOODEN_SHOVEL, Items.WOODEN_SWORD),
				new RepairData(Tiers.STONE, Items.STONE_AXE, Items.STONE_HOE, Items.STONE_PICKAXE, Items.STONE_SHOVEL, Items.STONE_SWORD),
				new RepairData(Tiers.IRON, Items.IRON_AXE, Items.IRON_HOE, Items.IRON_PICKAXE, Items.IRON_SHOVEL, Items.IRON_SWORD),
				new RepairData(Tiers.GOLD, Items.GOLDEN_AXE, Items.GOLDEN_HOE, Items.GOLDEN_PICKAXE, Items.GOLDEN_SHOVEL, Items.GOLDEN_SWORD),
				new RepairData(Tiers.DIAMOND, Items.DIAMOND_AXE, Items.DIAMOND_HOE, Items.DIAMOND_PICKAXE, Items.DIAMOND_SHOVEL, Items.DIAMOND_SWORD),
				new RepairData(Tiers.NETHERITE, Items.NETHERITE_AXE, Items.NETHERITE_HOE, Items.NETHERITE_PICKAXE, Items.NETHERITE_SHOVEL, Items.NETHERITE_SWORD),
				new RepairData(ArmorMaterials.LEATHER, Items.LEATHER_HELMET, Items.LEATHER_CHESTPLATE, Items.LEATHER_LEGGINGS, Items.LEATHER_BOOTS),
				new RepairData(ArmorMaterials.IRON, Items.IRON_HELMET, Items.IRON_CHESTPLATE, Items.IRON_LEGGINGS, Items.IRON_BOOTS),
				new RepairData(ArmorMaterials.GOLD, Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS),
				new RepairData(ArmorMaterials.DIAMOND, Items.DIAMOND_HELMET, Items.DIAMOND_CHESTPLATE, Items.DIAMOND_LEGGINGS, Items.DIAMOND_BOOTS),
				new RepairData(ArmorMaterials.NETHERITE, Items.NETHERITE_HELMET, Items.NETHERITE_CHESTPLATE, Items.NETHERITE_LEGGINGS, Items.NETHERITE_BOOTS)
		);
	}

	private static Stream<ReforgingRecipe> flattenDynamicRecipe(ReforgingRecipe recipe) {
		if (recipe.useRepairMaterialCondition) {
			return vanillaRepairData().flatMap(data ->
					data.stacks.stream().map(stack -> new ReforgingRecipe(new ResourceLocation("modifiers", "dummy_recipe"), recipe.template, Ingredient.of(stack), data.repairMaterial, false))
			);
		} else {
			return Stream.of(recipe);
		}
	}
}
