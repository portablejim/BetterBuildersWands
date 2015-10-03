import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import portablejim.bbw.core.items.UnbreakableWand;

/**
 * Created by james on 3/10/15.
 */
@Mod(modid = BetterBuildersWandsMod.MODID, version = BetterBuildersWandsMod.VERSION)
public class BetterBuildersWandsMod {
    public static final String MODID = "BetterBuildersWands";
    public static final String VERSION = "0.1";

    public static UnbreakableWand unbreakableWand;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        unbreakableWand = new UnbreakableWand();

        GameRegistry.registerItem(unbreakableWand, "unbreakableWand");
    }
}
