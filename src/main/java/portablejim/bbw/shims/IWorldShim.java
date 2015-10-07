package portablejim.bbw.shims;

import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import portablejim.bbw.basics.Point3d;

/**
 * Wrap functions to do with the world.
 */
public interface IWorldShim {
    Block getBlock(Point3d point);
    boolean blockIsAir(Point3d point);

    World getWorld();

    boolean copyBlock(Point3d originalBlock, Point3d blockPos);

    void setBlockToAir(Point3d blockPos);

    int getMetadata(Point3d blockPos);

    boolean entitiesInBox(AxisAlignedBB box);
}
