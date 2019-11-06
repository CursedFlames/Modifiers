package cursedflames.modifiers.common.modifier.effect;

import java.util.HashMap;
import java.util.Map;

import cursedflames.modifiers.common.modifier.curio.IModifierEffect;
import net.minecraft.nbt.CompoundNBT;

public class ModifierEffectTypes {
	// TODO IMC stuff for adding to this
	public static interface IEffectType {
		public IModifierEffect fromNBT(CompoundNBT tag);
	}
	public static class EffectTypeAttributes implements IEffectType {
		@Override
		public IModifierEffect fromNBT(CompoundNBT tag) {
			// FIXME Auto-generated method stub
			return null;
		}
	}
	public static EffectTypeAttributes attributes = new EffectTypeAttributes();
	public static Map<String, IEffectType> types = new HashMap<>();
	static {
		types.put("attributes", attributes);
	}
}
