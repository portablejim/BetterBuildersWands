package portablejim.bbw.shims;

import net.minecraft.item.ItemStack;
import portablejim.bbw.basics.Point3d;

/**
 * Wrap player functions.
 */
public interface IPlayerShim {
    int countItems(ItemStack itemStack);
    boolean useItem(ItemStack itemStack);
    Point3d getPlayerPosition();
}
