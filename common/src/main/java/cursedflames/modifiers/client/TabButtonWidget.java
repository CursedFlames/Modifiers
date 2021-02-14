package cursedflames.modifiers.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class TabButtonWidget extends ButtonWidget {
	public boolean toggled;

	protected Identifier texture;
	protected int u;
	protected int v;
	protected int pressedUOffset;
	protected int hoverVOffset;

	public TabButtonWidget(int i, int j, int k, int l, Text text, PressAction pressAction) {
		super(i, j, k, l, text, pressAction);
	}

	public TabButtonWidget(int i, int j, int k, int l, Text text, PressAction pressAction, ButtonWidget.TooltipSupplier tooltipSupplier) {
		super(i, j, k, l, text, pressAction, tooltipSupplier);
	}

	public void setTextureUV(int i, int j, int k, int l, Identifier identifier) {
		this.u = i;
		this.v = j;
		this.pressedUOffset = k;
		this.hoverVOffset = l;
		this.texture = identifier;
	}

	@Override
	public void renderButton(MatrixStack matrixStack, int i, int j, float f) {
		MinecraftClient minecraftClient = MinecraftClient.getInstance();
		TextRenderer textRenderer = minecraftClient.textRenderer;
		minecraftClient.getTextureManager().bindTexture(this.texture);
		RenderSystem.disableDepthTest();
		int u = this.u;
		int v = this.v;
		if (this.toggled) {
			u += this.pressedUOffset;
		}

		if (this.isHovered()) {
			v += this.hoverVOffset;
		}

		this.drawTexture(matrixStack, this.x, this.y, u, v, this.width, this.height);
		int color = this.active ? 16777215 : 10526880;
		drawCenteredText(matrixStack, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, color | MathHelper.ceil(this.alpha * 255.0F) << 24);

		if (this.isHovered()) {
			this.renderToolTip(matrixStack, i, j);
		}
		RenderSystem.enableDepthTest();
	}
}
