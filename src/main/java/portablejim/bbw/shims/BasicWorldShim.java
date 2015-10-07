package portablejim.bbw.shims;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import portablejim.bbw.basics.Point3d;

import java.util.List;

/**
 * Wrap a world to provide basic functions.
 */
public class BasicWorldShim implements IWorldShim {
    private World world;

    public BasicWorldShim(World world) {

        this.world = world;
    }

    @Override
    public Block getBlock(Point3d point) {
        if(world != null) {
            return world.getBlock(point.x, point.y, point.z);
        }
        return null;
    }

    @Override
    public boolean blockIsAir(Point3d point) {
        Block block = world.getBlock(point.x, point.y, point.z);
        return block.isAir(world, point.x, point.y, point.z);
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public boolean copyBlock(Point3d originalBlock, Point3d blockPos) {
        Block block = world.getBlock(originalBlock.x, originalBlock.y, originalBlock.z);
        int meta = world.getBlockMetadata(originalBlock.x, originalBlock.y, originalBlock.z);
        return world.setBlock(blockPos.x, blockPos.y, blockPos.z, block, meta, 3);
    }

    @Override
    public void setBlockToAir(Point3d blockPos) {
        world.setBlock(blockPos.x, blockPos.y, blockPos.z, Blocks.air);
    }

    @Override
    public int getMetadata(Point3d point) {
        if(world != null) {
            return world.getBlockMetadata(point.x, point.y, point.z);
        }
        return 0;
    }

    @Override
    public boolean entitiesInBox(AxisAlignedBB box) {
        List entitiesWithinAABB = world.getEntitiesWithinAABB(EntityLivingBase.class, box);
        return entitiesWithinAABB.size() > 0;
    }

    @Override
    public void playPlaceAtBlock(Point3d position, Block blockType) {
        if(position != null && blockType != null) {
            world.playSoundEffect(position.x + 0.5D, position.y + 0.5D, position.z + 0.5D, blockType.stepSound.func_150496_b(), (blockType.stepSound.getVolume() + 1.0F) / 2.0F, blockType.stepSound.getPitch() * 0.8F);
        }
    }
}
