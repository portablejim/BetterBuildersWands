package portablejim.bbw.shims;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import portablejim.bbw.basics.Point3d;

/**
 * Wrap a player to provide basic functions.
 */
public class BasicPlayerShim implements IPlayerShim {
    private EntityPlayer player;

    public BasicPlayerShim(EntityPlayer player) {
        this.player = player;
    }

    @Override
    public int countItems(ItemStack itemStack) {
        int total = 0;
        if(itemStack == null || player.inventory == null || player.inventory.mainInventory == null) {
            return 0;
        }


        for(ItemStack inventoryStack : player.inventory.mainInventory) {
            if(inventoryStack != null && itemStack.isItemEqual(inventoryStack)) {
                total += Math.max(0, inventoryStack.stackSize);
            }
        }

        return total;
    }

    @Override
    public boolean useItem(ItemStack itemStack) {
        if(itemStack == null || player.inventory == null || player.inventory.mainInventory == null) {
            return false;
        }

        // Reverse direction to leave hotbar to last.
        for(int i = player.inventory.mainInventory.length - 1; i >= 0; i--) {
            ItemStack inventoryStack = player.inventory.mainInventory[i];
            if(inventoryStack != null && itemStack.isItemEqual(inventoryStack)) {
                inventoryStack.stackSize -= 1;
                player.inventory.setInventorySlotContents(i, inventoryStack);
                if(inventoryStack.stackSize == 0) {
                    player.inventory.setInventorySlotContents(i, null);
                }
                player.inventoryContainer.detectAndSendChanges();
                return true;
            }
        }
        return false;
    }

    @Override
    public Point3d getPlayerPosition() {
        return new Point3d((int)player.posX, (int)player.posY, (int)player.posZ);
    }

    @Override
    public EntityPlayer getPlayer() {
        return player;
    }

    @Override
    public boolean isCreative() {
        return player.capabilities.isCreativeMode;
    }
}
