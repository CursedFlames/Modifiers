package cursedflames.modifiers.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;

import cursedflames.modifiers.common.ModifierHandler;
import cursedflames.modifiers.common.ModifiersMod;
import cursedflames.modifiers.common.config.Config;
import cursedflames.modifiers.common.modifier.curio.ModifierHandlerCurio;
import cursedflames.modifiers.common.network.PacketDoReforge;
import cursedflames.modifiers.common.network.PacketHandler;
import cursedflames.modifiers.common.reforge.ContainerReforge;
import cursedflames.modifiers.common.util.XpUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.Button.IPressable;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ScreenReforge extends ContainerScreen<ContainerReforge> {
	private ResourceLocation GUI = new ResourceLocation(ModifiersMod.MODID, "textures/gui/reforger.png");
	private static final String tranStr = ModifiersMod.MODID+".gui.reforger.";
	
	private ContainerReforge container;
	
	private Button reforgeButton;

	public ScreenReforge(ContainerReforge screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
		this.xSize = 194;
//		this.ySize = 162;
		container = screenContainer;
	}
	
	@Override
	public void init() {
		super.init();
		Button button = new Button(guiLeft+61, guiTop+42, 90, 20, I18n.format(tranStr+"reforge"),
				new IPressable() {
					@Override
					public void onPress(Button p_onPress_1_) {
//						ModifiersMod.logger.info("button press");
//						CompoundNBT tag = new CompoundNBT();
//						tag.setTag("pos", Util.blockPosToNBT(container.stationPos));
//						PacketHandler.INSTANCE
//								.sendToServer(new NBTPacket(tag, PacketHandler.HandlerIds.REFORGE.id));
						PacketHandler.INSTANCE.sendToServer(new PacketDoReforge());
					}
		});
//		button.buttonPressSound = false;
		this.addButton(button);
		reforgeButton = button;
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		this.renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		
		GlStateManager.pushMatrix();
		GlStateManager.translatef(guiLeft+4, guiTop+4, 0);
		GlStateManager.disableLighting();
		ItemStack stack = container.stackHandler.getStackInSlot(0);
		if (stack.hasTag() && ModifierHandler.canHaveModifier(stack)
				&& ((Config.REFORGE_CURIO_ENABLED.get() && ModifierHandler.canHaveCurioModifier(stack))
						||(false/*TODO tool mods*/))) {
			PlayerEntity player = Minecraft.getInstance().player;
			int xp = XpUtil.getPlayerXP(player);
			CompoundNBT tag = stack.getTag();
			int xpCost = 0;
			if (tag.contains("reforgeCost"))
				xpCost = tag.getInt("reforgeCost");
			boolean inCreative = player.isCreative();
			reforgeButton.active = xp>xpCost||inCreative;
			int levelCost = 1+(xpCost>xp ? XpUtil.getLevelForExperience(xpCost)
					: (player.experienceLevel-XpUtil.getLevelForExperience(xp-xpCost)));
			String xpCostStr = I18n.format(tranStr+"xpcost");
			String levelsStr;
			if (I18n.hasKey(tranStr+"levels.override."+levelCost)) {
				levelsStr = I18n.format(tranStr+"levels.override."+levelCost, levelCost);
			} else {
				levelsStr = I18n.format(tranStr+"levels", levelCost);
			}
			font.drawString(
					xpCostStr+" "+String.valueOf(xpCost)+(inCreative ? "" : (" "+levelsStr)), 0, 0,
					(xp>xpCost||inCreative ? 0x00aa00 : 0xaa0000));
			if (tag.contains("curioMod")) {
				ResourceLocation mod = new ResourceLocation(tag.getString("curioMod"));
				font.drawSplitString(
						I18n.format(ModifierHandlerCurio.getModifierTranslationKey(mod)),
						0, 1+font.FONT_HEIGHT, 0xFFFFFF, 30);
			}
		}
		GlStateManager.popMatrix();
		
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindTexture(GUI);
		int relX = (this.width - this.xSize) / 2;
		int relY = (this.height - this.ySize) / 2;
		this.blit(relX, relY, 0, 0, this.xSize, this.ySize);
	}
}
