package cursedflames.modifiers.mixin;

import cursedflames.modifiers.common.modifier.Modifier;
import cursedflames.modifiers.common.modifier.ModifierHandler;
import cursedflames.modifiers.common.modifier.Modifiers;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class MixinItem {
	@Inject(method = "getDisplayName", at = @At("RETURN"), cancellable = true)
	private void onGetDisplayName(ItemStack stack, CallbackInfoReturnable<ITextComponent> cir) {
		Modifier modifier = ModifierHandler.getModifier(stack);
		if (modifier != null && modifier != Modifiers.NONE) {
			cir.setReturnValue(modifier.getFormattedName().appendString(" ").append(new TranslationTextComponent(this.getTranslationKey(stack))));
		}
	}

	@Shadow public abstract String getTranslationKey(ItemStack stack);
}
