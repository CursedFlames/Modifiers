package cursedflames.modifiers.common.modifier.curio.modifiers;

import cursedflames.modifiers.common.modifier.curio.ModifierBase;

public class ModifierNone extends ModifierBase {
	@Override
	public int getWeight() {
		// Generation of this modifier is special-cased
		return 0;
	}
}