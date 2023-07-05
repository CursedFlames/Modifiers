package cursedflames.modifiers.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
//import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

//public class TabButton extends Button {
//	public boolean toggled;
//
//	protected ResourceLocation texture;
//	protected int u;
//	protected int v;
//	protected int pressedUOffset;
//	protected int hoverVOffset;
//
////	public TabButton(int i, int j, int k, int l, Component text, OnPress pressAction) {
////		super(i, j, k, l, text, pressAction);
////	}
//
//	public TabButton(int i, int j, int k, int l, Component text, OnPress pressAction, Button.CreateNarration tooltipSupplier) {
//		super(i, j, k, l, text, pressAction, tooltipSupplier);
//	}
//
//	public void setTextureUV(int i, int j, int k, int l, ResourceLocation identifier) {
//		this.u = i;
//		this.v = j;
//		this.pressedUOffset = k;
//		this.hoverVOffset = l;
//		this.texture = identifier;
//	}
//
//	@Override
//	public void renderButton(PoseStack matrixStack, int i, int j, float f) {
//		Minecraft minecraftClient = Minecraft.getInstance();
//		Font textRenderer = minecraftClient.font;
//		minecraftClient.getTextureManager().bindTexture(this.texture);
//		RenderSystem.disableDepthTest();
//		int u = this.u;
//		int v = this.v;
//		if (this.toggled) {
//			u += this.pressedUOffset;
//		}
//
//		if (this.isHovered()) {
//			v += this.hoverVOffset;
//		}
//
//		this.drawTexture(matrixStack, this.x, this.y, u, v, this.width, this.height);
//		int color = this.active ? 16777215 : 10526880;
//		drawCenteredText(matrixStack, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, color | Mth.ceil(this.alpha * 255.0F) << 24);
//
//		if (this.isHovered()) {
//			this.renderToolTip(matrixStack, i, j);
//		}
//		RenderSystem.enableDepthTest();
//	}
//}
