package portablejim.bbw.containers;

import net.minecraftforge.fml.common.Loader;
import portablejim.bbw.BetterBuildersWandsMod;
import portablejim.bbw.containers.handlers.HandlerBotania;
import portablejim.bbw.containers.handlers.HandlerCapability;

/**
 * Created by james on 28/12/16.
 */
public class ContainerRegistrar {
    public static void register() {
        BetterBuildersWandsMod.instance.containerManager.register(new HandlerCapability());
        if(Loader.isModLoaded("Botania") || Loader.isModLoaded("botania")) {
            BetterBuildersWandsMod.instance.containerManager.register(new HandlerBotania());
        }
    }
}
