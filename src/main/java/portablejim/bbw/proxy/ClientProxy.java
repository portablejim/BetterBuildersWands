package portablejim.bbw.proxy;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import portablejim.bbw.BetterBuildersWandsMod;
import portablejim.bbw.core.BlockEvents;
import portablejim.bbw.core.client.KeyEvents;

/**
 * Client proxy for client-side only methods
 */
public class ClientProxy extends CommonProxy {
    private KeyEvents keyevents;
    private BlockEvents blockEvents;

    @Override
    public void RegisterEvents() {
        keyevents = new KeyEvents();
        blockEvents = new BlockEvents();
        MinecraftForge.EVENT_BUS.register(blockEvents);

    }

    @Override
    public void RegisterModels() {
        ModelLoader.setCustomModelResourceLocation(BetterBuildersWandsMod.itemStoneWand, 0, new ModelResourceLocation(BetterBuildersWandsMod.MODID + ":wandstone", "inventory"));
        ModelLoader.setCustomModelResourceLocation(BetterBuildersWandsMod.itemIronWand, 0, new ModelResourceLocation(BetterBuildersWandsMod.MODID + ":wandiron", "inventory"));
        ModelLoader.setCustomModelResourceLocation(BetterBuildersWandsMod.itemDiamondWand, 0, new ModelResourceLocation(BetterBuildersWandsMod.MODID + ":wanddiamond", "inventory"));
        ModelLoader.setCustomMeshDefinition(BetterBuildersWandsMod.itemUnbreakableWand, new ItemMeshDefinition(){

            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack) {
                return new ModelResourceLocation(BetterBuildersWandsMod.MODID + ":wandunbreakable", "inventory");
            }
        });
    }

    @Override
    public String Translate(String key) {
        return I18n.format(key);
    }

    @Override
    public String Translate(String formatString, Object... values) {
        return I18n.format(formatString, values);
    }
}
