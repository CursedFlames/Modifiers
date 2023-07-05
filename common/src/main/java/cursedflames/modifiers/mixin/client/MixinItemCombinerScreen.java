package cursedflames.modifiers.mixin.client;

import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemCombinerScreen.class)
public abstract class MixinItemCombinerScreen extends MixinAbstractContainerScreen {
	private MixinItemCombinerScreen(Component text) {
		super(text);
	}

//	@Inject(method = "drawBackground", at = @At("HEAD"), cancellable = true)
//	private void onDrawBackground(PoseStack matrixStack, float f, int i, int j, CallbackInfo ci) {
//		if (((Object) this) instanceof SmithingScreen) {
//			if (((SmithingScreenReforge) this).modifiers_onTab2()) {
//				ci.cancel();
//				RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//				this.client.getTextureManager().bindTexture(new ResourceLocation("modifiers", "textures/gui/reforger.png"));
//				int k = (this.width - this.backgroundWidth) / 2;
//				int l = (this.height - this.backgroundHeight) / 2;
//				this.drawTexture(matrixStack, k, l, 0, 0, this.backgroundWidth, this.backgroundHeight);
//				ItemStack stack1 = this.handler.getSlot(0).getStack();
//				ItemStack stack2 = this.handler.getSlot(1).getStack();
//
//				// TODO add a util function somewhere for `canReforge(stack1, stack2)`
//				boolean cantReforge = !stack1.isEmpty() && !stack1.getItem().canRepair(stack1, stack2);
//				// canReforge is also true for empty slot 1. Probably how it should behave.
//				((SmithingScreenReforge) this).modifiers_setCanReforge(!cantReforge);
//				if (!stack1.isEmpty() && !stack1.getItem().canRepair(stack1, stack2)) {
//					this.drawTexture(matrixStack, k + 99 - 53, l + 45, this.backgroundWidth, 0, 28, 21);
//				}
//			}
//		}
//	}
}
