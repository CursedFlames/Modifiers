package cursedflames.modifiers.mixin;

import cursedflames.modifiers.common.modifier.Modifier;
import cursedflames.modifiers.common.modifier.ModifierHandler;
import cursedflames.modifiers.common.modifier.Modifiers;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class MixinItem {
	@Inject(method = "getName(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/network/chat/Component;", at = @At("RETURN"), cancellable = true)
	private void onGetDisplayName(ItemStack stack, CallbackInfoReturnable<Component> cir) {
		Modifier modifier = ModifierHandler.getModifier(stack);
		if (modifier != null && modifier != Modifiers.NONE) {
			cir.setReturnValue(modifier.getFormattedName().append(" ").append(Component.translatable(this.getDescriptionId(stack))));
		}
	}

	@Shadow public abstract String getDescriptionId(ItemStack stack);
}
