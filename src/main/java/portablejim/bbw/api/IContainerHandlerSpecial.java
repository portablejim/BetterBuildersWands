package portablejim.bbw.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Handler for containers with shared inventories.
 */
public interface IContainerHandlerSpecial {
    Object initCount(EntityPlayer player);
    Object initUse(EntityPlayer player);
    boolean matches(Object object, ItemStack itemStack, ItemStack inventoryStack);
    void handleInventorySlot(Object object, ItemStack itemStack, ItemStack inventoryStack);
    int finalCount(Object object);
    int finalUse(Object object, int toUse);
}
