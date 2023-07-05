package cursedflames.modifiers.mixin;

import cursedflames.modifiers.common.modifier.Modifier;
import cursedflames.modifiers.common.modifier.ModifierHandler;
import cursedflames.modifiers.common.reforge.SmithingScreenHandlerReforge;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SmithingMenu.class)
public abstract class MixinSmithingTableContainer extends ItemCombinerMenu implements SmithingScreenHandlerReforge {
//	@Shadow @Final private Level field_25385;
	public MixinSmithingTableContainer(MenuType<?> a, int b, Inventory c, ContainerLevelAccess d) {
		super(a, b, c, d);
	}

//	@Override public void tryReforge() {
//		ItemStack stack = input.getStack(0);
//		ItemStack material = input.getStack(1);
//
//		if (ModifierHandler.canHaveModifiers(stack)) {
//			if (stack.getItem().canRepair(stack, material)) {
//				boolean hadModifier = ModifierHandler.hasModifier(stack);
//				Modifier modifier = ModifierHandler.rollModifier(stack, this.field_25385.random);
//				if (modifier != null) {
//					ModifierHandler.setModifier(stack, modifier);
//					if (hadModifier) {
//						material.decrement(1);
//						// We do this for markDirty() mostly, I think
//						input.setStack(1, material);
//					}
//				}
//			}
//		}
//	}
}
