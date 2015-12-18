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

        configFile.save();
    }
}
