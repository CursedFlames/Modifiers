package cursedflames.modifiers.mixin.client;

import cursedflames.modifiers.client.SmithingScreenReforge;
import cursedflames.modifiers.client.TabButtonWidget;
import cursedflames.modifiers.common.modifier.Modifier;
import cursedflames.modifiers.common.modifier.ModifierHandler;
import cursedflames.modifiers.common.network.NetworkHandler;
import cursedflames.modifiers.common.network.PacketC2SReforge;
import net.minecraft.client.gui.screen.ingame.ForgingScreen;
import net.minecraft.client.gui.screen.ingame.SmithingScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ToggleButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SmithingScreen.class)
public abstract class MixinSmithingScreen extends ForgingScreen implements SmithingScreenReforge {
	private ButtonWidget modifiers_reforgeButton;
	private TabButtonWidget modifiers_tabButton1;
	private TabButtonWidget modifiers_tabButton2;
	private boolean modifiers_onTab2 = false;

	private Text modifiers_tab1Title;
	private Text modifiers_tab2Title;

	private int modifiers_outputSlotX;
	private int modifiers_outputSlotY;

	private MixinSmithingScreen(ForgingScreenHandler forgingScreenHandler, PlayerInventory playerInventory, Text text, Identifier identifier) {
		super(forgingScreenHandler, playerInventory, text, identifier);
	}

	private void modifiers_toTab1() {
		modifiers_onTab2 = false;
		modifiers_reforgeButton.visible = false;
		this.title = modifiers_tab1Title;
		Slot slot = this.getScreenHandler().slots.get(2);
		slot.x = modifiers_outputSlotX;
		slot.y = modifiers_outputSlotY;
		this.modifiers_tabButton1.toggled = true;
		this.modifiers_tabButton2.toggled = false;
	}

	private void modifiers_toTab2() {
		modifiers_onTab2 = true;
		modifiers_reforgeButton.visible = true;
		this.title = modifiers_tab2Title;
		Slot slot = this.getScreenHandler().slots.get(2);
		slot.x = 150;
		slot.y = 8;
		this.modifiers_tabButton1.toggled = false;
		this.modifiers_tabButton2.toggled = true;
	}

	@Override
	public void modifiers_init() {
		int k = (this.width - this.backgroundWidth) / 2;
		int l = (this.height - this.backgroundHeight) / 2;
		Slot slot = this.getScreenHandler().slots.get(2);
		modifiers_outputSlotX = slot.x;
		modifiers_outputSlotY = slot.y;
		this.modifiers_tabButton1 = new TabButtonWidget(k-70, l+2, 70, 18, new TranslatableText("container.modifiers.reforge.tab1"), (button) -> modifiers_toTab1());
		this.modifiers_tabButton2 = new TabButtonWidget(k-70, l+22, 70, 18, new TranslatableText("container.modifiers.reforge.tab2"), (button) -> modifiers_toTab2());
		this.modifiers_tabButton1.toggled = true;
		this.modifiers_tabButton1.setTextureUV(0, 166, 70, 18, new Identifier("modifiers", "textures/gui/reforger.png"));
		this.modifiers_tabButton2.setTextureUV(0, 166, 70, 18, new Identifier("modifiers", "textures/gui/reforger.png"));
		this.modifiers_reforgeButton = new ButtonWidget(k+100, l+45, 69, 20, new TranslatableText("container.modifiers.reforge.reforge"), (button) -> NetworkHandler.sendToServer(new PacketC2SReforge()));
		addButton(this.modifiers_tabButton1);
		addButton(this.modifiers_tabButton2);
		addButton(this.modifiers_reforgeButton);
		modifiers_tab1Title = this.title;
		modifiers_tab2Title = new TranslatableText("container.modifiers.reforge");
		this.modifiers_toTab1();
	}

	@Override
	public boolean modifiers_onTab2() {
		return modifiers_onTab2;
	}

	@Inject(method = "drawForeground", at = @At("RETURN"))
	private void onDrawForeground(MatrixStack matrixStack, int i, int j, CallbackInfo ci) {
		if (this.modifiers_onTab2) {
			ItemStack stack = this.handler.getSlot(0).getStack();
			Modifier modifier = ModifierHandler.getModifier(stack);
			if (modifier != null) {
				this.textRenderer.draw(matrixStack, new TranslatableText("misc.modifiers.modifier_prefix").append(modifier.getFormattedName()), (float)this.titleX-15, (float)this.titleY+15, 4210752);
			}
		}
	}
}
