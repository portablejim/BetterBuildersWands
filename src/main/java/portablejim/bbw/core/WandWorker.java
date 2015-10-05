package portablejim.bbw.core;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import portablejim.bbw.IWand;
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
        return block == null ? null : new ItemStack(block.getItem(world.getWorld(), blockPos.x, blockPos.y, blockPos.z), 1, block.getDamageValue(world.getWorld(), blockPos.x, blockPos.y, blockPos.z));
    }

    public LinkedList<Point3d> getBlockPositionList(Point3d blockLookedAt, ForgeDirection placeDirection, int maxBlocks) {
        LinkedList<Point3d> candidates = new LinkedList<Point3d>();
        LinkedList<Point3d> toPlace = new LinkedList<Point3d>();

        Block targetBlock = world.getBlock(blockLookedAt);
        Point3d startingPoint = blockLookedAt.move(placeDirection);

        candidates.add(startingPoint);
        while(candidates.size() > 0 && toPlace.size() < maxBlocks) {
            Point3d currentCandidate = candidates.removeFirst();

            Point3d supportingPoint = currentCandidate.move(placeDirection.getOpposite());
            Block candidateSupportingBlock = world.getBlock(supportingPoint);
            if(world.blockIsAir(currentCandidate)
                    && targetBlock.equals(candidateSupportingBlock)
                    && allCandidates.add(currentCandidate)) {
                toPlace.add(currentCandidate);
                switch (placeDirection) {
                    case DOWN:
                    case UP:
                        candidates.add(currentCandidate.move(ForgeDirection.NORTH));
                        candidates.add(currentCandidate.move(ForgeDirection.EAST));
                        candidates.add(currentCandidate.move(ForgeDirection.SOUTH));
                        candidates.add(currentCandidate.move(ForgeDirection.WEST));
                        candidates.add(currentCandidate.move(ForgeDirection.NORTH).move(ForgeDirection.EAST));
                        candidates.add(currentCandidate.move(ForgeDirection.NORTH).move(ForgeDirection.WEST));
                        candidates.add(currentCandidate.move(ForgeDirection.SOUTH).move(ForgeDirection.EAST));
                        candidates.add(currentCandidate.move(ForgeDirection.SOUTH).move(ForgeDirection.WEST));
                        break;
                    case NORTH:
                    case SOUTH:
                        candidates.add(currentCandidate.move(ForgeDirection.UP));
                        candidates.add(currentCandidate.move(ForgeDirection.EAST));
                        candidates.add(currentCandidate.move(ForgeDirection.DOWN));
                        candidates.add(currentCandidate.move(ForgeDirection.WEST));
                        candidates.add(currentCandidate.move(ForgeDirection.UP).move(ForgeDirection.EAST));
                        candidates.add(currentCandidate.move(ForgeDirection.UP).move(ForgeDirection.WEST));
                        candidates.add(currentCandidate.move(ForgeDirection.DOWN).move(ForgeDirection.EAST));
                        candidates.add(currentCandidate.move(ForgeDirection.DOWN).move(ForgeDirection.WEST));
                        break;
                    case WEST:
                    case EAST:
                        candidates.add(currentCandidate.move(ForgeDirection.UP));
                        candidates.add(currentCandidate.move(ForgeDirection.NORTH));
                        candidates.add(currentCandidate.move(ForgeDirection.DOWN));
                        candidates.add(currentCandidate.move(ForgeDirection.SOUTH));
                        candidates.add(currentCandidate.move(ForgeDirection.UP).move(ForgeDirection.NORTH));
                        candidates.add(currentCandidate.move(ForgeDirection.UP).move(ForgeDirection.SOUTH));
                        candidates.add(currentCandidate.move(ForgeDirection.DOWN).move(ForgeDirection.NORTH));
                        candidates.add(currentCandidate.move(ForgeDirection.DOWN).move(ForgeDirection.SOUTH));
                }
            }
        }
        return toPlace;
    }

    public void placeBlocks(LinkedList<Point3d> blockPosList, Point3d originalBlock) {
        for(Point3d blockPos : blockPosList) {
            boolean blockPlaceSuccess = world.copyBlock(originalBlock, blockPos);

            if(blockPlaceSuccess) {
                boolean takeFromInventory = player.useItem(getEquivalentItemStack(originalBlock));
                if(!takeFromInventory) {
                    world.setBlockToAir(blockPos);
                }
            }
        }
    }
}
