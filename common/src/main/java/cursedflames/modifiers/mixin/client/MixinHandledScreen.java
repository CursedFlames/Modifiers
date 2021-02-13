package cursedflames.modifiers.mixin.client;

import cursedflames.modifiers.client.SmithingScreenReforge;
import cursedflames.modifiers.common.network.NetworkHandler;
import cursedflames.modifiers.common.network.PacketC2SReforge;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.SmithingScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public abstract class MixinHandledScreen extends Screen {
	private MixinHandledScreen(Text titleIn) {
		super(titleIn);
	}

	@Inject(method="init", at=@At("RETURN"))
	private void onInit(CallbackInfo ci) {
		if (((Object) this) instanceof SmithingScreen) {
			((SmithingScreenReforge) this).modifiers_init();
		}
	}
}
