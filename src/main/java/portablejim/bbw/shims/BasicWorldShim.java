package portablejim.bbw.shims;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
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
            return world.getBlockState(new BlockPos(point.x, point.y, point.z)).getBlock();
        }
        return null;
    }

    @Override
    public boolean blockIsAir(Point3d point) {
        return world.isAirBlock(new BlockPos(point.x, point.y, point.z));
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public boolean copyBlock(Point3d originalBlock, Point3d blockPos) {
        IBlockState blockState = world.getBlockState(new BlockPos(originalBlock.x, originalBlock.y, originalBlock.z));
        boolean retval =  world.setBlockState(new BlockPos(blockPos.x, blockPos.y, blockPos.z), blockState, 3);
        // setBlockState includes 'smart' rotations blocks have which messes with the wand.
        // Doing it a second time ensures the block is set.
        world.setBlockState(new BlockPos(blockPos.x, blockPos.y, blockPos.z), blockState, 3);
        return retval;
    }

    @Override
    public void setBlockToAir(Point3d blockPos) {
        world.setBlockToAir(new BlockPos(blockPos.x, blockPos.y, blockPos.z));
    }

    @Override
    public int getMetadata(Point3d point) {
        if(world != null) {
            IBlockState state = world.getBlockState(new BlockPos(point.x, point.y, point.z));
            return state.getBlock().getMetaFromState(state);
        }
        return 0;
    }

    @Override
    public boolean entitiesInBox(AxisAlignedBB box) {
        if(box == null) return false;

        List entitiesWithinAABB = world.getEntitiesWithinAABB(EntityLivingBase.class, box);
        return entitiesWithinAABB.size() > 0;
    }

    @Override
    public void playPlaceAtBlock(Point3d position, Block blockType) {
        if(position != null && blockType != null) {
            world.playSound(position.x + 0.5D, position.y + 0.5D, position.z + 0.5D, blockType.getStepSound().getPlaceSound(), SoundCategory.BLOCKS, (blockType.getStepSound().getVolume() + 1.0F) / 2.0F, blockType.getStepSound().getPitch() * 0.8F, false);
        }
    }

    @Override
    public boolean setBlock(Point3d position, Block block, int meta) {
        return world.setBlockState(position.toBlockPos(), block.getStateFromMeta(meta), 3);
    }
}
