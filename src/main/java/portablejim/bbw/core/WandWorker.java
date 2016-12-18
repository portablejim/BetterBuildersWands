package portablejim.bbw.core;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import portablejim.bbw.BetterBuildersWandsMod;
import portablejim.bbw.basics.EnumFluidLock;
import portablejim.bbw.basics.ReplacementTriplet;
import portablejim.bbw.core.conversion.CustomMapping;
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

    public ReplacementTriplet getProperItemStack(IWorldShim world, IPlayerShim player, Point3d blockPos) {
        Block block = world.getBlock(blockPos);
        IBlockState startBlockState = world.getWorld().getBlockState(blockPos.toBlockPos());
        int meta = world.getMetadata(blockPos);
        String blockString = String.format("%s/%s", Block.REGISTRY.getNameForObject(block), meta);
        if(!BetterBuildersWandsMod.instance.configValues.HARD_BLACKLIST_SET.contains(blockString)) {
            ArrayList<CustomMapping> customMappings = BetterBuildersWandsMod.instance.mappingManager.getMappings(block, meta);
            for (CustomMapping customMapping : customMappings) {
                if(player.countItems(customMapping.getItems()) > 0) {
                    return new ReplacementTriplet(customMapping.getLookBlock().getStateFromMeta(customMapping.getMeta()),
                            customMapping.getItems(), customMapping.getPlaceBlock().getStateFromMeta(customMapping.getPlaceMeta()));
                }
            }

            // Handle slabs specially.
            if(startBlockState.getBlock() instanceof BlockSlab) {
                Item itemDropped = startBlockState.getBlock().getItemDropped(startBlockState, world.rand(), 0);
                ItemStack itemStackDropped = new ItemStack(itemDropped, startBlockState.getBlock().quantityDropped(world.rand()), startBlockState.getBlock().damageDropped(startBlockState));
                if(itemDropped != null && player.countItems(itemStackDropped) > 0) {
                    return new ReplacementTriplet(startBlockState, itemStackDropped, startBlockState);
                }
            }

            ItemStack exactItemstack = block.getPickBlock(startBlockState, ForgeHooks.rayTraceEyes(player.getPlayer(), player.getReachDistance()), world.getWorld(), blockPos.toBlockPos(), player.getPlayer());
            if (player.countItems(exactItemstack) > 0) {
                if(exactItemstack != null && exactItemstack.getItem() instanceof ItemBlock) {
                    IBlockState newState = ((ItemBlock) exactItemstack.getItem()).getBlock().getStateFromMeta(exactItemstack.getMetadata());
                    return new ReplacementTriplet(startBlockState, exactItemstack, newState);
                }
                else {
                    return null;
                }
            }
            return getEquivalentItemStack(blockPos);
        }
        return null;
    }

    public ReplacementTriplet getEquivalentItemStack(Point3d blockPos) {
        Block block = world.getBlock(blockPos);
        int meta = world.getMetadata(blockPos);
        IBlockState startBlockState = world.getWorld().getBlockState(blockPos.toBlockPos());
        //ArrayList<ItemStack> items = new ArrayList<ItemStack>();
        String blockString = String.format("%s/%s", Block.REGISTRY.getNameForObject(block), meta);

        if(block.canSilkHarvest(world.getWorld(), blockPos.toBlockPos(), block.getStateFromMeta(meta), player.getPlayer())) {
        }
        else if(!BetterBuildersWandsMod.instance.configValues.SOFT_BLACKLIST_SET.contains(blockString)) {
            Item dropped = block.getItemDropped(block.getStateFromMeta(meta), new Random(), 0);
            if (dropped != null) {
                ItemStack stack = new ItemStack(dropped, block.quantityDropped(block.getStateFromMeta(meta), 0, new Random()), block.damageDropped(block.getStateFromMeta(meta)));
                if (stack.getItem() instanceof ItemBlock) {
                    return new ReplacementTriplet(startBlockState, stack, ((ItemBlock) stack.getItem()).getBlock().getStateFromMeta(stack.getMetadata()));
                }
            }
        }
        //ForgeEventFactory.fireBlockHarvesting(items,this.world.getWorld(), block, blockPos.x, blockPos.y, blockPos.z, world.getMetadata(blockPos), 0, 1.0F, true, this.player.getPlayer());
        return null;
    }

    private boolean shouldContinue(Point3d currentCandidate, Block targetBlock, int targetMetadata, EnumFacing facing, Block candidateSupportingBlock, int candidateSupportingMeta, AxisAlignedBB blockBB, EnumFluidLock fluidLock) {
        if(!world.blockIsAir(currentCandidate)){
            Block currrentCandidateBlock = world.getBlock(currentCandidate);
            if(!(fluidLock == EnumFluidLock.IGNORE && currrentCandidateBlock != null && (currrentCandidateBlock instanceof IFluidBlock || currrentCandidateBlock instanceof BlockLiquid))) return false;
        };
        /*if((FluidRegistry.getFluid("water").getBlock().equals(world.getBlock(currentCandidate)) || FluidRegistry.getFluid("lava").getBlock().equals(world.getBlock(currentCandidate)))
                && world.getMetadata(currentCandidate) == 0){
            return false;
        }*/
        if(!targetBlock.equals(candidateSupportingBlock)) return false;
        if(targetMetadata != candidateSupportingMeta) return false;
        //if(targetBlock instanceof BlockCrops) return false;
        if(!targetBlock.canPlaceBlockAt(world.getWorld(), new BlockPos(currentCandidate.x, currentCandidate.y, currentCandidate.z))) return false;
        if(!targetBlock.canReplace(world.getWorld(), new BlockPos(currentCandidate.x, currentCandidate.y, currentCandidate.z), facing, new ItemStack(candidateSupportingBlock, 1, candidateSupportingMeta))) return false;

        return !world.entitiesInBox(blockBB);

    }

    public LinkedList<Point3d> getBlockPositionList(Point3d blockLookedAt, EnumFacing placeDirection, int maxBlocks, EnumLock directionLock, EnumLock faceLock, EnumFluidLock fluidLock) {
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
            AxisAlignedBB blockBB = targetBlock.getStateFromMeta(targetMetadata).getBoundingBox(world.getWorld(), new BlockPos(currentCandidate.x, currentCandidate.y, currentCandidate.z)).offset(currentCandidate.x, currentCandidate.y, currentCandidate.z);
            if(shouldContinue(currentCandidate, targetBlock, targetMetadata, placeDirection, candidateSupportingBlock, candidateSupportingMeta, blockBB, fluidLock)
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

    public ArrayList<Point3d> placeBlocks(ItemStack wandItem, LinkedList<Point3d> blockPosList, IBlockState targetBlock, ItemStack sourceItems, EnumFacing side, float hitX, float hitY, float hitZ) {
        ArrayList<Point3d> placedBlocks = new ArrayList<Point3d>();
        for(Point3d blockPos : blockPosList) {
            boolean blockPlaceSuccess;
            blockPlaceSuccess = world.setBlock(blockPos, targetBlock);

            if(blockPlaceSuccess) {
                world.playPlaceAtBlock(blockPos, targetBlock.getBlock());
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
