package cursedflames.modifiers.mixin.client;

import cursedflames.modifiers.client.SmithingScreenReforge;
//import cursedflames.modifiers.client.TabButton;
//import cursedflames.modifiers.common.modifier.Modifier;
//import cursedflames.modifiers.common.modifier.ModifierHandler;
//import cursedflames.modifiers.common.network.NetworkHandler;
//import cursedflames.modifiers.common.network.PacketC2SReforge;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.client.gui.screens.inventory.SmithingScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ItemCombinerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SmithingScreen.class)
public abstract class MixinSmithingScreen extends ItemCombinerScreen implements SmithingScreenReforge {
//	private TabButton modifiers_reforgeButton;
//	private TabButton modifiers_tabButton1;
//	private TabButton modifiers_tabButton2;
//	private boolean modifiers_onTab2 = false;
//	private boolean modifiers_canReforge = false;
//
//	privateComponent modifiers_tab1Title;
//	privateComponent modifiers_tab2Title;
//
//	private int modifiers_outputSlotX;
//	private int modifiers_outputSlotY;

	private MixinSmithingScreen(ItemCombinerMenu forgingScreenHandler, Inventory playerInventory, Component text, ResourceLocation identifier) {
		super(forgingScreenHandler, playerInventory, text, identifier);
	}

//	private void modifiers_toTab1() {
//		modifiers_onTab2 = false;
//		modifiers_reforgeButton.visible = false;
//		this.title = modifiers_tab1Title;
//		Slot slot = this.getScreenHandler().slots.get(2);
//		slot.x = modifiers_outputSlotX;
//		slot.y = modifiers_outputSlotY;
//		this.modifiers_tabButton1.toggled = true;
//		this.modifiers_tabButton2.toggled = false;
//	}
//
//	private void modifiers_toTab2() {
//		modifiers_onTab2 = true;
//		modifiers_reforgeButton.visible = true;
//		this.title = modifiers_tab2Title;
//		Slot slot = this.getScreenHandler().slots.get(2);
//		slot.x = 152;
//		slot.y = 8;
//		this.modifiers_tabButton1.toggled = false;
//		this.modifiers_tabButton2.toggled = true;
//	}
//
//	@Override
//	public void modifiers_init() {
//		int k = (this.width - this.backgroundWidth) / 2;
//		int l = (this.height - this.backgroundHeight) / 2;
//		Slot slot = this.getScreenHandler().slots.get(2);
//		modifiers_outputSlotX = slot.x;
//		modifiers_outputSlotY = slot.y;
//		this.modifiers_tabButton1 = new TabButton(k-70, l+2, 70, 18, Component.translatable("container.modifiers.reforge.tab1"), (button) -> modifiers_toTab1());
//		this.modifiers_tabButton2 = new TabButton(k-70, l+22, 70, 18, Component.translatable("container.modifiers.reforge.tab2"), (button) -> modifiers_toTab2());
//		this.modifiers_tabButton1.setTextureUV(0, 166, 70, 18, new ResourceLocation("modifiers", "textures/gui/reforger.png"));
//		this.modifiers_tabButton2.setTextureUV(0, 166, 70, 18, new ResourceLocation("modifiers", "textures/gui/reforger.png"));
//		this.modifiers_reforgeButton = new TabButton(k+132, l+45, 20, 20,Component.of(""),
//				(button) -> NetworkHandler.sendToServer(new PacketC2SReforge()),
//				(button, matrixStack, i, j) -> this.renderTooltip(matrixStack, Component.translatable("container.modifiers.reforge.reforge"), i, j));
//		this.modifiers_reforgeButton.setTextureUV(0, 202, 20, 20, new ResourceLocation("modifiers", "textures/gui/reforger.png"));
//		addButton(this.modifiers_tabButton1);
//		addButton(this.modifiers_tabButton2);
//		addButton(this.modifiers_reforgeButton);
//		modifiers_tab1Title = this.title;
//		modifiers_tab2Title = Component.translatable("container.modifiers.reforge");
//		this.modifiers_toTab1();
//	}
//
//	@Override
//	public boolean modifiers_onTab2() {
//		return modifiers_onTab2;
//	}
//
//	@Override
//	public void modifiers_setCanReforge(boolean canReforge) {
//		this.modifiers_canReforge = canReforge;
//		this.modifiers_reforgeButton.toggled = canReforge;
//		this.modifiers_reforgeButton.active = canReforge;
//	}
//
//	@Inject(method = "drawForeground", at = @At("RETURN"))
//	private void onDrawForeground(PoseStack matrixStack, int i, int j, CallbackInfo ci) {
//		if (this.modifiers_onTab2) {
//			ItemStack stack = this.handler.getSlot(0).getStack();
//			Modifier modifier = ModifierHandler.getModifier(stack);
//			if (modifier != null) {
//				this.textRenderer.draw(matrixStack, Component.translatable("misc.modifiers.modifier_prefix").append(modifier.getFormattedName()), (float)this.titleX-15, (float)this.titleY+15, 4210752);
//			}
//		}
//	}
}
