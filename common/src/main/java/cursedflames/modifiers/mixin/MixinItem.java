package cursedflames.modifiers.mixin;

import cursedflames.modifiers.common.modifier.Modifier;
import cursedflames.modifiers.common.modifier.ModifierHandler;
import cursedflames.modifiers.common.modifier.Modifiers;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class MixinItem {
	@Inject(method = "getName(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/text/Text;", at = @At("RETURN"), cancellable = true)
	private void onGetDisplayName(ItemStack stack, CallbackInfoReturnable<Text> cir) {
		Modifier modifier = ModifierHandler.getModifier(stack);
		if (modifier != null && modifier != Modifiers.NONE) {
			cir.setReturnValue(modifier.getFormattedName().append(" ").append(new TranslatableText(this.getTranslationKey(stack))));
		}
	}

	@Shadow public abstract String getTranslationKey(ItemStack stack);
}
