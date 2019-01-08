package portablejim.bbw;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.IngredientNBT;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.ShapedOreRecipe;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.simple.SimpleLogger;
import org.apache.logging.log4j.util.PropertiesUtil;
import portablejim.bbw.containers.ContainerManager;
import portablejim.bbw.containers.ContainerRegistrar;
import portablejim.bbw.core.ConfigValues;
import portablejim.bbw.core.OopsCommand;
import portablejim.bbw.core.conversion.CustomMappingManager;
import portablejim.bbw.core.conversion.StackedBlockManager;
import portablejim.bbw.core.items.ItemRestrictedWandAdvanced;
import portablejim.bbw.core.items.ItemRestrictedWandBasic;
import portablejim.bbw.core.items.ItemUnrestrictedWand;
import portablejim.bbw.core.wands.RestrictedWand;
import portablejim.bbw.core.wands.UnbreakingWand;
import portablejim.bbw.network.PacketWandActivate;
import portablejim.bbw.proxy.IProxy;

import java.util.function.Function;

/**
 * Author: Portablejim
 */
@Mod(modid = BetterBuildersWandsMod.MODID, acceptedMinecraftVersions = "[1.12,1.13)")
public class BetterBuildersWandsMod {
    public static final String MODID = "betterbuilderswands";
    public static final String LANGID = "bbw";

    @Mod.Instance
    public static BetterBuildersWandsMod instance;

    @SidedProxy(modId = MODID, clientSide = "portablejim.bbw.proxy.ClientProxy", serverSide = "portablejim.bbw.proxy.ServerProxy")
    public static IProxy proxy;

    public ConfigValues configValues;

    public ContainerManager containerManager = new ContainerManager();

    public static Logger logger = new SimpleLogger("BetterBuildersWand", Level.ALL, true, false, true, false, "YYYY-MM-DD", null, PropertiesUtil.getProperties(), null);

    public static ItemRestrictedWandBasic itemStoneWand;
    public static ItemRestrictedWandAdvanced itemIronWand;
    public static ItemUnrestrictedWand itemDiamondWand;
    public static ItemUnrestrictedWand itemUnbreakableWand;

    public SimpleNetworkWrapper networkWrapper;

    // Caches calls to Block.getStackedBlock(int)
    public StackedBlockManager blockCache;
    public CustomMappingManager mappingManager;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();

        configValues = new ConfigValues(event.getSuggestedConfigurationFile());
        configValues.loadConfigFile();

        networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel("bbwands");
        networkWrapper.registerMessage(PacketWandActivate.Handler.class, PacketWandActivate.class, 0, Side.SERVER);

        int diamondWandLimit = configValues.DIAMOND_WAND_LIMIT < 0 ? Item.ToolMaterial.DIAMOND.getMaxUses() : configValues.DIAMOND_WAND_LIMIT;

        itemStoneWand = new ItemRestrictedWandBasic(new RestrictedWand(5));
        itemIronWand = new ItemRestrictedWandAdvanced(new RestrictedWand(9));
        itemDiamondWand = new ItemUnrestrictedWand(new RestrictedWand(diamondWandLimit), "unrestricted", "diamond");
        itemDiamondWand.setMaxDamage(Item.ToolMaterial.DIAMOND.getMaxUses());
        itemUnbreakableWand = new ItemUnrestrictedWand(new UnbreakingWand(), "unbreakable", "unbreakable");

        itemStoneWand.setRegistryName("wandStone");
        itemIronWand.setRegistryName("wandIron");
        itemDiamondWand.setRegistryName("wandDiamond");
        itemUnbreakableWand.setRegistryName("wandUnbreakable");

        MinecraftForge.EVENT_BUS.register(this);

        blockCache = new StackedBlockManager();
        mappingManager = new CustomMappingManager();

        /*mappingManager.setMapping(new CustomMapping(Blocks.lapis_ore, 0, new ItemStack(Blocks.lapis_ore, 1, 4), Blocks.lapis_ore, 0));
        mappingManager.setMapping(new CustomMapping(Blocks.lit_redstone_ore, 0, new ItemStack(Blocks.redstone_ore, 1, 0), Blocks.lit_redstone_ore, 0));*/
    }

    @SubscribeEvent
    public void registration(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(itemStoneWand);
        event.getRegistry().register(itemIronWand);
        event.getRegistry().register(itemDiamondWand);
        event.getRegistry().register(itemUnbreakableWand);
    }

    private IRecipe wandRecipe(String variant, Item output, String material) {
        return wandRecipe(variant, new ItemStack(output), material);
    }

    private IRecipe wandRecipe(String variant, ItemStack output, String material) {
        ResourceLocation recipeName = new ResourceLocation(MODID, "recipewand" + variant);
        ShapedOreRecipe recipe = new ShapedOreRecipe(recipeName, output, "  H", " S ", "S  ", 'S', "stickWood", 'H', material);
        recipe.setRegistryName(recipeName);
        return recipe;
    }

    private IRecipe wandRecipe(String variant, ItemStack output, Item material) {
        ResourceLocation recipeName = new ResourceLocation(MODID, "recipewand" + variant);
        ShapedOreRecipe recipe = new ShapedOreRecipe(recipeName, output, "  H", " S ", "S  ", 'S', "stickWood", 'H', material);
        recipe.setRegistryName(recipeName);
        return recipe;
    }

    @SubscribeEvent
    public void recipes(RegistryEvent.Register<IRecipe> event) {
        if(configValues.ENABLE_STONE_WAND) event.getRegistry().register(wandRecipe("stone", itemStoneWand, "cobblestone"));
        if(configValues.ENABLE_IRON_WAND) event.getRegistry().register(wandRecipe("iron", itemIronWand, "ingotIron"));
        if(configValues.ENABLE_DIAMOND_WAND) event.getRegistry().register(wandRecipe("diamond", itemDiamondWand, "gemDiamond"));

        Function<Integer,Boolean> unbreakableWandRecipe = damage -> {
            GameRegistry.addShapelessRecipe(
                    new ResourceLocation(MODID, "wandunbreakable" + damage),
                    new ResourceLocation(MODID, "wandunbreakable" + damage),
                    newWand(damage),
                    Ingredient.fromStacks(newWand(damage - 1)),
                    Ingredient.fromStacks(newWand(damage - 1))
            );
            return true;
        };

        boolean EXTRA_UTILS_RECIPES = !configValues.NO_EXTRA_UTILS_RECIPES;
        if(Loader.isModLoaded("ExtraUtilities") && EXTRA_UTILS_RECIPES) {
            Item buildersWand = Item.REGISTRY.getObject(new ResourceLocation("ExtraUtilities", "builderswand"));
            Item creativebuildersWand = Item.REGISTRY.getObject(new ResourceLocation("ExtraUtilities", "creativebuilderswand"));

            event.getRegistry().register(wandRecipe("unbreakabletiny", newWand(4), buildersWand));
            event.getRegistry().register(wandRecipe("unbreakable", newWand(12), creativebuildersWand));
            unbreakableWandRecipe.apply(5);
            unbreakableWandRecipe.apply(6);
        }
        else {
            event.getRegistry().register(wandRecipe("unbreakable", newWand(12), "netherStar"));
        }
        unbreakableWandRecipe.apply(13);
        unbreakableWandRecipe.apply(14);
    }

    @SubscribeEvent
    public void models(ModelRegistryEvent event) {
        proxy.RegisterModels();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        mappingManager.loadConfig(configValues.OVERRIDES_RECIPES);
    }

    private ItemStack newWand(int damage) {
        return new ItemStack(BetterBuildersWandsMod.itemUnbreakableWand, 1, damage);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.RegisterEvents();

        boolean EXTRA_UTILS_RECIPES = !configValues.NO_EXTRA_UTILS_RECIPES;
        if(Loader.isModLoaded("ExtraUtilities") && EXTRA_UTILS_RECIPES) {
            itemUnbreakableWand.addSubMeta(4);
            itemUnbreakableWand.addSubMeta(5);
            itemUnbreakableWand.addSubMeta(6);
        }
        itemUnbreakableWand.addSubMeta(12);
        itemUnbreakableWand.addSubMeta(13);
        itemUnbreakableWand.addSubMeta(14);

        ContainerRegistrar.register();
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new OopsCommand());
    }
}
