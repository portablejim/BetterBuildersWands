package portablejim.bbw.core.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import portablejim.bbw.basics.EnumFluidLock;
import portablejim.bbw.core.wands.IWand;
import portablejim.bbw.basics.EnumLock;

/**
 * Identifies classes that should be treated as a wand.
 */
public interface IWandItem {
    EnumLock getMode(ItemStack itemStack);
    void nextMode(ItemStack itemStack, EntityPlayer player);
    EnumFluidLock getFluidMode(ItemStack itemStack);
    void nextFluidMode(ItemStack itemStack, EntityPlayer player);
    IWand getWand();
    EnumLock getFaceLock(ItemStack itemStack);
}
