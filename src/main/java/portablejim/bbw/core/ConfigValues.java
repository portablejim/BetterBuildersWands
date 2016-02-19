package portablejim.bbw.core;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

/**
 * Config file
 */
public class ConfigValues {
    private Configuration configFile;

    public static final String CONFIG_WHYNOT = "why_not";
    public static final String CONFIG_GENERAL = "general";

    public boolean NO_EXTRA_UTILS_RECIPES;
    public static final boolean NO_EXTRA_UTILS_RECIPES_DEFAULT = false;
    public static final String NO_EXTRA_UTILS_RECIPES_NAME = "straymav_ultimate_wand";
    public static final String NO_EXTRA_UTILS_RECIPES_DESCRIPTION = "For those that don't like Extra Utils progression.";

    public boolean ENABLE_STONE_WAND;
    public static final boolean ENABLE_STONE_WAND_DEFAULT = true;
    public static final String ENABLE_STONE_WAND_NAME = "enable_stone_wand";
    public static final String ENABLE_STONE_WAND_DESCRIPTION = "Enable recipe for stone builder's wand";

    public boolean ENABLE_IRON_WAND;
    public static final boolean ENABLE_IRON_WAND_DEFAULT = true;
    public static final String ENABLE_IRON_WAND_NAME = "enable_iron_wand";
    public static final String ENABLE_IRON_WAND_DESCRIPTION = "Enable recipe for iron builder's wand";

    public boolean ENABLE_DIAMOND_WAND;
    public static final boolean ENABLE_DIAMOND_WAND_DEFAULT = true;
    public static final String ENABLE_DIAMOND_WAND_NAME = "enable_diamond_wand";
    public static final String ENABLE_DIAMOND_WAND_DESCRIPTION = "Enable recipe for diamond builder's wand";

    public String OVERRIDES_RECIPES;
    public static final String OVERRIDES_RECIPES_DEFAULT = "minecraft:lapis_ore/0=>1*minecraft:lapis_ore/4=>minecraft:lapis_ore/0,minecraft:lit_redstone_ore/0=>1*minecraft:redstone_ore/0=>minecraft:lit_redstone_ore/0";
    public static final String OVERRIDES_RECIPES_NAME = "forced_blocks";
    public static final String OVERRIDES_RECIPES_DESCRIPTION = "Specify forced mappings for what to build from certain blocks.\n(what you are looking at)=>(number required)*(item required)=>(block to build)";

    public ConfigValues(File file) {
        configFile = new Configuration(file);
    }

    public void loadConfigFile() {
        NO_EXTRA_UTILS_RECIPES = configFile.getBoolean(NO_EXTRA_UTILS_RECIPES_NAME, CONFIG_WHYNOT, NO_EXTRA_UTILS_RECIPES_DEFAULT, NO_EXTRA_UTILS_RECIPES_DESCRIPTION);
        OVERRIDES_RECIPES = configFile.get(CONFIG_GENERAL, OVERRIDES_RECIPES_NAME, OVERRIDES_RECIPES_DEFAULT, OVERRIDES_RECIPES_DESCRIPTION).getString();
        ENABLE_STONE_WAND = configFile.get(CONFIG_GENERAL, ENABLE_STONE_WAND_NAME, ENABLE_STONE_WAND_DEFAULT, ENABLE_STONE_WAND_DESCRIPTION).getBoolean();
        ENABLE_IRON_WAND = configFile.get(CONFIG_GENERAL, ENABLE_IRON_WAND_NAME, ENABLE_IRON_WAND_DEFAULT, ENABLE_IRON_WAND_DESCRIPTION).getBoolean();
        ENABLE_DIAMOND_WAND = configFile.get(CONFIG_GENERAL, ENABLE_DIAMOND_WAND_NAME, ENABLE_DIAMOND_WAND_DEFAULT, ENABLE_DIAMOND_WAND_DESCRIPTION).getBoolean();

        configFile.save();
    }
}
