package portablejim.bbw.core.items;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import portablejim.bbw.core.wands.IWand;
import portablejim.bbw.basics.EnumLock;
import portablejim.bbw.core.wands.RestrictedWand;

/**
 * Item for wand that takes damage.
 */
public class ItemRestrictedWandBasic extends ItemBasicWand {
    public ItemRestrictedWandBasic(RestrictedWand wand) {
        super();
        setMaxDamage(ToolMaterial.STONE.getMaxUses());
        this.setUnlocalizedName("betterbuilderswands:wandBasic");
        //setTextureName("betterbuilderswands:wandStone");
        this.wand = wand;
    }

    @Override
    public void nextMode(ItemStack itemStack, EntityPlayer player) {
        setMode(itemStack, EnumLock.HORIZONTAL);
    }

    public EnumLock getFaceLock(ItemStack itemStack) {
        return EnumLock.HORIZONTAL;
    }

    public EnumLock getDefaultMode() {
        return EnumLock.HORIZONTAL;
    }
}
