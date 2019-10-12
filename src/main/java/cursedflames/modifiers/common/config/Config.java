package cursedflames.modifiers.common.config;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod.EventBusSubscriber
public class Config {
	public static final String CATEGORY_GENERAL = "general";
	
	private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
	private static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

	public static ForgeConfigSpec COMMON_CONFIG;
	public static ForgeConfigSpec CLIENT_CONFIG;
	
	public static ForgeConfigSpec.BooleanValue CURIO_IS_WHITELIST;
	public static ConfigValue<List<? extends String>> CURIO_SLOTS_ALLOWED;
//
//	public static ForgeConfigSpec.BooleanValue MAGIC_MIRROR_INTERDIMENSIONAL;
//	
//	public static ForgeConfigSpec.BooleanValue BROKEN_HEART_REGEN;
//	public static ForgeConfigSpec.DoubleValue BROKEN_HEART_REGEN_AMOUNT;
	

	static {
		COMMON_BUILDER.comment("General configuration").push(CATEGORY_GENERAL);
		setupGeneralConfig();
		COMMON_BUILDER.pop();
		
//		COMMON_BUILDER.comment("Item configuration").push(CATEGORY_ITEMS);
//		setupItemConfig();
//		COMMON_BUILDER.pop();
		
		COMMON_CONFIG = COMMON_BUILDER.build();
		CLIENT_CONFIG = CLIENT_BUILDER.build();
	}
	
	private static void setupGeneralConfig() {
//		COMMON_BUILDER.comment("Recipe settings").push(SUBCAT_RECIPES);
		CURIO_IS_WHITELIST = COMMON_BUILDER
				.comment("If true, the list of curio modifier slots is a whitelist, otherwise it's a blacklist")
				.define("curio_is_whitelist", false);
		CURIO_SLOTS_ALLOWED = COMMON_BUILDER
				.comment("Blacklist/Whitelist of curio slots that allow modifiers",
						"Set curio_is_whitelist to control whether this is a whitelist or blacklist.")
				.defineList("curio_slots_list", Arrays.asList("example1", "example2"),
						String.class::isInstance);
//		COMMON_BUILDER.pop();
	}

	public static void loadConfig(ForgeConfigSpec spec, Path path) {

		final CommentedFileConfig configData = CommentedFileConfig.builder(path)
				.sync()
				.autosave()
				.writingMode(WritingMode.REPLACE)
				.build();

		configData.load();
		spec.setConfig(configData);
	}

	@SubscribeEvent
	public static void onLoad(final ModConfig.Loading configEvent) {

	}

	@SubscribeEvent
	public static void onReload(final ModConfig.ConfigReloading configEvent) {
	}
}