package portablejim.bbw;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
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
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.ShapedOreRecipe;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Author: Portablejim
 */
@Mod(modid = BetterBuildersWandsMod.MODID, version = BetterBuildersWandsMod.VERSION)
public class BetterBuildersWandsMod {
    public static final String MODID = "betterbuilderswands";
    public static final String VERSION = "0.1.1";
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

        networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel("bbwands");
        networkWrapper.registerMessage(PacketWandActivate.Handler.class, PacketWandActivate.class, 0, Side.SERVER);

    }

    private ItemStack newWand(int damage) {
        return new ItemStack(BetterBuildersWandsMod.itemUnbreakableWand, 1, damage);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.RegisterEvents();

        itemStoneWand = new ItemRestrictedWandBasic(new RestrictedWand(5));
        itemIronWand = new ItemRestrictedWandAdvanced(new RestrictedWand(9));
        itemDiamondWand = new ItemUnrestrictedWand(new RestrictedWand(Item.ToolMaterial.EMERALD.getMaxUses()), "Unrestricted", "Diamond");
        itemDiamondWand.setMaxDamage(Item.ToolMaterial.EMERALD.getMaxUses());
        itemUnbreakableWand = new ItemUnrestrictedWand(new UnbreakingWand(), "Unbreakable", "Unbreakable");
        GameRegistry.registerItem(itemStoneWand, "wandStone");
        GameRegistry.registerItem(itemIronWand, "wandIron");
        GameRegistry.registerItem(itemDiamondWand, "wandDiamond");
        GameRegistry.registerItem(itemUnbreakableWand, "wandUnbreakable");

        GameRegistry.addRecipe(new ShapedOreRecipe(BetterBuildersWandsMod.itemStoneWand, "  H", " S ", "S  ", 'S', "stickWood", 'H', "cobblestone"));
        GameRegistry.addRecipe(new ShapedOreRecipe(BetterBuildersWandsMod.itemIronWand, "  H", " S ", "S  ", 'S', "stickWood", 'H', "ingotIron"));
        GameRegistry.addRecipe(new ShapedOreRecipe(BetterBuildersWandsMod.itemDiamondWand, "  H", " S ", "S  ", 'S', "stickWood", 'H', "gemDiamond"));

        if(Loader.isModLoaded("ExtraUtilities")) {
            Item buildersWand = GameRegistry.findItem("ExtraUtilities", "builderswand");
            Item creativebuildersWand = GameRegistry.findItem("ExtraUtilities", "creativebuilderswand");
            GameRegistry.addRecipe(new ShapedOreRecipe(newWand(4), "  H", " S ", "S  ", 'S', "stickWood", 'H', buildersWand));
            GameRegistry.addRecipe(new ShapedOreRecipe(newWand(12), "  H", " S ", "S  ", 'S', "stickWood", 'H', creativebuildersWand));
            GameRegistry.addRecipe(new ShapelessRecipes(newWand(5), Arrays.asList(newWand(4), newWand(4))));
            GameRegistry.addRecipe(new ShapelessRecipes(newWand(6), Arrays.asList(newWand(5), newWand(5))));
            itemUnbreakableWand.addSubMeta(4);
            itemUnbreakableWand.addSubMeta(5);
            itemUnbreakableWand.addSubMeta(6);
        }
        else {
            GameRegistry.addRecipe(new ShapedOreRecipe(newWand(12), "  H", " S ", "S  ", 'S', "stickWood", 'H', Items.nether_star));
        }
        itemUnbreakableWand.addSubMeta(12);
        itemUnbreakableWand.addSubMeta(13);
        itemUnbreakableWand.addSubMeta(14);
        GameRegistry.addRecipe(new ShapelessRecipes(newWand(13), Arrays.asList(newWand(12), newWand(12))));
        GameRegistry.addRecipe(new ShapelessRecipes(newWand(14), Arrays.asList(newWand(13), newWand(13))));
    }

    @EventHandler
    public void keypress(InputEvent event) {
        //FMLClientHandler.instance().
    }
}
