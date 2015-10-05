package portablejim.bbw.core.wands;

import net.minecraft.item.ItemStack;
import portablejim.bbw.IWand;
import portablejim.bbw.basics.EnumLock;
import portablejim.bbw.core.WandUtility;

/**
 * Simple wand that doesn't break / have durability.
 */
public class UnbreakingWand implements IWand {
    private ItemStack wandItem;

    public UnbreakingWand(ItemStack wandItem) {

        this.wandItem = wandItem;
    }
    @Override
    public EnumLock getMode() {
        return WandUtility.getMode(wandItem);
    }

    @Override
    public int getMaxBlocks() {
        return 32;
    }

    @Override
    public boolean placeBlock() {
        return true;
    }
}
