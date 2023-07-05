package cursedflames.modifiers.mixin.client;

import cursedflames.modifiers.client.SmithingScreenReforge;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.SmithingScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public abstract class MixinAbstractContainerScreen extends Screen {
	protected MixinAbstractContainerScreen(Component titleIn) {
		super(titleIn);
	}

	@Inject(method="init", at=@At("RETURN"))
	private void onInit(CallbackInfo ci) {
		if (((Object) this) instanceof SmithingScreen) {
			((SmithingScreenReforge) this).modifiers_init();
		}
	}
}
