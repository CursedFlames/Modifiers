package cursedflames.modifiers.mixin;

import cursedflames.modifiers.common.modifier.Modifier;
import cursedflames.modifiers.common.modifier.ModifierHandler;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.AbstractRepairContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.SmithingTableContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SmithingTableContainer.class)
public abstract class MixinSmithingTableContainer extends AbstractRepairContainer {
	@Shadow @Final private World field_234651_g_;
	public MixinSmithingTableContainer(ContainerType<?> a, int b, PlayerInventory c, IWorldPosCallable d) {
		super(a, b, c, d);
	}

	@Inject(method="updateRepairOutput", at=@At("RETURN"))
	private void onCreateResult(CallbackInfo ci) {
		if (this.field_234642_c_.isEmpty()) {
			ItemStack stack = field_234643_d_.getStackInSlot(0);
			ItemStack material = field_234643_d_.getStackInSlot(1);

			if (ModifierHandler.canHaveModifiers(stack)) {
				if (stack.getItem().getIsRepairable(stack, material)) {
					if (ModifierHandler.hasModifier(stack)) {
						ItemStack output = stack.copy();
						CompoundNBT tag = stack.getTag();
						if (tag == null) {
							tag = new CompoundNBT();
						} else {
							tag = tag.copy();
						}
						output.setTag(tag);
//						Modifier modifier = ModifierHandler.rollModifier(output, field_234651_g_.rand);
//						if (modifier != null) {
//							ModifierHandler.setModifier(output, modifier);
							ModifierHandler.removeModifier(output);
							this.field_234642_c_.setInventorySlotContents(0, output);
//						}
					}
				}
			}
		}
	}
}
