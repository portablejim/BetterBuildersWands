package portablejim.bbw.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Created by james on 28/12/16.
 */
public interface IContainerHandler {
    boolean matches(EntityPlayer player, ItemStack itemStack, ItemStack inventoryStack);
    int countItems(EntityPlayer player, ItemStack itemStack, ItemStack inventoryStack);
    int useItems(EntityPlayer player, ItemStack itemStack, ItemStack inventoryStack, int count);
}
