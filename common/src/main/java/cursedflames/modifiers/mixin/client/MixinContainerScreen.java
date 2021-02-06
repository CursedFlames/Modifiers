package cursedflames.modifiers.mixin.client;

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
public abstract class MixinContainerScreen extends Screen {
	private MixinContainerScreen(Text titleIn) {
		super(titleIn);
	}

	private ButtonWidget button;

//	@Override
//	protected void init() {
//		super.init();
//		this.button = new Button(0, 0, 80, 20, new StringTextComponent("test"), (button) -> {});
//		addButton(this.button);
//	}

	@Inject(method="init", at=@At("RETURN"))
	private void onInit(CallbackInfo ci) {
		if (((Object) this) instanceof SmithingScreen) {
			this.button = new ButtonWidget(0, 0, 80, 20, new LiteralText("test"), (button) -> {});
			addButton(this.button);
		}
	}
}
