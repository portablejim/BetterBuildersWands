import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraftforge.common.MinecraftForge;
import portablejim.bbw.core.BlockEvents;
import portablejim.bbw.core.items.ItemUnbreakableWand;

/**
 * Author: Portablejim
 */
@Mod(modid = BetterBuildersWandsMod.MODID, version = BetterBuildersWandsMod.VERSION)
public class BetterBuildersWandsMod {
    public static final String MODID = "betterbuilderswands";
    public static final String VERSION = "0.1";

    public static ItemUnbreakableWand itemUnbreakableWand;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        itemUnbreakableWand = new ItemUnbreakableWand();

        GameRegistry.registerItem(itemUnbreakableWand, "unbreakableWand");
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new BlockEvents());
    }
}
