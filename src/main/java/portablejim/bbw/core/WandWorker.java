package portablejim.bbw.core;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import portablejim.bbw.core.wands.IWand;
import portablejim.bbw.basics.EnumLock;
import portablejim.bbw.basics.Point3d;
import portablejim.bbw.shims.IPlayerShim;
import portablejim.bbw.shims.IWorldShim;

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

    private boolean shouldContinue(Point3d currentCandidate, Block targetBlock, int targetMetadata, Block candidateSupportingBlock, int candidateSupportingMeta, AxisAlignedBB blockBB) {
        if(!world.blockIsAir(currentCandidate)) return false;
        /*if((FluidRegistry.getFluid("water").getBlock().equals(world.getBlock(currentCandidate)) || FluidRegistry.getFluid("lava").getBlock().equals(world.getBlock(currentCandidate)))
                && world.getMetadata(currentCandidate) == 0){
            return false;
        }*/
        if(!targetBlock.equals(candidateSupportingBlock)) return false;
        if(targetMetadata != candidateSupportingMeta) return false;
        //if(targetBlock instanceof BlockCrops) return false;
        if(!targetBlock.canPlaceBlockAt(world.getWorld(), currentCandidate.x, currentCandidate.y, currentCandidate.z)) return false;
        if(!targetBlock.canBlockStay(world.getWorld(), currentCandidate.x, currentCandidate.y, currentCandidate.z)) return false;
        if(!targetBlock.canReplace(world.getWorld(), currentCandidate.x, currentCandidate.y, currentCandidate.z, targetMetadata, new ItemStack(candidateSupportingBlock, 1, candidateSupportingMeta))) return false;

        return !world.entitiesInBox(blockBB);

    }

    public LinkedList<Point3d> getBlockPositionList(Point3d blockLookedAt, ForgeDirection placeDirection, int maxBlocks, EnumLock directionLock, EnumLock faceLock) {
        LinkedList<Point3d> candidates = new LinkedList<Point3d>();
        LinkedList<Point3d> toPlace = new LinkedList<Point3d>();

        Block targetBlock = world.getBlock(blockLookedAt);
        int targetMetadata = world.getMetadata(blockLookedAt);
        Point3d startingPoint = blockLookedAt.move(placeDirection);

        int directionMaskInt = directionLock.mask;
        int faceMaskInt = faceLock.mask;

        if (((directionLock != EnumLock.HORIZONTAL && directionLock != EnumLock.VERTICAL) || (placeDirection != ForgeDirection.UP && placeDirection != ForgeDirection.DOWN))
                && (directionLock != EnumLock.NORTHSOUTH || (placeDirection != ForgeDirection.NORTH && placeDirection != ForgeDirection.SOUTH))
                && (directionLock != EnumLock.EASTWEST || (placeDirection != ForgeDirection.EAST && placeDirection != ForgeDirection.WEST))) {
            candidates.add(startingPoint);
        }
        while(candidates.size() > 0 && toPlace.size() < maxBlocks) {
            Point3d currentCandidate = candidates.removeFirst();

            Point3d supportingPoint = currentCandidate.move(placeDirection.getOpposite());
            Block candidateSupportingBlock = world.getBlock(supportingPoint);
            int candidateSupportingMeta = world.getMetadata(supportingPoint);
            AxisAlignedBB blockBB =targetBlock.getCollisionBoundingBoxFromPool(world.getWorld(), currentCandidate.x, currentCandidate.y, currentCandidate.z);
            if(shouldContinue(currentCandidate, targetBlock, targetMetadata, candidateSupportingBlock, candidateSupportingMeta, blockBB)
                    && allCandidates.add(currentCandidate)) {
                toPlace.add(currentCandidate);

                switch (placeDirection) {
                    case DOWN:
                    case UP:
                        if((faceMaskInt & EnumLock.UP_DOWN_MASK) > 0) {
                            if ((directionMaskInt & EnumLock.NORTH_SOUTH_MASK) > 0)
                                candidates.add(currentCandidate.move(ForgeDirection.NORTH));
                            if ((directionMaskInt & EnumLock.EAST_WEST_MASK) > 0)
                                candidates.add(currentCandidate.move(ForgeDirection.EAST));
                            if ((directionMaskInt & EnumLock.NORTH_SOUTH_MASK) > 0)
                                candidates.add(currentCandidate.move(ForgeDirection.SOUTH));
                            if ((directionMaskInt & EnumLock.EAST_WEST_MASK) > 0)
                                candidates.add(currentCandidate.move(ForgeDirection.WEST));
                            if ((directionMaskInt & EnumLock.NORTH_SOUTH_MASK) > 0 && (directionMaskInt & EnumLock.EAST_WEST_MASK) > 0) {
                                candidates.add(currentCandidate.move(ForgeDirection.NORTH).move(ForgeDirection.EAST));
                                candidates.add(currentCandidate.move(ForgeDirection.NORTH).move(ForgeDirection.WEST));
                                candidates.add(currentCandidate.move(ForgeDirection.SOUTH).move(ForgeDirection.EAST));
                                candidates.add(currentCandidate.move(ForgeDirection.SOUTH).move(ForgeDirection.WEST));
                            }
                        }
                        break;
                    case NORTH:
                    case SOUTH:
                        if((faceMaskInt & EnumLock.NORTH_SOUTH_MASK) > 0) {
                            if ((directionMaskInt & EnumLock.UP_DOWN_MASK) > 0)
                                candidates.add(currentCandidate.move(ForgeDirection.UP));
                            if ((directionMaskInt & EnumLock.EAST_WEST_MASK) > 0)
                                candidates.add(currentCandidate.move(ForgeDirection.EAST));
                            if ((directionMaskInt & EnumLock.UP_DOWN_MASK) > 0)
                                candidates.add(currentCandidate.move(ForgeDirection.DOWN));
                            if ((directionMaskInt & EnumLock.EAST_WEST_MASK) > 0)
                                candidates.add(currentCandidate.move(ForgeDirection.WEST));
                            if ((directionMaskInt & EnumLock.UP_DOWN_MASK) > 0 && (directionMaskInt & EnumLock.EAST_WEST_MASK) > 0) {
                                candidates.add(currentCandidate.move(ForgeDirection.UP).move(ForgeDirection.EAST));
                                candidates.add(currentCandidate.move(ForgeDirection.UP).move(ForgeDirection.WEST));
                                candidates.add(currentCandidate.move(ForgeDirection.DOWN).move(ForgeDirection.EAST));
                                candidates.add(currentCandidate.move(ForgeDirection.DOWN).move(ForgeDirection.WEST));
                            }
                        }
                        break;
                    case WEST:
                    case EAST:
                        if((faceMaskInt & EnumLock.EAST_WEST_MASK) > 0) {
                            if ((directionMaskInt & EnumLock.UP_DOWN_MASK) > 0)
                                candidates.add(currentCandidate.move(ForgeDirection.UP));
                            if ((directionMaskInt & EnumLock.NORTH_SOUTH_MASK) > 0)
                                candidates.add(currentCandidate.move(ForgeDirection.NORTH));
                            if ((directionMaskInt & EnumLock.UP_DOWN_MASK) > 0)
                                candidates.add(currentCandidate.move(ForgeDirection.DOWN));
                            if ((directionMaskInt & EnumLock.NORTH_SOUTH_MASK) > 0)
                                candidates.add(currentCandidate.move(ForgeDirection.SOUTH));
                            if ((directionMaskInt & EnumLock.UP_DOWN_MASK) > 0 && (directionMaskInt & EnumLock.NORTH_SOUTH_MASK) > 0) {
                                candidates.add(currentCandidate.move(ForgeDirection.UP).move(ForgeDirection.NORTH));
                                candidates.add(currentCandidate.move(ForgeDirection.UP).move(ForgeDirection.SOUTH));
                                candidates.add(currentCandidate.move(ForgeDirection.DOWN).move(ForgeDirection.NORTH));
                                candidates.add(currentCandidate.move(ForgeDirection.DOWN).move(ForgeDirection.SOUTH));
                            }
                        }
                }
            }
        }
        return toPlace;
    }

    public void placeBlocks(ItemStack itemStack, LinkedList<Point3d> blockPosList, Point3d originalBlock) {
        for(Point3d blockPos : blockPosList) {
            /*if(player.countItems(getEquivalentItemStack(originalBlock)) < 1) {
                break;
            }*/

            boolean blockPlaceSuccess = world.copyBlock(originalBlock, blockPos);

            if(blockPlaceSuccess) {
                world.playPlaceAtBlock(blockPos, world.getBlock(originalBlock));
                if (!player.isCreative()) {
                    wand.placeBlock(itemStack, player.getPlayer());
                }
                boolean takeFromInventory = player.useItem(getEquivalentItemStack(originalBlock));
                if(!takeFromInventory) {
                    FMLLog.info("BBW takeback: %s", blockPos.toString());
                    world.setBlockToAir(blockPos);
                }
            }
        }
    }
}
