package portablejim.bbw.core;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.ForgeEventFactory;
import org.apache.logging.log4j.Logger;
import portablejim.bbw.BetterBuildersWandsMod;
import portablejim.bbw.core.wands.IWand;
import portablejim.bbw.basics.EnumLock;
import portablejim.bbw.basics.Point3d;
import portablejim.bbw.shims.IPlayerShim;
import portablejim.bbw.shims.IWorldShim;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

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

    public ItemStack getProperItemStack(IWorldShim world, IPlayerShim player, Point3d blockPos) {
        Block block = world.getBlock(blockPos);
        int meta = world.getMetadata(blockPos);
        ItemStack exactItemstack = new ItemStack(block, 1, meta);
         if(player.countItems(exactItemstack) > 0) {
             return exactItemstack;
         }
        return getEquivalentItemStack(blockPos);
    }

    public ItemStack getEquivalentItemStack(Point3d blockPos) {
        Block block = world.getBlock(blockPos);
        int meta = world.getMetadata(blockPos);
        //ArrayList<ItemStack> items = new ArrayList<ItemStack>();
        ItemStack stack = null;
        if(block.canSilkHarvest(world.getWorld(), player.getPlayer(), blockPos.x, blockPos.y, blockPos.z, meta)) {
            stack = BetterBuildersWandsMod.instance.blockCache.getStackedBlock(world, blockPos);
        }
        else {
            Item dropped = block.getItemDropped(meta, new Random(), 0);
            if (dropped != null) {
                stack = new ItemStack(dropped, block.quantityDropped(meta, 0, new Random()), block.damageDropped(meta));
            }
        }
        //ForgeEventFactory.fireBlockHarvesting(items,this.world.getWorld(), block, blockPos.x, blockPos.y, blockPos.z, world.getMetadata(blockPos), 0, 1.0F, true, this.player.getPlayer());
        return stack;
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

    public ArrayList<Point3d> placeBlocks(ItemStack wandItem, LinkedList<Point3d> blockPosList, Point3d originalBlock, ItemStack sourceItems, int side, float hitX, float hitY, float hitZ) {
        ArrayList<Point3d> placedBlocks = new ArrayList<Point3d>();
        for(Point3d blockPos : blockPosList) {
            boolean blockPlaceSuccess = world.copyBlock(originalBlock, blockPos);

            if(blockPlaceSuccess) {
                Item itemFromBlock = Item.getItemFromBlock(world.getBlock(originalBlock));
                world.playPlaceAtBlock(blockPos, world.getBlock(originalBlock));
                /*if(sourceItems.getItem() instanceof ItemBlock) {
                    ItemBlock itemBlock = (ItemBlock) sourceItems.getItem();
                    itemBlock.placeBlockAt(sourceItems, player.getPlayer(), world.getWorld(), blockPos.x, blockPos.y, blockPos.z, side, hitX, hitY, hitZ, sourceItems.getItemDamage());
                }
                else {
                    world.playPlaceAtBlock(blockPos, world.getBlock(originalBlock));
                }*/
                placedBlocks.add(blockPos);
                if (!player.isCreative()) {
                    wand.placeBlock(wandItem, player.getPlayer());
                }
                boolean takeFromInventory = player.useItem(sourceItems);
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
