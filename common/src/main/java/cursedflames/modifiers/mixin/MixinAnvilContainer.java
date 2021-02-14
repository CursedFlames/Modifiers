package cursedflames.modifiers.mixin;

import cursedflames.modifiers.ModifiersMod;
import cursedflames.modifiers.common.modifier.Modifier;
import cursedflames.modifiers.common.modifier.ModifierHandler;
import cursedflames.modifiers.common.modifier.Modifiers;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(AnvilScreenHandler.class)
public abstract class MixinAnvilContainer extends ForgingScreenHandler {
	public MixinAnvilContainer(ScreenHandlerType<?> p_i231587_1_, int p_i231587_2_, PlayerInventory p_i231587_3_, ScreenHandlerContext p_i231587_4_) {
		super(p_i231587_1_, p_i231587_2_, p_i231587_3_, p_i231587_4_);
	}

	@Shadow @Final private Property levelCost;

	@Shadow public int repairItemUsage;

	@Inject(method = "updateResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;get(Lnet/minecraft/item/ItemStack;)Ljava/util/Map;", ordinal = 0),
		locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
	private void onUpdateRepairOutput(CallbackInfo ci, ItemStack stack, int i, int j, int k, ItemStack output, ItemStack stack2) {
		if (stack2.getItem() == ModifiersMod.modifier_book && stack2.hasTag()) {
			Modifier modifier = Modifiers.modifiers.get(new Identifier(stack2.getTag().getString(ModifierHandler.bookTagName)));
			if (modifier != null) {
				ModifierHandler.setModifier(output, modifier);
				this.levelCost.set(1);
				this.repairItemUsage = 1;
				this.output.setStack(0, output);
				this.sendContentUpdates();
				ci.cancel();
			}
		}
	}
}
