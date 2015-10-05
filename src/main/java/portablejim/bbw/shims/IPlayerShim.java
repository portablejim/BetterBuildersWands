package portablejim.bbw.shims;

import net.minecraft.item.ItemStack;

/**
 * Wrap player functions.
 */
public interface IPlayerShim {
    int countItems(ItemStack itemStack);
    boolean useItem(ItemStack itemStack);
}
