package portablejim.bbw.containers.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import portablejim.bbw.api.IContainerHandler;
import portablejim.bbw.shims.BasicPlayerShim;
import vazkii.botania.api.item.IBlockProvider;

/**
 * Created by james on 28/12/16.
 */
public class HandlerBotania implements IContainerHandler {
    @Override
    public boolean matches(EntityPlayer player, ItemStack itemStack, ItemStack inventoryStack) {
        return inventoryStack != null && inventoryStack.stackSize == 1 && inventoryStack.getItem() instanceof IBlockProvider;
    }

    @Override
    public int countItems(EntityPlayer player, ItemStack itemStack, ItemStack inventoryStack) {
        IBlockProvider prov = (IBlockProvider) inventoryStack.getItem();
        int provCount = prov.getBlockCount(player, itemStack, inventoryStack, BasicPlayerShim.getBlock(itemStack), BasicPlayerShim.getBlockMeta(itemStack));
        if(provCount == -1)
            return Integer.MAX_VALUE;
        return provCount;
    }

    @Override
    public int useItems(EntityPlayer player, ItemStack itemStack, ItemStack inventoryStack, int count) {
        IBlockProvider prov = (IBlockProvider) inventoryStack.getItem();
        if(prov.provideBlock(player, itemStack, inventoryStack, BasicPlayerShim.getBlock(itemStack), BasicPlayerShim.getBlockMeta(itemStack), true))
            return 1;
        return 0;
    }
}
