package portablejim.bbw.core.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import portablejim.bbw.IWand;
import portablejim.bbw.basics.EnumLock;

/**
 * Identifies classes that should be treated as a wand.
 */
public interface IWandItem {
    void nextMode(ItemStack itemStack, EntityPlayer player);
    IWand getWand(ItemStack itemStack);
    EnumLock getLock(ItemStack itemStack);
    EnumLock getFaceLock(ItemStack itemStack);
}
