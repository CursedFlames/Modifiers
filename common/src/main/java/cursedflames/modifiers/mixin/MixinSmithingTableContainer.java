package cursedflames.modifiers.mixin;

import cursedflames.modifiers.common.modifier.Modifier;
import cursedflames.modifiers.common.modifier.ModifierHandler;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.SmithingScreenHandler;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SmithingScreenHandler.class)
public abstract class MixinSmithingTableContainer extends ForgingScreenHandler {
	@Shadow @Final private World field_25385;
	public MixinSmithingTableContainer(ScreenHandlerType<?> a, int b, PlayerInventory c, ScreenHandlerContext d) {
		super(a, b, c, d);
	}

	@Inject(method="updateResult", at=@At("RETURN"))
	private void onCreateResult(CallbackInfo ci) {
		if (this.output.isEmpty()) {
			ItemStack stack = input.getStack(0);
			ItemStack material = input.getStack(1);

			if (ModifierHandler.canHaveModifiers(stack)) {
				if (stack.getItem().canRepair(stack, material)) {
					if (ModifierHandler.hasModifier(stack)) {
						ItemStack output = stack.copy();
						CompoundTag tag = stack.getTag();
						if (tag == null) {
							tag = new CompoundTag();
						} else {
							tag = tag.copy();
						}
						output.setTag(tag);
//						Modifier modifier = ModifierHandler.rollModifier(output, field_234651_g_.rand);
//						if (modifier != null) {
//							ModifierHandler.setModifier(output, modifier);
							ModifierHandler.removeModifier(output);
							this.output.setStack(0, output);
//						}
					}
				}
			}
		}
	}
}
