package portablejim.bbw.shims;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import portablejim.bbw.BetterBuildersWandsMod;
import portablejim.bbw.api.IContainerHandlerSpecial;
import portablejim.bbw.basics.Point3d;
import portablejim.bbw.containers.ContainerManager;
import portablejim.bbw.core.items.IWandItem;
import vazkii.botania.api.item.IBlockProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Wrap a player to provide basic functions.
 */
public class BasicPlayerShim implements IPlayerShim {
    private EntityPlayer player;
    private boolean providersEnabled;

    protected float assumedReachDistance;

    public BasicPlayerShim(EntityPlayer player) {
        this.player = player;
        this.providersEnabled = areProvidersEnabled();
        this.assumedReachDistance = 4.5F;
    }

    public static Block getBlock(ItemStack stack) {
        return Block.getBlockFromItem(stack.getItem());
    }

    public static int getBlockMeta(ItemStack stack) {
        return stack.getHasSubtypes() ? stack.getItemDamage() : 0;
    }

    private static boolean areProvidersEnabled() {
        try {
            boolean disable = new Object() instanceof IBlockProvider;
            return true;
        } catch (NoClassDefFoundError e) {
            return false;
        }
    }

    @Override
    public int countItems(ItemStack itemStack) {
        int total = 0;
        if(itemStack == null || player.inventory == null || player.inventory.mainInventory == null) {
            return 0;
        }

        ContainerManager containerManager = BetterBuildersWandsMod.instance.containerManager;
        Map<IContainerHandlerSpecial, Object> containerState = containerManager.initCount(player);

        Block block = getBlock(itemStack);
        int meta = getBlockMeta(itemStack);

        for(ItemStack inventoryStack : player.inventory.mainInventory) {
            if(inventoryStack != null && itemStack.isItemEqual(inventoryStack)) {
                total += Math.max(0, inventoryStack.getCount());
            }
            else {
                int amount = containerManager.countItems(containerState, player, itemStack, inventoryStack);
                if(amount == Integer.MAX_VALUE) {
                    return Integer.MAX_VALUE;
                }
                total += amount;
            }
        }

        total += containerManager.finalCount(containerState);
        return itemStack.getCount() > 0 ? total / itemStack.getCount() : 0;
    }

    @Override
    public boolean useItem(ItemStack itemStack) {
        if(itemStack == null || player.inventory == null || player.inventory.mainInventory == null) {
            return false;
        }

        ContainerManager containerManager = BetterBuildersWandsMod.instance.containerManager;
        Map<IContainerHandlerSpecial, Object> containerState = containerManager.initUse(player);

        // Reverse direction to leave hotbar to last.
        int toUse = itemStack.getCount();
        List<ItemStack> providers = new ArrayList<ItemStack>();
        for(int i = player.inventory.mainInventory.size()- 1; i >= 0; i--) {
            ItemStack inventoryStack = player.inventory.mainInventory.get(i);
            if(inventoryStack != ItemStack.EMPTY && itemStack.isItemEqual(inventoryStack)) {
                if(inventoryStack.getCount() < toUse) {
                    inventoryStack.setCount(0);
                    toUse -= inventoryStack.getCount();
                }
                else {
                    inventoryStack.setCount(inventoryStack.getCount() - toUse);
                    toUse = 0;
                }
                if(inventoryStack.getCount() == 0) {
                    player.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
                }
                player.inventoryContainer.detectAndSendChanges();
            }
            else {
                toUse = containerManager.useItems(containerState, player, itemStack, inventoryStack, toUse);
            }
            toUse = containerManager.finalUse(containerState, toUse);
            if(toUse <= 0) {
                return true;
            }
        }

        return false;
    }

    @Override
    public ItemStack getNextItem(Block block, int meta) {
        for(int i = player.inventory.mainInventory.size() - 1; i >= 0; i--) {
            ItemStack inventoryStack = player.inventory.mainInventory.get(i);

        }

        return null;
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
    public ItemStack getHeldWandIfAny() {
        return getHeldWandIfAny(player);
    }

    public static ItemStack getHeldWandIfAny(EntityPlayer player) {
        ItemStack wandItem = ItemStack.EMPTY;
        if(player.getHeldItem(EnumHand.MAIN_HAND) != ItemStack.EMPTY && player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof IWandItem) {
            wandItem = player.getHeldItem(EnumHand.MAIN_HAND);
        }
        else if(player.getHeldItem(EnumHand.OFF_HAND) != ItemStack.EMPTY && player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof IWandItem) {
            wandItem = player.getHeldItem(EnumHand.OFF_HAND);
        }
        return wandItem;
    }

    @Override
    public boolean isCreative() {
        return player.capabilities.isCreativeMode;
    }

    @Override
    public double getReachDistance() {
        if(player instanceof EntityPlayerMP) {
            return ((EntityPlayerMP)player).interactionManager.getBlockReachDistance();
        }
        else {
            return this.assumedReachDistance;
        }
    }
}
