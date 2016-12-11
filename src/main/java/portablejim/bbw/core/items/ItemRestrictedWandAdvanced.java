package portablejim.bbw.core.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import portablejim.bbw.basics.EnumFluidLock;
import portablejim.bbw.basics.EnumLock;
import portablejim.bbw.core.wands.RestrictedWand;

/**
 * Item for wand that takes damage.
 */
public class ItemRestrictedWandAdvanced extends ItemBasicWand {
    public ItemRestrictedWandAdvanced(RestrictedWand wand) {
        super();
        setMaxDamage(ToolMaterial.IRON.getMaxUses());
        this.setUnlocalizedName("betterbuilderswands.wandadvanced");
        //setTextureName("betterbuilderswands:wandIron");
        this.wand = wand;
    }

    @Override
    public void nextMode(ItemStack itemStack, EntityPlayer player) {

        switch(getMode(itemStack)) {
            case VERTICAL:
                setMode(itemStack, EnumLock.HORIZONTAL);
                break;
            case HORIZONTAL:
                setMode(itemStack, EnumLock.VERTICAL);
                break;
            default:
                setMode(itemStack, EnumLock.HORIZONTAL);
                break;
        }
    }

    @Override
    public void nextFluidMode(ItemStack itemStack, EntityPlayer player) {
        setFluidMode(itemStack, EnumFluidLock.STOPAT);
    }

    public EnumLock getFaceLock(ItemStack itemStack) {
        return EnumLock.NOLOCK;
    }

    @Override
    public EnumLock getDefaultMode() {
        return EnumLock.HORIZONTAL;
    }
}
