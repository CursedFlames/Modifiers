package cursedflames.modifiers;

import cursedflames.modifiers.common.curio.ICurioProxy;
import cursedflames.modifiers.common.item.ItemModifierBook;
import cursedflames.modifiers.common.network.NetworkHandlerProxy;
import net.minecraft.item.ItemGroup;

public class ModifiersMod {

    public static final String MODID = "modifiers";

    public static NetworkHandlerProxy networkProxy;

    public static ICurioProxy curioProxy;

    public static ItemModifierBook modifier_book;

    // TODO sort creative tab/JEI
    public static ItemGroup GROUP_BOOKS;
}
