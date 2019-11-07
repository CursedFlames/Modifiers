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
	public static final String CATEGORY_REFORGE = "reforge";
	
	public static final String SUBCAT_CURIO = "curio";
	
	private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
	private static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

	public static ForgeConfigSpec COMMON_CONFIG;
	public static ForgeConfigSpec CLIENT_CONFIG;
	
	public static ForgeConfigSpec.BooleanValue CURIO_IS_WHITELIST;
	public static ConfigValue<List<? extends String>> CURIO_SLOTS_ALLOWED;

	public static ForgeConfigSpec.BooleanValue REFORGE_ENABLED;
	
	public static ForgeConfigSpec.IntValue REFORGE_COST_MIN;
	public static ForgeConfigSpec.IntValue REFORGE_COST_MAX;

	static {
		COMMON_BUILDER.comment("General configuration").push(CATEGORY_GENERAL);
		setupGeneralConfig();
		COMMON_BUILDER.pop();
		
		COMMON_BUILDER.comment("Reforger configuration").push(CATEGORY_REFORGE);
		setupReforgeConfig();
		COMMON_BUILDER.pop();
		
		COMMON_CONFIG = COMMON_BUILDER.build();
		CLIENT_CONFIG = CLIENT_BUILDER.build();
	}
	
	private static void setupGeneralConfig() {
		COMMON_BUILDER.comment("Curios").push(SUBCAT_CURIO);
		CURIO_IS_WHITELIST = COMMON_BUILDER
				.comment("If true, the list of curio modifier slots is a whitelist, otherwise it's a blacklist")
				.define("curio_is_whitelist", false);
		CURIO_SLOTS_ALLOWED = COMMON_BUILDER
				.comment("Blacklist/Whitelist of curio slots that allow modifiers",
						"Set curio_is_whitelist to control whether this is a whitelist or blacklist.")
				.defineList("curio_slots_list", Arrays.asList("example1", "example2"),
						String.class::isInstance);
		COMMON_BUILDER.pop();
	}

	private static void setupReforgeConfig() {
		REFORGE_ENABLED = COMMON_BUILDER
				.comment("Whether the reforger gui, accessed by right clicking the smithing station, is enabled",
						"If disabled, items will not be reforgeable")
				.define("reforge_enabled", true);
		
		REFORGE_COST_MIN = COMMON_BUILDER
				.comment("Minimum cost, in XP points, to reforge an item")
				.defineInRange("reforge_cost_min", 80, 1, Integer.MAX_VALUE/100);
		REFORGE_COST_MAX = COMMON_BUILDER
				.comment("Maximum cost, in XP points, to reforge an item")
				.defineInRange("reforge_cost_max", 160, 1, Integer.MAX_VALUE/100);
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