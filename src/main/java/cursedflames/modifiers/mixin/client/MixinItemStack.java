package cursedflames.modifiers.mixin.client;

import cursedflames.modifiers.common.modifier.Modifier;
import cursedflames.modifiers.common.modifier.ModifierHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(ItemStack.class)
public class MixinItemStack {
	@Inject(method = "getTooltip", at = @At("RETURN"), locals= LocalCapture.CAPTURE_FAILHARD)
	private void onGetTooltip(PlayerEntity playerIn, ITooltipFlag advanced, CallbackInfoReturnable<List<ITextComponent>> cir, List<ITextComponent> list) {
		Modifier modifier = ModifierHandler.getModifier((ItemStack) (Object) this);
		if (modifier != null) {
			list.addAll(modifier.getInfoLines());
		}
	}
}
