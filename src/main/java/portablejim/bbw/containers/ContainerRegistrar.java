package portablejim.bbw.containers;

import portablejim.bbw.BetterBuildersWandsMod;
import portablejim.bbw.containers.handlers.handlerCapability;

/**
 * Created by james on 28/12/16.
 */
public class ContainerRegistrar {
    public static void register() {
        BetterBuildersWandsMod.instance.containerManager.register(new handlerCapability());
    }
}
