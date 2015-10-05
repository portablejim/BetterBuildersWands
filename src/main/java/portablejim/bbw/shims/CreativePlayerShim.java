package portablejim.bbw.shims;

import net.minecraft.item.ItemStack;

/**
 * Wrap functions for a creative player.
 */
public class CreativePlayerShim implements IPlayerShim {
    @Override
    public int countItems(ItemStack itemStack) {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean useItem(ItemStack itemStack) {
        return true;
    }
}
