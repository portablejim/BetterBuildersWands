package portablejim.bbw.core.wands;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import portablejim.bbw.IWand;
import portablejim.bbw.basics.EnumLock;
import portablejim.bbw.core.WandUtility;

/**
 * Simple wand that doesn't break / have durability.
 */
public class RestrictedWand implements IWand {
    private ItemStack wandItem;

    public RestrictedWand(ItemStack wandItem) {
        this.wandItem = wandItem;
    }
    @Override
    public EnumLock getMode() {
        return WandUtility.getMode(wandItem);
    }

    @Override
    public int getMaxBlocks() {
        return wandItem.getMaxDamage() - wandItem.getItemDamage();
    }

    @Override
    public boolean placeBlock(EntityLivingBase entityLivingBase) {
        wandItem.damageItem(1, entityLivingBase);
        return true;
    }
}
