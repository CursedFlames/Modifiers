package cursedflames.modifiers.common.recipe;

import com.google.gson.JsonObject;
import cursedflames.modifiers.common.modifier.ModifierHandler;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.level.Level;

import java.util.function.BiPredicate;

public class ReforgingRecipe implements SmithingRecipe {
	private final ResourceLocation id;
	final Ingredient template;
	final Ingredient base;
	final Ingredient add;
	final boolean useRepairMaterialCondition;

	public ReforgingRecipe(ResourceLocation id, Ingredient template, Ingredient base, Ingredient add, boolean useRepairMaterialCondition) {
		this.id = id;
		this.template = template;
		this.base = base;
		this.add = add;
		this.useRepairMaterialCondition = useRepairMaterialCondition;
	}

	@Override
	public boolean isTemplateIngredient(ItemStack stack) {
		return template.test(stack);
	}

	@Override
	public boolean isBaseIngredient(ItemStack stack) {
		return ModifierHandler.hasModifier(stack) && (useRepairMaterialCondition || this.base.test(stack));
	}

	@Override
	public boolean isAdditionIngredient(ItemStack stack) {
		// This is kinda hacky, but the repair material condition depends on both base and add items, so we can't tell just from the add one.
		return useRepairMaterialCondition || this.add.test(stack);
	}

	@Override
	public boolean matches(Container container, Level level) {
		var template = container.getItem(0);
		var base = container.getItem(1);
		var add = container.getItem(2);
		return this.isTemplateIngredient(template)
				&& this.isBaseIngredient(base)
				&& this.isAdditionIngredient(add)
				&& (!useRepairMaterialCondition || base.getItem().isValidRepairItem(base, add));
	}

	@Override
	public ItemStack assemble(Container container, RegistryAccess registryAccess) {
		var base = container.getItem(1);
		if (!this.matches(container, null)) return ItemStack.EMPTY;
		var out = base.copy();
		ModifierHandler.prepareReroll(out);
		return out;
	}

	@Override
	public ItemStack getResultItem(RegistryAccess registryAccess) {
		return new ItemStack(Items.GOLDEN_SWORD);
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return Serializer.INSTANCE;
	}

	public static class Serializer implements RecipeSerializer<ReforgingRecipe> {
		public static Serializer INSTANCE = new Serializer();

		@Override
		public ReforgingRecipe fromJson(ResourceLocation id, JsonObject json) {
			Ingredient template = Ingredient.fromJson(GsonHelper.getNonNull(json, "template"));
			if (json.has("addition") && json.get("addition").isJsonPrimitive() && json.get("addition").getAsString().equals("modifiers:special_repair")) {
				return new ReforgingRecipe(id, template, null, null, true);
			}
			Ingredient base = Ingredient.fromJson(GsonHelper.getNonNull(json, "base"));
			Ingredient add = Ingredient.fromJson(GsonHelper.getNonNull(json, "addition"));
			return new ReforgingRecipe(id, template, base, add, false);
		}

		@Override
		public ReforgingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
			boolean special = buf.readBoolean();
			Ingredient template = Ingredient.fromNetwork(buf);
			if (special) return new ReforgingRecipe(id, template, null, null, true);
			Ingredient base = Ingredient.fromNetwork(buf);
			Ingredient add = Ingredient.fromNetwork(buf);
			return new ReforgingRecipe(id, template, base, add, false);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buf, ReforgingRecipe recipe) {
			buf.writeBoolean(recipe.useRepairMaterialCondition);
			recipe.template.toNetwork(buf);
			if (!recipe.useRepairMaterialCondition) {
				recipe.base.toNetwork(buf);
				recipe.add.toNetwork(buf);
			}
		}
	}
}
