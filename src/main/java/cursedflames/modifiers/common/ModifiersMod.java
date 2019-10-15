package cursedflames.modifiers.common;

import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cursedflames.modifiers.common.config.Config;
import cursedflames.modifiers.common.item.ModItems;
import cursedflames.modifiers.common.modifier.EventHandler;
import cursedflames.modifiers.common.modifier.curio.EventHandlerCurio;
import cursedflames.modifiers.common.modifier.curio.IModifierCurio;
import cursedflames.modifiers.common.modifier.curio.ModifierCurioRegistry;
import cursedflames.modifiers.common.network.PacketHandler;
import cursedflames.modifiers.common.proxy.ClientProxy;
import cursedflames.modifiers.common.proxy.IProxy;
import cursedflames.modifiers.common.proxy.ServerProxy;
import cursedflames.modifiers.common.reforge.ContainerReforge;
import cursedflames.modifiers.common.reforge.EventHandlerReforger;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import top.theillusivec4.curios.api.CuriosAPI;
import top.theillusivec4.curios.api.imc.CurioIMCMessage;

//The value here should match an entry in the META-INF/mods.toml file
@Mod(ModifiersMod.MODID)
public class ModifiersMod { //TODO ensure missing modifiers handled correctly
	public static final String MODID = "modifiers";

	// Directly reference a log4j logger.
	public static final Logger logger = LogManager.getLogger();
	
	public static IProxy proxy = DistExecutor.runForDist(
			() -> () -> new ClientProxy(),
			() -> () -> new ServerProxy());

	public ModifiersMod() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);
		
		// Register the setup method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		// Register the enqueueIMC method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
		// Register the processIMC method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
		// Register the doClientStuff method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerRegistries);
		
		
		Config.loadConfig(Config.CLIENT_CONFIG, FMLPaths.CONFIGDIR.get().resolve(MODID+"-client.toml"));
		Config.loadConfig(Config.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve(MODID+"-common.toml"));

		// Register ourselves for server and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);
//		MinecraftForge.EVENT_BUS.register(ModifierCurioRegistry.class);
		MinecraftForge.EVENT_BUS.register(EventHandler.class);
		MinecraftForge.EVENT_BUS.register(EventHandlerReforger.class);
		MinecraftForge.EVENT_BUS.register(EventHandlerCurio.class);
		
		PacketHandler.registerMessages(); // TODO where are we supposed to do this?
	}

	private void setup(final FMLCommonSetupEvent event) {
		proxy.init(event);
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
		// FIXME REMOVE TESTING
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
		public static void onItemRegistry(final RegistryEvent.Register<Item> event) {
			ModItems.registerItems(event);
		}
		
		@SubscribeEvent
		public static void onContainerRegistry(final RegistryEvent.Register<ContainerType<?>> event) {
			event.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> {
				BlockPos pos = data.readBlockPos();
				return new ContainerReforge(windowId, proxy.getClientWorld(), pos, inv, proxy.getClientPlayer());
			}).setRegistryName(MODID, "reforge"));
		}
		
		@SubscribeEvent
		public static void onModifierCurioRegistry(final RegistryEvent.Register<IModifierCurio> event) {
			ModifierCurioRegistry.registerCurioModifiers(event);
		}
	}
}
