package cursedflames.modifiers.mixin;

import cursedflames.modifiers.common.modifier.Modifier;
import cursedflames.modifiers.common.modifier.ModifierHandler;
import cursedflames.modifiers.common.reforge.SmithingScreenHandlerReforge;
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
public abstract class MixinSmithingTableContainer extends ForgingScreenHandler implements SmithingScreenHandlerReforge {
	@Shadow @Final private World field_25385;
	public MixinSmithingTableContainer(ScreenHandlerType<?> a, int b, PlayerInventory c, ScreenHandlerContext d) {
		super(a, b, c, d);
	}

	@Override public void tryReforge() {
		ItemStack stack = input.getStack(0);
		ItemStack material = input.getStack(1);

		if (ModifierHandler.canHaveModifiers(stack)) {
			if (stack.getItem().canRepair(stack, material)) {
				boolean hadModifier = ModifierHandler.hasModifier(stack);
				Modifier modifier = ModifierHandler.rollModifier(stack, this.field_25385.random);
				if (modifier != null) {
					ModifierHandler.setModifier(stack, modifier);
					if (hadModifier) {
						material.decrement(1);
						// We do this for markDirty() mostly, I think
						input.setStack(1, material);
					}
				}
			}
		}
	}
}
