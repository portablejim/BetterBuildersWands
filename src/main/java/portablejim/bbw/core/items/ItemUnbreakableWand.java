package portablejim.bbw.core.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import portablejim.bbw.IWand;
import portablejim.bbw.basics.EnumLock;
import portablejim.bbw.core.WandUtility;
import portablejim.bbw.core.WandWorker;
import portablejim.bbw.core.wands.UnbreakingWand;

/**
 * The actual item: Simple wand with no durability. Similar to current Builder's Wand.
 */
public class ItemUnbreakableWand extends ItemBasicWand{
    public ItemUnbreakableWand() {
        super();
        this.setUnlocalizedName("betterbuilderswands:unbreakableWand");
        this.setTextureName("betterbuilderswands:wandUnbreakable");
    }

    public EnumLock getLock(ItemStack itemStack) {
        IWand wand = new UnbreakingWand(itemStack);
        return  wand.getMode();
    }

    public EnumLock getFaceLock(ItemStack itemStack) {
        if(WandUtility.getMode(itemStack) == EnumLock.HORIZONTAL) {
            return EnumLock.HORIZONTAL;
        }
        return EnumLock.NOLOCK;
    }

    @Override
    public void nextMode(ItemStack itemStack, EntityPlayer player) {
        IWand wand = new UnbreakingWand(itemStack);
        switch(wand.getMode()) {

            case NORTHSOUTH:
                WandUtility.setMode(itemStack, EnumLock.EASTWEST);
                break;
            case VERTICAL:
                WandUtility.setMode(itemStack, EnumLock.NORTHSOUTH);
                break;
            case VERTICALEASTWEST:
                WandUtility.setMode(itemStack, EnumLock.NOLOCK);
                break;
            case EASTWEST:
                WandUtility.setMode(itemStack, EnumLock.VERTICALNORTHSOUTH);
                break;
            case HORIZONTAL:
                WandUtility.setMode(itemStack, EnumLock.VERTICAL);
                break;
            case VERTICALNORTHSOUTH:
                WandUtility.setMode(itemStack, EnumLock.VERTICALEASTWEST);
                break;
            case NOLOCK:
                WandUtility.setMode(itemStack, EnumLock.HORIZONTAL);
                break;
        }
    }

    @Override
    public IWand getWand(ItemStack itemStack) {
        return new UnbreakingWand(itemStack);
    }
}
