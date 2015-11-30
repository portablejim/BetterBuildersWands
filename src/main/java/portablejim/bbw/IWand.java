package portablejim.bbw;

import net.minecraft.entity.EntityLivingBase;
import portablejim.bbw.basics.EnumLock;
import portablejim.bbw.basics.Point3d;

import java.util.ArrayList;

/**
 * Created by james on 3/10/15.
 */
public interface IWand {
    EnumLock getMode();

    int getMaxBlocks();

    boolean placeBlock(EntityLivingBase entityLivingBase);
}
