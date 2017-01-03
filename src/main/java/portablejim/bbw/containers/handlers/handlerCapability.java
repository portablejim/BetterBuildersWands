package portablejim.bbw.containers.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import portablejim.bbw.api.IContainerHandler;

/**
 * Created by james on 28/12/16.
 */
public class handlerCapability implements IContainerHandler {
    @Override
    public boolean matches(EntityPlayer player, ItemStack itemStack, ItemStack inventoryStack) {
        return inventoryStack != null && inventoryStack.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
    }

    @Override
    public int countItems(EntityPlayer player, ItemStack itemStack, ItemStack inventoryStack) {
        if(!inventoryStack.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
            return 0;
        }

        int total = 0;

        IItemHandler itemHandler = inventoryStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            ItemStack containerStack = itemHandler.getStackInSlot(i);
            if (containerStack != null && itemStack.isItemEqual(containerStack)) {
                total += Math.max(0, containerStack.stackSize);
            }

            // Already in a container. Don't inception this thing.
        }
        return total;
    }

    @Override
    public int useItems(EntityPlayer player, ItemStack itemStack, ItemStack inventoryStack, int count) {
        int toUse = itemStack.stackSize;
        if(!inventoryStack.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
            return 0;
        }

        IItemHandler itemHandler = inventoryStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            ItemStack handlerStack = itemHandler.getStackInSlot(i);
            if(handlerStack != null && handlerStack.isItemEqual(itemStack)) {
                ItemStack extracted = itemHandler.extractItem(i, count, false);
                if(extracted != null) {
                    count -= extracted.stackSize;
                }
                if(count <= 0) {
                    break;
                }
            }
        }
        return count;
    }
}
