package portablejim.bbw.core.items;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import portablejim.bbw.IWand;
import portablejim.bbw.basics.EnumLock;

/**
 * Item for wand that takes damage.
 */
public class ItemRestrictedWandBasic extends ItemBasicWand {
    public ItemRestrictedWandBasic(int damage) {
        super();
        setMaxDamage(damage);
    }

    @Override
    public void nextMode(ItemStack itemStack, EntityPlayer player) {

    }

    @Override
    public IWand getWand(ItemStack itemStack) {
        return null;
    }

    @Override
    public EnumLock getLock(ItemStack itemStack) {
        return null;
    }

    @Override
    public EnumLock getFaceLock(ItemStack itemStack) {
        return null;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack itemStack, World world, Block block, int x, int y, int z, EntityLivingBase entityLivingBase) {
        itemStack.damageItem(2, entityLivingBase);
        return true;
    }

    public boolean hitEntity(ItemStack p_77644_1_, EntityLivingBase p_77644_2_, EntityLivingBase p_77644_3_)
    {
        p_77644_1_.damageItem(2, p_77644_3_);
        return true;
    }
}
