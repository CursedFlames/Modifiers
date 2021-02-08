package cursedflames.modifiers.forge;

import java.util.ArrayList;
import java.util.List;

import cursedflames.modifiers.ModifiersMod;
import cursedflames.modifiers.common.curio.ICurioProxy;
import cursedflames.modifiers.common.item.ItemModifierBook;
import cursedflames.modifiers.common.network.NetworkHandler;
import cursedflames.modifiers.forge.network.NetworkHandlerForge;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(ModifiersMod.MODID)
public class ModifiersModForge extends ModifiersMod {
	public ModifiersModForge() {
		// Register the setup method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		// Register the doClientStuff method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);

		// Register ourselves for server and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);
	}

	static {
		NetworkHandler.setProxy(new NetworkHandlerForge());

		GROUP_BOOKS = new ItemGroup(-1, ModifiersMod.MODID+"_books") {
			@Override
			public ItemStack createIcon() {
				return new ItemStack(modifier_book);
			}
		};
	}

	private void setup(final FMLCommonSetupEvent event) {
		System.out.println(ModList.get().getMods());
		if (ModList.get().isLoaded("curios")) {
			try {
				curioProxy = (ICurioProxy) Class.forName("cursedflames.modifiers.forge.curio.CurioCompat").newInstance();
				MinecraftForge.EVENT_BUS.register(curioProxy);
			} catch (ClassNotFoundException | IllegalAccessException | InstantiationException | ClassCastException e) {
				e.printStackTrace();
				// FIXME probably don't want this in non-debug releases
				throw new Error();
			}
		}
		if (curioProxy == null) {
			// Dummy implementation that does nothing
			curioProxy = new ICurioProxy() {};
		}
	}

	private void doClientStuff(final FMLClientSetupEvent event) {
	}

	private void enqueueIMC(final InterModEnqueueEvent event) {
		// some example code to dispatch IMC to another mod
		// FIXME testing only
		SlotTypePreset[] slots = {
				SlotTypePreset.HEAD, SlotTypePreset.NECKLACE, SlotTypePreset.BACK, SlotTypePreset.BODY,
				SlotTypePreset.HANDS, SlotTypePreset.RING, SlotTypePreset.CHARM
		};
		List<SlotTypeMessage.Builder> builders = new ArrayList<>();
		for (SlotTypePreset slot : slots) {
			SlotTypeMessage.Builder builder = slot.getMessageBuilder();
			if (slot == SlotTypePreset.RING) {
				builder.size(2);
			}
			builders.add(builder);
		}
		for (SlotTypeMessage.Builder builder : builders) {
			SlotTypeMessage message = builder.build();
			InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE,
					()->message);
		}
	}

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents {
		@SubscribeEvent
		public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
			modifier_book = new ItemModifierBook();
			modifier_book.setRegistryName(new Identifier(MODID, "modifier_book"));
			event.getRegistry().register(modifier_book);
		}
	}
}
