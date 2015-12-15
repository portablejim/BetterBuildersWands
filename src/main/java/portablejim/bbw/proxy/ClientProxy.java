package portablejim.bbw.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import portablejim.bbw.BetterBuildersWandsMod;
import portablejim.bbw.core.BlockEvents;
import portablejim.bbw.core.client.KeyEvents;

/**
 * Created by james on 16/10/15.
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
        /*ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
        mesher.register(BetterBuildersWandsMod.itemStoneWand, 0, new ModelResourceLocation(BetterBuildersWandsMod.MODID + ":wandStone", "inventory"));
        mesher.register(BetterBuildersWandsMod.itemIronWand, 0, new ModelResourceLocation(BetterBuildersWandsMod.MODID + ":wandIron", "inventory"));
        mesher.register(BetterBuildersWandsMod.itemDiamondWand, 0, new ModelResourceLocation(BetterBuildersWandsMod.MODID + ":wandDiamond", "inventory"));
        mesher.register(BetterBuildersWandsMod.itemUnbreakableWand, new ItemMeshDefinition(){

            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack) {
                return new ModelResourceLocation(BetterBuildersWandsMod.MODID + ":wandUnbreakable", "inventory");
            }
        });*/

        ModelLoader.setCustomModelResourceLocation(BetterBuildersWandsMod.itemStoneWand, 0, new ModelResourceLocation(BetterBuildersWandsMod.MODID + ":wandStone", "inventory"));
        ModelLoader.setCustomModelResourceLocation(BetterBuildersWandsMod.itemIronWand, 0, new ModelResourceLocation(BetterBuildersWandsMod.MODID + ":wandIron", "inventory"));
        ModelLoader.setCustomModelResourceLocation(BetterBuildersWandsMod.itemDiamondWand, 0, new ModelResourceLocation(BetterBuildersWandsMod.MODID + ":wandDiamond", "inventory"));
        ModelLoader.setCustomMeshDefinition(BetterBuildersWandsMod.itemUnbreakableWand, new ItemMeshDefinition(){

            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack) {
                return new ModelResourceLocation(BetterBuildersWandsMod.MODID + ":wandUnbreakable", "inventory");
            }
        });
    }
}
