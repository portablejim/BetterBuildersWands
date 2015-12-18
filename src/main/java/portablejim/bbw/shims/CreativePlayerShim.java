package portablejim.bbw.shims;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import portablejim.bbw.basics.Point3d;

/**
 * Wrap functions for a creative player.
 */
public class CreativePlayerShim extends BasicPlayerShim implements IPlayerShim {

    public CreativePlayerShim(EntityPlayer player) {
        super(player);
    }

    @Override
    public int countItems(ItemStack itemStack) {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean useItem(ItemStack itemStack) {
        return true;
    }

    @Override
    public ItemStack getNextItem(Block block, int meta) {
        return new ItemStack(block, 1, meta);
    }
}
