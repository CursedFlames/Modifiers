package cursedflames.modifiers.mixin;

import cursedflames.modifiers.common.modifier.Modifier;
import cursedflames.modifiers.common.modifier.ModifierHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {
	private MixinLivingEntity(EntityType<?> entityTypeIn, Level worldIn) {
		super(entityTypeIn, worldIn);
	}

	// FIXME this doesn't detect dropped hand items correctly
	@Inject(method = "collectEquipmentChanges", at = @At(value="INVOKE", target="Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"), locals=LocalCapture.CAPTURE_FAILHARD)
	private void onCollectEquipmentChanges(CallbackInfoReturnable<Map<EquipmentSlot, ItemStack>> cir,
			Map<EquipmentSlot, ItemStack> v1, EquipmentSlot[] v2, int i, int j, EquipmentSlot slotType, ItemStack from, ItemStack to) {
		Modifier fromMod = ModifierHandler.getModifier(from);
		if (fromMod != null) {
			ModifierHandler.removeEquipmentModifier((LivingEntity) (Object) this, fromMod, slotType);
		}
		Modifier toMod = ModifierHandler.getModifier(to);
		if (toMod == null) {
			toMod = ModifierHandler.rollModifier(to, this.level().random);
			if (toMod == null) return;
			ModifierHandler.setModifier(to, toMod);
		}
		ModifierHandler.applyEquipmentModifier((LivingEntity) (Object) this, toMod, slotType);
	}
}
