package portablejim.bbw.core;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;
import portablejim.bbw.IWand;
import portablejim.bbw.basics.EnumLock;
import portablejim.bbw.core.items.IWandItem;

/**
 * Common functions across wands.
 */
public class WandUtility {
    public static void setMode(ItemStack item, EnumLock mode) {
        NBTTagCompound tagCompound = new NBTTagCompound();
        if(item.hasTagCompound()) {
            tagCompound = item.getTagCompound();
        }
        NBTTagCompound bbwCompond = new NBTTagCompound();
        if(tagCompound.hasKey("bbw", Constants.NBT.TAG_COMPOUND)) {
            bbwCompond = tagCompound.getCompoundTag("bbw");
        }
        short shortMask = (short) (mode.mask & 7);
        bbwCompond.setShort("mask", shortMask);
        tagCompound.setTag("bbw", bbwCompond);
        item.setTagCompound(tagCompound);
    }

    public static EnumLock getMode(ItemStack item) {
        if(item != null && item.getItem() != null && item.getItem() instanceof IWandItem) {
            NBTTagCompound itemBaseNBT = item.getTagCompound();
            if(itemBaseNBT != null && itemBaseNBT.hasKey("bbw", Constants.NBT.TAG_COMPOUND)) {
                NBTTagCompound itemNBT = itemBaseNBT.getCompoundTag("bbw");
                int mask = itemNBT.hasKey("mask", Constants.NBT.TAG_SHORT) ? itemNBT.getShort("mask") : EnumLock.NOLOCK.mask;
                return EnumLock.fromMask(mask);
            }
        }
        return EnumLock.NOLOCK;
    }
}
