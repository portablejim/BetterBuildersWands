package portablejim.bbw.core.wands;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import portablejim.bbw.basics.EnumLock;
import portablejim.bbw.basics.Point3d;

import java.util.ArrayList;

/**
 * Interface for IWand objects that abstract the wand working.
 */
public interface IWand {
    int getMaxBlocks(ItemStack itemStack);

    boolean placeBlock(ItemStack itemStack, EntityLivingBase entityLivingBase);
}
