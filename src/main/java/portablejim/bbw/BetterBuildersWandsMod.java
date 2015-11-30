package portablejim.bbw;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.simple.SimpleLogger;
import org.apache.logging.log4j.util.PropertiesUtil;
import portablejim.bbw.core.BlockEvents;
import portablejim.bbw.core.items.ItemRestrictedWandAdvanced;
import portablejim.bbw.core.items.ItemRestrictedWandBasic;
import portablejim.bbw.core.items.ItemUnrestrictedWand;
import portablejim.bbw.core.wands.RestrictedWand;
import portablejim.bbw.core.wands.UnbreakingWand;
import portablejim.bbw.network.PacketWandActivate;
import portablejim.bbw.proxy.IProxy;

/**
 * Author: Portablejim
 */
@Mod(modid = BetterBuildersWandsMod.MODID, version = BetterBuildersWandsMod.VERSION)
public class BetterBuildersWandsMod {
    public static final String MODID = "betterbuilderswands";
    public static final String VERSION = "0.1";
    public static final String LANGID = "bbw";

    @Mod.Instance
    public static BetterBuildersWandsMod instance;

    @SidedProxy(modId = MODID, clientSide = "portablejim.bbw.proxy.ClientProxy", serverSide = "portablejim.bbw.proxy.ServerProxy")
    public static IProxy proxy;

    public static Logger logger = new SimpleLogger("BetterBuildersWand", Level.ALL, true, false, true, false, "YYYY-MM-DD", null, PropertiesUtil.getProperties(), null);

    public static ItemRestrictedWandBasic itemStoneWand;
    public static ItemRestrictedWandAdvanced itemIronWand;
    public static ItemUnrestrictedWand itemDiamondWand;
    public static ItemUnrestrictedWand itemUnbreakableWand;

    public SimpleNetworkWrapper networkWrapper;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();

        itemStoneWand = new ItemRestrictedWandBasic(new RestrictedWand(5));
        itemIronWand = new ItemRestrictedWandAdvanced(new RestrictedWand(9));
        itemDiamondWand = new ItemUnrestrictedWand(new RestrictedWand(Item.ToolMaterial.EMERALD.getMaxUses()), "Unrestricted", "Diamond");
        itemDiamondWand.setMaxDamage(Item.ToolMaterial.EMERALD.getMaxUses());
        itemUnbreakableWand = new ItemUnrestrictedWand(new UnbreakingWand(), "Unbreakable", "Unbreakable");
        GameRegistry.registerItem(itemStoneWand, "wandStone");
        GameRegistry.registerItem(itemIronWand, "wandIron");
        GameRegistry.registerItem(itemDiamondWand, "wandDiamond");
        GameRegistry.registerItem(itemUnbreakableWand, "wandUnbreakable");

        networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel("bbwands");
        networkWrapper.registerMessage(PacketWandActivate.Handler.class, PacketWandActivate.class, 0, Side.SERVER);

    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new BlockEvents());
        proxy.RegisterEvents();
    }

    @EventHandler
    public void keypress(InputEvent event) {
        //FMLClientHandler.instance().
    }
}
