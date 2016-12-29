package portablejim.bbw.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import portablejim.bbw.api.IContainerHandler;

import java.util.ArrayList;

/**
 * Created by james on 28/12/16.
 */
public class ContainerManager {
    private ArrayList<IContainerHandler> handlers;

    public ContainerManager() {
        handlers = new ArrayList<IContainerHandler>();
    }


    public boolean register(IContainerHandler handler) {
        return handlers.add(handler);
    }

    public int countItems(EntityPlayer player, ItemStack itemStack, ItemStack inventoryStack) {
        for(IContainerHandler handler : handlers) {
            if(handler.matches(player, itemStack, inventoryStack)) {
                return handler.countItems(player,itemStack, inventoryStack);
            }
        }
        return 0;
    }

    public int useItems(EntityPlayer player, ItemStack itemStack, ItemStack inventoryStack, int count) {
        for(IContainerHandler handler : handlers) {
            if(handler.matches(player, itemStack, inventoryStack)) {
                return handler.useItems(player, itemStack, inventoryStack, count);
            }
        }
        return count;
    }
}
