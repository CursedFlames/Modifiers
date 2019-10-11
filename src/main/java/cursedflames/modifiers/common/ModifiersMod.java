package cursedflames.modifiers.common;

import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cursedflames.modifiers.common.modifier.curio.EventHandlerCurio;
import cursedflames.modifiers.common.modifier.curio.IModifierCurio;
import cursedflames.modifiers.common.modifier.curio.ModifierCurioRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import top.theillusivec4.curios.api.CuriosAPI;
import top.theillusivec4.curios.api.imc.CurioIMCMessage;

//The value here should match an entry in the META-INF/mods.toml file
@Mod(ModifiersMod.MODID)
public class ModifiersMod {
	public static final String MODID = "modifiers";

	// Directly reference a log4j logger.
	public static final Logger logger = LogManager.getLogger();

	public ModifiersMod() {
		// Register the setup method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		// Register the enqueueIMC method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
		// Register the processIMC method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
		// Register the doClientStuff method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerRegistries);

		// Register ourselves for server and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);
//		MinecraftForge.EVENT_BUS.register(ModifierCurioRegistry.class);
		MinecraftForge.EVENT_BUS.register(EventHandlerCurio.class);
	}

	private void setup(final FMLCommonSetupEvent event) {
		// some preinit code
		logger.info("HELLO FROM PREINIT");
		logger.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
	}
	
	private void registerRegistries(final RegistryEvent.NewRegistry event) {
		logger.info("ASDFASDF");
		ModifierCurioRegistry.createRegistry(event);
	}

	private void doClientStuff(final FMLClientSetupEvent event) {
		// do something that can only be done on the client
		logger.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
	}

	private void enqueueIMC(final InterModEnqueueEvent event) {
		// some example code to dispatch IMC to another mod
		InterModComms.sendTo("examplemod", "helloworld", () -> {
			logger.info("Hello world from the MDK");
			return "Hello world";
		});
		// FIXME remove this, this is just for testing
		String[] slots = {"necklace", "head"};
		// idk if there's a way to register multiple with one message
		for (String slot : slots) {
			InterModComms.sendTo("curios", CuriosAPI.IMC.REGISTER_TYPE, () -> {
				return new CurioIMCMessage(slot);
			});
		}
		InterModComms.sendTo("curios", CuriosAPI.IMC.REGISTER_TYPE, () -> {
			return new CurioIMCMessage("ring").setSize(2);
		});
	}

	private void processIMC(final InterModProcessEvent event) {
		// some example code to receive and process InterModComms from other mods
		logger.info("Got IMC {}",
				event.getIMCStream().map(m -> m.getMessageSupplier().get()).collect(Collectors.toList()));
	}

	// You can use SubscribeEvent and let the Event Bus discover methods to call
	@SubscribeEvent
	public void onServerStarting(FMLServerStartingEvent event) {
		// do something when the server starts
		logger.info("HELLO from server starting");
	}

	// You can use EventBusSubscriber to automatically subscribe events on the
	// contained class (this is subscribing to the MOD
	// Event bus for receiving Registry Events)
	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents {
		@SubscribeEvent
		public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
			// register a new block here
			logger.info("HELLO from Register Block");
		}
		
		@SubscribeEvent
		public static void onModifierCurioRegistry(final RegistryEvent.Register<IModifierCurio> event) {
			ModifierCurioRegistry.registerCurioModifiers(event);
		}
	}
}
