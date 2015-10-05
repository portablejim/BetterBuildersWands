package portablejim.bbw.core.items;

import com.sun.jmx.remote.internal.ArrayQueue;
import cpw.mods.fml.common.FMLLog;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import portablejim.bbw.basics.Point3d;
import portablejim.bbw.core.WandWorker;
import portablejim.bbw.core.wands.UnbreakingWand;
import portablejim.bbw.shims.BasicPlayerShim;
import portablejim.bbw.shims.BasicWorldShim;
import portablejim.bbw.shims.CreativePlayerShim;
import portablejim.bbw.shims.IPlayerShim;
import portablejim.bbw.shims.IWorldShim;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

/**
 * The actual item: Simple wand with no durability. Similar to current Builder's Wand.
 */
public class ItemUnbreakableWand extends Item implements IWandItem{
    public ItemUnbreakableWand() {
        super();
        this.setUnlocalizedName("BetterBuildersWands:unbreakableWand");
        this.setTextureName("minecraft:stick");
        this.setCreativeTab(CreativeTabs.tabTools);
    }

    public boolean onItemUse(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        IPlayerShim playerShim = new BasicPlayerShim(player);
        if(player.capabilities.isCreativeMode) {
            playerShim = new CreativePlayerShim();
        }
        IWorldShim worldShim = new BasicWorldShim(world);
        UnbreakingWand unbreakingWand = new UnbreakingWand(itemstack);

        WandWorker worker = new WandWorker(unbreakingWand, playerShim, worldShim);

        Point3d clickedPos = new Point3d(x, y, z);

        ItemStack targetItemstack = worker.getEquivalentItemStack(clickedPos);
        int numBlocks = Math.min(unbreakingWand.getMaxBlocks(), playerShim.countItems(targetItemstack));
        FMLLog.info("Max blocks: %d (%d|%d", numBlocks, unbreakingWand.getMaxBlocks(), playerShim.countItems(targetItemstack));

        LinkedList<Point3d> blocks = worker.getBlockPositionList(clickedPos, ForgeDirection.getOrientation(side), numBlocks);

        worker.placeBlocks(blocks, clickedPos);

       return true;
    }
}
