package cursedflames.modifiers.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import cursedflames.modifiers.client.SmithingScreenReforge;
import net.minecraft.client.gui.screen.ingame.ForgingScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.SmithingScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ForgingScreen.class)
public abstract class MixinForgingScreen extends HandledScreen {
	private MixinForgingScreen(ScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
		super(screenHandler, playerInventory, text);
	}

	@Inject(method = "drawBackground", at = @At("HEAD"), cancellable = true)
	private void onDrawBackground(MatrixStack matrixStack, float f, int i, int j, CallbackInfo ci) {
		if (((Object) this) instanceof SmithingScreen) {
			if (((SmithingScreenReforge) this).modifiers_onTab2()) {
				ci.cancel();
				RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
				this.client.getTextureManager().bindTexture(new Identifier("modifiers", "textures/gui/reforger.png"));
				int k = (this.width - this.backgroundWidth) / 2;
				int l = (this.height - this.backgroundHeight) / 2;
				this.drawTexture(matrixStack, k, l, 0, 0, this.backgroundWidth, this.backgroundHeight);
				ItemStack stack1 = this.handler.getSlot(0).getStack();
				ItemStack stack2 = this.handler.getSlot(1).getStack();

				// TODO add a util function somewhere for `canReforge(stack1, stack2)`
				if (!stack1.isEmpty() && !stack1.getItem().canRepair(stack1, stack2)) {
					this.drawTexture(matrixStack, k + 99 - 53, l + 45, this.backgroundWidth, 0, 28, 21);
				}
			}
		}
	}
}
