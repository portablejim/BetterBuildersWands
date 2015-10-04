import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraftforge.common.MinecraftForge;
import portablejim.bbw.core.BlockEvents;
import portablejim.bbw.core.items.UnbreakableWand;

/**
 * Author: Portablejim
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

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new BlockEvents());
    }
}
