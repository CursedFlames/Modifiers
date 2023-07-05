package cursedflames.modifiers.forge;

import java.util.ArrayList;
import java.util.List;

import cursedflames.modifiers.ModifiersMod;
import cursedflames.modifiers.common.curio.ICurioProxy;
import cursedflames.modifiers.common.item.ItemModifierBook;
import cursedflames.modifiers.common.network.NetworkHandler;
import cursedflames.modifiers.forge.network.NetworkHandlerForge;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(ModifiersMod.MODID)
public class ModifiersModForge extends ModifiersMod {
	public ModifiersModForge() {
		NetworkHandler.register();
		// Register the setup method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		// Register the doClientStuff method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);

		// Register ourselves for server and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);
		FMLJavaModLoadingContext.get().getModEventBus().register(this);
	}

	static {
		NetworkHandler.setProxy(new NetworkHandlerForge());
	}

	private void setup(final FMLCommonSetupEvent event) {
//		if (ModList.get().isLoaded("curios")) {
//			try {
//				curioProxy = (ICurioProxy) Class.forName("cursedflames.modifiers.forge.curio.CurioCompat").newInstance();
//				MinecraftForge.EVENT_BUS.register(curioProxy);
//			} catch (ClassNotFoundException | IllegalAccessException | InstantiationException | ClassCastException e) {
//				e.printStackTrace();
//				// FIXME probably don't want this in non-debug releases
//				throw new Error("Failed to load Curios compatibility. Go tell CursedFlames that her mod is broken.");
//			}
//		}
		if (curioProxy == null) {
			// Dummy implementation that does nothing
			curioProxy = new ICurioProxy() {};
		}
	}

	private void doClientStuff(final FMLClientSetupEvent event) {
	}

	private void enqueueIMC(final InterModEnqueueEvent event) {
//		SlotTypePreset[] slots = {
//				SlotTypePreset.HEAD, SlotTypePreset.NECKLACE, SlotTypePreset.BACK, SlotTypePreset.BODY,
//				SlotTypePreset.HANDS, SlotTypePreset.RING, SlotTypePreset.CHARM
//		};
//		List<SlotTypeMessage.Builder> builders = new ArrayList<>();
//		for (SlotTypePreset slot : slots) {
//			SlotTypeMessage.Builder builder = slot.getMessageBuilder();
//			if (slot == SlotTypePreset.RING) {
//				builder.size(2);
//			}
//			builders.add(builder);
//		}
//		for (SlotTypeMessage.Builder builder : builders) {
//			SlotTypeMessage message = builder.build();
//			InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE,
//					()->message);
//		}
	}

	@SubscribeEvent
	public void register(RegisterEvent event) {
		event.register(ForgeRegistries.ITEMS.getRegistryKey(), helper -> {
			modifier_book = new ItemModifierBook();
			helper.register(new ResourceLocation(MODID, "modifier_book"), modifier_book);
		});
		event.register(Registries.CREATIVE_MODE_TAB, helper -> {
			GROUP_BOOKS = CreativeModeTab
					.builder()
					.withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
					.title(Component.translatable("itemGroup.modifiers_books"))
					.icon(() -> new ItemStack(modifier_book))
					.displayItems((params, output) -> output.acceptAll(modifier_book.getStacksForCreativeTab()))
					.build();
			helper.register(new ResourceLocation(MODID, "books"), GROUP_BOOKS);
		});
	}
}
