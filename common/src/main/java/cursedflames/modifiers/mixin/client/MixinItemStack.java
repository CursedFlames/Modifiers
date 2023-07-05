package cursedflames.modifiers.mixin.client;

import cursedflames.modifiers.common.modifier.Modifier;
import cursedflames.modifiers.common.modifier.ModifierHandler;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(ItemStack.class)
public class MixinItemStack {
	@Inject(method = "getTooltipLines", at = @At("RETURN"), locals= LocalCapture.CAPTURE_FAILHARD)
	private void onGetTooltip(Player playerIn, TooltipFlag advanced, CallbackInfoReturnable<List<Component>> cir, List<Component> list) {
		Modifier modifier = ModifierHandler.getModifier((ItemStack) (Object) this);
		if (modifier != null) {
			list.addAll(modifier.getInfoLines());
		}
	}
}
