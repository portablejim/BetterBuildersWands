package portablejim.bbw.core;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import portablejim.bbw.core.wands.IWand;
import portablejim.bbw.basics.EnumLock;
import portablejim.bbw.basics.Point3d;
import portablejim.bbw.shims.IPlayerShim;
import portablejim.bbw.shims.IWorldShim;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Does the heavy work of working out the blocks to place and places them.
 */
public class WandWorker {
    private final IWand wand;
    private final IPlayerShim player;
    private final IWorldShim world;

    HashSet<Point3d> allCandidates = new HashSet<Point3d>();

    public WandWorker(IWand wand, IPlayerShim player, IWorldShim world) {

        this.wand = wand;
        this.player = player;
        this.world = world;
    }

    public ItemStack getEquivalentItemStack(Point3d blockPos) {
        Block block = world.getBlock(blockPos);
        return block == null ? null : new ItemStack(Item.getItemFromBlock(block), 1, world.getMetadata(blockPos));
    }

    private boolean shouldContinue(Point3d currentCandidate, Block targetBlock, EnumFacing facing, Block candidateSupportingBlock, int candidateSupportingMeta, AxisAlignedBB blockBB) {
        if(!world.blockIsAir(currentCandidate)) return false;
        /*if((FluidRegistry.getFluid("water").getBlock().equals(world.getBlock(currentCandidate)) || FluidRegistry.getFluid("lava").getBlock().equals(world.getBlock(currentCandidate)))
                && world.getMetadata(currentCandidate) == 0){
            return false;
        }*/
        if(!targetBlock.equals(candidateSupportingBlock)) return false;
        //if(targetBlock instanceof BlockCrops) return false;
        if(!targetBlock.canPlaceBlockAt(world.getWorld(), new BlockPos(currentCandidate.x, currentCandidate.y, currentCandidate.z))) return false;
        if(!targetBlock.canReplace(world.getWorld(), new BlockPos(currentCandidate.x, currentCandidate.y, currentCandidate.z), facing, new ItemStack(candidateSupportingBlock, 1, candidateSupportingMeta))) return false;

        return !world.entitiesInBox(blockBB);

    }

    public LinkedList<Point3d> getBlockPositionList(Point3d blockLookedAt, EnumFacing placeDirection, int maxBlocks, EnumLock directionLock, EnumLock faceLock) {
        LinkedList<Point3d> candidates = new LinkedList<Point3d>();
        LinkedList<Point3d> toPlace = new LinkedList<Point3d>();

        Block targetBlock = world.getBlock(blockLookedAt);
        int targetMetadata = world.getMetadata(blockLookedAt);
        Point3d startingPoint = blockLookedAt.move(placeDirection);

        int directionMaskInt = directionLock.mask;
        int faceMaskInt = faceLock.mask;

        if (((directionLock != EnumLock.HORIZONTAL && directionLock != EnumLock.VERTICAL) || (placeDirection != EnumFacing.UP && placeDirection != EnumFacing.DOWN))
                && (directionLock != EnumLock.NORTHSOUTH || (placeDirection != EnumFacing.NORTH && placeDirection != EnumFacing.SOUTH))
                && (directionLock != EnumLock.EASTWEST || (placeDirection != EnumFacing.EAST && placeDirection != EnumFacing.WEST))) {
            candidates.add(startingPoint);
        }
        while(candidates.size() > 0 && toPlace.size() < maxBlocks) {
            Point3d currentCandidate = candidates.removeFirst();

            Point3d supportingPoint = currentCandidate.move(placeDirection.getOpposite());
            Block candidateSupportingBlock = world.getBlock(supportingPoint);
            int candidateSupportingMeta = world.getMetadata(supportingPoint);
            AxisAlignedBB blockBB =targetBlock.getCollisionBoundingBox(world.getWorld(), new BlockPos(currentCandidate.x, currentCandidate.y, currentCandidate.z), targetBlock.getDefaultState());
            if(shouldContinue(currentCandidate, targetBlock, placeDirection, candidateSupportingBlock, candidateSupportingMeta, blockBB)
                    && allCandidates.add(currentCandidate)) {
                toPlace.add(currentCandidate);

                switch (placeDirection) {
                    case DOWN:
                    case UP:
                        if((faceMaskInt & EnumLock.UP_DOWN_MASK) > 0) {
                            if ((directionMaskInt & EnumLock.NORTH_SOUTH_MASK) > 0)
                                candidates.add(currentCandidate.move(EnumFacing.NORTH));
                            if ((directionMaskInt & EnumLock.EAST_WEST_MASK) > 0)
                                candidates.add(currentCandidate.move(EnumFacing.EAST));
                            if ((directionMaskInt & EnumLock.NORTH_SOUTH_MASK) > 0)
                                candidates.add(currentCandidate.move(EnumFacing.SOUTH));
                            if ((directionMaskInt & EnumLock.EAST_WEST_MASK) > 0)
                                candidates.add(currentCandidate.move(EnumFacing.WEST));
                            if ((directionMaskInt & EnumLock.NORTH_SOUTH_MASK) > 0 && (directionMaskInt & EnumLock.EAST_WEST_MASK) > 0) {
                                candidates.add(currentCandidate.move(EnumFacing.NORTH).move(EnumFacing.EAST));
                                candidates.add(currentCandidate.move(EnumFacing.NORTH).move(EnumFacing.WEST));
                                candidates.add(currentCandidate.move(EnumFacing.SOUTH).move(EnumFacing.EAST));
                                candidates.add(currentCandidate.move(EnumFacing.SOUTH).move(EnumFacing.WEST));
                            }
                        }
                        break;
                    case NORTH:
                    case SOUTH:
                        if((faceMaskInt & EnumLock.NORTH_SOUTH_MASK) > 0) {
                            if ((directionMaskInt & EnumLock.UP_DOWN_MASK) > 0)
                                candidates.add(currentCandidate.move(EnumFacing.UP));
                            if ((directionMaskInt & EnumLock.EAST_WEST_MASK) > 0)
                                candidates.add(currentCandidate.move(EnumFacing.EAST));
                            if ((directionMaskInt & EnumLock.UP_DOWN_MASK) > 0)
                                candidates.add(currentCandidate.move(EnumFacing.DOWN));
                            if ((directionMaskInt & EnumLock.EAST_WEST_MASK) > 0)
                                candidates.add(currentCandidate.move(EnumFacing.WEST));
                            if ((directionMaskInt & EnumLock.UP_DOWN_MASK) > 0 && (directionMaskInt & EnumLock.EAST_WEST_MASK) > 0) {
                                candidates.add(currentCandidate.move(EnumFacing.UP).move(EnumFacing.EAST));
                                candidates.add(currentCandidate.move(EnumFacing.UP).move(EnumFacing.WEST));
                                candidates.add(currentCandidate.move(EnumFacing.DOWN).move(EnumFacing.EAST));
                                candidates.add(currentCandidate.move(EnumFacing.DOWN).move(EnumFacing.WEST));
                            }
                        }
                        break;
                    case WEST:
                    case EAST:
                        if((faceMaskInt & EnumLock.EAST_WEST_MASK) > 0) {
                            if ((directionMaskInt & EnumLock.UP_DOWN_MASK) > 0)
                                candidates.add(currentCandidate.move(EnumFacing.UP));
                            if ((directionMaskInt & EnumLock.NORTH_SOUTH_MASK) > 0)
                                candidates.add(currentCandidate.move(EnumFacing.NORTH));
                            if ((directionMaskInt & EnumLock.UP_DOWN_MASK) > 0)
                                candidates.add(currentCandidate.move(EnumFacing.DOWN));
                            if ((directionMaskInt & EnumLock.NORTH_SOUTH_MASK) > 0)
                                candidates.add(currentCandidate.move(EnumFacing.SOUTH));
                            if ((directionMaskInt & EnumLock.UP_DOWN_MASK) > 0 && (directionMaskInt & EnumLock.NORTH_SOUTH_MASK) > 0) {
                                candidates.add(currentCandidate.move(EnumFacing.UP).move(EnumFacing.NORTH));
                                candidates.add(currentCandidate.move(EnumFacing.UP).move(EnumFacing.SOUTH));
                                candidates.add(currentCandidate.move(EnumFacing.DOWN).move(EnumFacing.NORTH));
                                candidates.add(currentCandidate.move(EnumFacing.DOWN).move(EnumFacing.SOUTH));
                            }
                        }
                }
            }
        }
        return toPlace;
    }

    public ArrayList<Point3d> placeBlocks(ItemStack itemStack, LinkedList<Point3d> blockPosList, Point3d originalBlock) {
        ArrayList<Point3d> placedBlocks = new ArrayList<Point3d>();
        for(Point3d blockPos : blockPosList) {
            /*if(player.countItems(getEquivalentItemStack(originalBlock)) < 1) {
                break;
            }*/

            boolean blockPlaceSuccess = world.copyBlock(originalBlock, blockPos);

            if(blockPlaceSuccess) {
                world.playPlaceAtBlock(blockPos, world.getBlock(originalBlock));
                placedBlocks.add(blockPos);
                if (!player.isCreative()) {
                    wand.placeBlock(itemStack, player.getPlayer());
                }
                boolean takeFromInventory = player.useItem(getEquivalentItemStack(originalBlock));
                if(!takeFromInventory) {
                    FMLLog.info("BBW takeback: %s", blockPos.toString());
                    world.setBlockToAir(blockPos);
                    placedBlocks.remove(placedBlocks.size() - 1);
                }
            }
        }

        return placedBlocks;
    }
}
