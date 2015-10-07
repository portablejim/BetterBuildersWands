package portablejim.bbw.shims;

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
}
