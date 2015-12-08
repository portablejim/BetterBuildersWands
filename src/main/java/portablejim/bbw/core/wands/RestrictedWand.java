package portablejim.bbw.core.wands;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

/**
 * Simple wand that doesn't break / have durability.
 */
public class RestrictedWand implements IWand {
    protected int blocklimit = 0;

    public RestrictedWand(int limit) {
        this.blocklimit = limit;
    }
    @Override
    public int getMaxBlocks(ItemStack itemStack) {
        return Math.min(itemStack.getMaxDamage() - itemStack.getItemDamage(), this.blocklimit);
    }

    @Override
    public boolean placeBlock(ItemStack itemStack, EntityLivingBase entityLivingBase) {
        itemStack.damageItem(1, entityLivingBase);
        if(itemStack.stackSize > 0 && itemStack.getItemDamage() == itemStack.getMaxDamage()) {
            itemStack.stackSize = 0;
        }
        return true;
    }
}
