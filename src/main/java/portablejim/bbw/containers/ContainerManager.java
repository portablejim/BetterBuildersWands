package portablejim.bbw.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import portablejim.bbw.api.IContainerHandler;
import portablejim.bbw.api.IContainerHandlerSpecial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Manage container handlers and wrap their usage.
 */
public class ContainerManager {
    private ArrayList<IContainerHandler> handlers;
    private ArrayList<IContainerHandlerSpecial> handlersSpecial;

    public ContainerManager() {
        handlers = new ArrayList<IContainerHandler>();
        handlersSpecial = new ArrayList<IContainerHandlerSpecial>();
    }


    public boolean register(IContainerHandler handler) {
        return handlers.add(handler);
    }

    public boolean registerSpecial(IContainerHandlerSpecial handler) {
        return handlersSpecial.add(handler);
    }

    public Map<IContainerHandlerSpecial, Object> initCount(EntityPlayer player) {
        HashMap<IContainerHandlerSpecial, Object> state = new HashMap<IContainerHandlerSpecial, Object>();
        for(IContainerHandlerSpecial handlerSpecial : handlersSpecial) {
            state.put(handlerSpecial, handlerSpecial.initCount(player));
        }
        return state;
    }

    public Map<IContainerHandlerSpecial, Object> initUse(EntityPlayer player) {
        HashMap<IContainerHandlerSpecial, Object> state = new HashMap<IContainerHandlerSpecial, Object>();
        for(IContainerHandlerSpecial handlerSpecial : handlersSpecial) {
            state.put(handlerSpecial, handlerSpecial.initUse(player));
        }
        return state;
    }

    public int countItems(Map<IContainerHandlerSpecial, Object> state, EntityPlayer player, ItemStack itemStack, ItemStack inventoryStack) {
        for(IContainerHandlerSpecial handlerSpecial : handlersSpecial) {
            handlerSpecial.handleInventorySlot(state.get(handlerSpecial), itemStack, inventoryStack);
        }
        for(IContainerHandler handler : handlers) {
            if(handler.matches(player, itemStack, inventoryStack)) {
                return handler.countItems(player,itemStack, inventoryStack);
            }
        }
        return 0;
    }

    public int useItems(Map<IContainerHandlerSpecial, Object> state, EntityPlayer player, ItemStack itemStack, ItemStack inventoryStack, int count) {
        for(IContainerHandlerSpecial handlerSpecial : handlersSpecial) {
            handlerSpecial.handleInventorySlot(state.get(handlerSpecial), itemStack, inventoryStack);
        }
        for(IContainerHandler handler : handlers) {
            if(handler.matches(player, itemStack, inventoryStack)) {
                return handler.useItems(player, itemStack, inventoryStack, count);
            }
        }
        return count;
    }

    public int finalCount(Map<IContainerHandlerSpecial, Object> state) {
        int count = 0;
        for(IContainerHandlerSpecial handlerSpecial : handlersSpecial) {
            count += handlerSpecial.finalCount(state.get(handlerSpecial));
        }
        return count;
    }

    public int finalUse(Map<IContainerHandlerSpecial, Object> state, int toUse) {
        for(IContainerHandlerSpecial handlerSpecial : handlersSpecial) {
            toUse = handlerSpecial.finalUse(state.get(handlerSpecial), toUse);
        }
        return toUse;
    }
}
