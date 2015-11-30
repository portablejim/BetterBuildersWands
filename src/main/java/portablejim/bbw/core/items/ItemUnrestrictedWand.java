package portablejim.bbw.core.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import portablejim.bbw.core.wands.IWand;
import portablejim.bbw.basics.EnumLock;
import portablejim.bbw.core.wands.UnbreakingWand;

/**
 * The actual item: Simple wand with no durability. Similar to current Builder's Wand.
 */
public class ItemUnrestrictedWand extends ItemBasicWand{
    public ItemUnrestrictedWand(IWand wand, String name, String texture) {
        super();
        this.setUnlocalizedName("betterbuilderswands:wand" + name);
        this.setTextureName("betterbuilderswands:wand" + texture);
        this.wand = wand;
    }

    public EnumLock getFaceLock(ItemStack itemStack) {
        if(getMode(itemStack) == EnumLock.HORIZONTAL) {
            return EnumLock.HORIZONTAL;
        }
        return EnumLock.NOLOCK;
    }

    @Override
    public void nextMode(ItemStack itemStack, EntityPlayer player) {
        switch(getMode(itemStack)) {

            case NORTHSOUTH:
                setMode(itemStack, EnumLock.EASTWEST);
                break;
            case VERTICAL:
                setMode(itemStack, EnumLock.NORTHSOUTH);
                break;
            case VERTICALEASTWEST:
                setMode(itemStack, EnumLock.NOLOCK);
                break;
            case EASTWEST:
                setMode(itemStack, EnumLock.VERTICALNORTHSOUTH);
                break;
            case HORIZONTAL:
                setMode(itemStack, EnumLock.VERTICAL);
                break;
            case VERTICALNORTHSOUTH:
                setMode(itemStack, EnumLock.VERTICALEASTWEST);
                break;
            case NOLOCK:
                setMode(itemStack, EnumLock.HORIZONTAL);
                break;
        }
    }
}
