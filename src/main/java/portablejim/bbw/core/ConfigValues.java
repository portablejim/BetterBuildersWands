package portablejim.bbw.core;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

/**
 * Created by james on 16/12/15.
 */
public class ConfigValues {
    private Configuration configFile;

    public static final String CONFIG_WHYNOT = "why_not";

    public boolean NO_EXTRA_UTILS_RECIPES;
    public static final boolean NO_EXTRA_UTILS_RECIPES_DEFAULT = false;
    public static final String NO_EXTRA_UTILS_RECIPES_NAME = "straymav_ultimate_wand";
    public static final String NO_EXTRA_UTILS_RECIPES_DESCRIPTION = "For those that don't like Extra Utils progression.";

    public ConfigValues(File file) {
        configFile = new Configuration(file);
    }

    public void loadConfigFile() {
        NO_EXTRA_UTILS_RECIPES = configFile.getBoolean(NO_EXTRA_UTILS_RECIPES_NAME, CONFIG_WHYNOT, NO_EXTRA_UTILS_RECIPES_DEFAULT, NO_EXTRA_UTILS_RECIPES_DESCRIPTION);

        configFile.save();
    }
}
