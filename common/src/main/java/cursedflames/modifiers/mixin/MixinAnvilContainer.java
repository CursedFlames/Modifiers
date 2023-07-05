package cursedflames.modifiers.mixin;

import cursedflames.modifiers.ModifiersMod;
import cursedflames.modifiers.common.modifier.Modifier;
import cursedflames.modifiers.common.modifier.ModifierHandler;
import cursedflames.modifiers.common.modifier.Modifiers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(AnvilMenu.class)
public abstract class MixinAnvilContainer extends ItemCombinerMenu {
	public MixinAnvilContainer(MenuType<?> p_i231587_1_, int p_i231587_2_, Inventory p_i231587_3_, ContainerLevelAccess p_i231587_4_) {
		super(p_i231587_1_, p_i231587_2_, p_i231587_3_, p_i231587_4_);
	}

	@Shadow @Final private DataSlot cost;

	@Shadow public int repairItemCountCost;

	@Inject(method = "createResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getEnchantments(Lnet/minecraft/world/item/ItemStack;)Ljava/util/Map;", ordinal = 0),
		locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
	private void onUpdateRepairOutput(CallbackInfo ci, ItemStack stack, int i, int j, int k, ItemStack output, ItemStack stack2) {
		// TODO only allow applying modifiers to items that accept them?
		if (stack2.getItem() == ModifiersMod.modifier_book && stack2.hasTag()) {
			Modifier modifier = Modifiers.modifiers.get(new ResourceLocation(stack2.getTag().getString(ModifierHandler.bookTagName)));
			if (modifier != null) {
				ModifierHandler.setModifier(output, modifier);
				this.cost.set(1);
				this.repairItemCountCost = 1;
				this.resultSlots.setItem(0, output);
				this.broadcastChanges();
				ci.cancel();
			}
		}
	}
}
