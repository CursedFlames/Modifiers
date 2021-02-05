package cursedflames.modifiers.mixin;

import cursedflames.modifiers.ModifiersMod;
import cursedflames.modifiers.common.modifier.Modifier;
import cursedflames.modifiers.common.modifier.ModifierHandler;
import cursedflames.modifiers.common.modifier.Modifiers;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.AbstractRepairContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.RepairContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(RepairContainer.class)
public abstract class MixinAnvilContainer extends AbstractRepairContainer {
	public MixinAnvilContainer(ContainerType<?> p_i231587_1_, int p_i231587_2_, PlayerInventory p_i231587_3_, IWorldPosCallable p_i231587_4_) {
		super(p_i231587_1_, p_i231587_2_, p_i231587_3_, p_i231587_4_);
	}

	@Shadow @Final private IntReferenceHolder maximumCost;

	@Shadow public int materialCost;

	@Inject(method = "updateRepairOutput", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getEnchantments(Lnet/minecraft/item/ItemStack;)Ljava/util/Map;", ordinal = 0),
		locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
	private void onUpdateRepairOutput(CallbackInfo ci, ItemStack stack, int i, int j, int k, ItemStack output, ItemStack stack2) {
		System.out.println(stack.getItem().getRegistryName().toString() + " " + stack2.getItem().getRegistryName().toString() + " " + output.getItem().getRegistryName().toString());
		if (stack2.getItem() == ModifiersMod.modifier_book && stack2.hasTag()) {
			System.out.println(stack2.getTag().toString());
			Modifier modifier = Modifiers.modifiers.get(new ResourceLocation(stack2.getTag().getString(ModifierHandler.bookTagName)));
			if (modifier != null) {
				ModifierHandler.setModifier(output, modifier);
				this.maximumCost.set(1);
				this.materialCost = 1;
				this.field_234642_c_.setInventorySlotContents(0, output);
				this.detectAndSendChanges();
				ci.cancel();
			}
		}
	}
}
