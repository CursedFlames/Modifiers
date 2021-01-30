package cursedflames.modifiers.mixin.client;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.AbstractRepairScreen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.SmithingTableScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.SmithingTableContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ContainerScreen.class)
public abstract class MixinContainerScreen extends Screen {
	private MixinContainerScreen(ITextComponent titleIn) {
		super(titleIn);
	}

	private Button button;

//	@Override
//	protected void init() {
//		super.init();
//		this.button = new Button(0, 0, 80, 20, new StringTextComponent("test"), (button) -> {});
//		addButton(this.button);
//	}

	@Inject(method="init", at=@At("RETURN"))
	private void onInit(CallbackInfo ci) {
		if (((Object) this) instanceof SmithingTableScreen) {
			this.button = new Button(0, 0, 80, 20, new StringTextComponent("test"), (button) -> {});
			addButton(this.button);
		}
	}
}
