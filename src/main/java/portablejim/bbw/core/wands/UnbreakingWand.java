package portablejim.bbw.core.wands;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

/**
 * Simple wand that doesn't break / have durability.
 */
public class UnbreakingWand implements IWand {
    @Override
    public int getMaxBlocks(ItemStack itemStack) {
        return 4096;
    }

    @Override
    public boolean placeBlock(ItemStack itemStack, EntityLivingBase entityLivingBase) {
        return true;
    }
}
