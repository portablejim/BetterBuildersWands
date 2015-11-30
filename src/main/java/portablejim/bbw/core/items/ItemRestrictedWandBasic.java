package portablejim.bbw.core.items;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import portablejim.bbw.IWand;
import portablejim.bbw.basics.EnumLock;
import portablejim.bbw.core.WandUtility;
import portablejim.bbw.core.wands.RestrictedWand;

/**
 * Item for wand that takes damage.
 */
public class ItemRestrictedWandBasic extends ItemBasicWand {
    public ItemRestrictedWandBasic(int damage) {
        super();
        setMaxDamage(ToolMaterial.STONE.getMaxUses());
        this.setUnlocalizedName("betterbuilderswands:basicWand");
        setTextureName("betterbuilderswands:wandStone");
    }

    @Override
    public void nextMode(ItemStack itemStack, EntityPlayer player) {
        IWand wand = new RestrictedWand(itemStack);
        switch(wand.getMode()) {

            case NORTHSOUTH:
                break;
            case VERTICAL:
                break;
            case VERTICALEASTWEST:
                break;
            case EASTWEST:
                break;
            case HORIZONTAL:
                WandUtility.setMode(itemStack, EnumLock.HORIZONTAL);
                break;
            case VERTICALNORTHSOUTH:
                break;
            case NOLOCK:
                WandUtility.setMode(itemStack, EnumLock.HORIZONTAL);
                break;
        }
    }

    @Override
    public IWand getWand(ItemStack itemStack) {
        return new RestrictedWand(itemStack);
    }

    public EnumLock getLock(ItemStack itemStack) {
        IWand wand = new RestrictedWand(itemStack);
        return  wand.getMode();
    }

    public EnumLock getFaceLock(ItemStack itemStack) {
        if(WandUtility.getMode(itemStack) == EnumLock.HORIZONTAL) {
            return EnumLock.HORIZONTAL;
        }
        return EnumLock.NOLOCK;
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
