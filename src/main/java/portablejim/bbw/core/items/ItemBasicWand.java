package portablejim.bbw.core.items;

import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import portablejim.bbw.BetterBuildersWandsMod;
import portablejim.bbw.basics.EnumFluidLock;
import portablejim.bbw.basics.EnumLock;
import portablejim.bbw.basics.Point3d;
import portablejim.bbw.core.WandWorker;
import portablejim.bbw.core.wands.IWand;
import portablejim.bbw.shims.BasicPlayerShim;
import portablejim.bbw.shims.BasicWorldShim;
import portablejim.bbw.shims.CreativePlayerShim;
import portablejim.bbw.shims.IPlayerShim;
import portablejim.bbw.shims.IWorldShim;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Class to cover common functions between wands.
 */
public abstract class ItemBasicWand extends Item implements IWandItem{
    public IWand wand;

    public ItemBasicWand() {
        super();
        this.setCreativeTab(CreativeTabs.TOOLS);
        this.setMaxStackSize(1);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(wand == null) {
            return EnumActionResult.FAIL;
        }
        ItemStack itemstack = player.getHeldItem(hand);
        if(itemstack == null) {
            BetterBuildersWandsMod.logger.error("BasicWand onItemUse itemstack empty");
            return EnumActionResult.FAIL;
        }

        if(!world.isRemote) {
            IPlayerShim playerShim = new BasicPlayerShim(player);
            if (player.capabilities.isCreativeMode) {
                playerShim = new CreativePlayerShim(player);
            }
            IWorldShim worldShim = new BasicWorldShim(world);

            WandWorker worker = new WandWorker(this.wand, playerShim, worldShim);

            Point3d clickedPos = new Point3d(pos.getX(), pos.getY(), pos.getZ());

            //ItemStack pickBlock = worldShim.getBlock(clickedPos).getPickBlock(this.getMovingObjectPositionFromPlayer(world, player, false), world, x, y, z, player);

            ItemStack  sourceItems = worker.getProperItemStack(worldShim, playerShim, clickedPos);

            if (sourceItems != null && sourceItems.getItem() instanceof ItemBlock) {
                int numBlocks = Math.min(wand.getMaxBlocks(itemstack), playerShim.countItems(sourceItems));

                //FMLLog.info("Max blocks: %d (%d|%d", numBlocks, this.wand.getMaxBlocks(itemstack), playerShim.countItems(sourceItems));
                LinkedList<Point3d> blocks = worker.getBlockPositionList(clickedPos, side, numBlocks, getMode(itemstack), getFaceLock(itemstack), getFluidMode(itemstack));

                ArrayList<Point3d> placedBlocks = worker.placeBlocks(itemstack, blocks, clickedPos, sourceItems, side, hitX, hitY, hitZ);
                if(placedBlocks.size() > 0) {
                    int[] placedIntArray = new int[placedBlocks.size() * 3];
                    for (int i = 0; i < placedBlocks.size(); i++) {
                        Point3d currentPoint = placedBlocks.get(i);
                        placedIntArray[i * 3] = currentPoint.x;
                        placedIntArray[i * 3 + 1] = currentPoint.y;
                        placedIntArray[i * 3 + 2] = currentPoint.z;
                    }
                    NBTTagCompound itemNBT = itemstack.hasTagCompound() ? itemstack.getTagCompound() : new NBTTagCompound();
                    NBTTagCompound bbwCompound = new NBTTagCompound();
                    if(itemNBT.hasKey("bbw", Constants.NBT.TAG_COMPOUND)) {
                        bbwCompound = itemNBT.getCompoundTag("bbw");
                    }
                    if(!bbwCompound.hasKey("mask", Constants.NBT.TAG_SHORT)) {
                        bbwCompound.setShort("mask", (short) this.getDefaultMode().mask);
                    }
                    if(!bbwCompound.hasKey("fluidmask", Constants.NBT.TAG_SHORT)) {
                        bbwCompound.setShort("fluidmask", (short) this.getDefaultFluidMode().mask);
                    }
                    bbwCompound.setIntArray("lastPlaced", placedIntArray);
                    bbwCompound.setString("lastBlock", Item.REGISTRY.getNameForObject(sourceItems.getItem()).toString());
                    bbwCompound.setInteger("lastBlockMeta", sourceItems.getItemDamage());
                    bbwCompound.setInteger("lastPerBlock", sourceItems.getCount());
                    itemstack.setTagInfo("bbw", bbwCompound);
                }
            }

        }
        return EnumActionResult.SUCCESS;
    }

    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack itemstack, EntityPlayer player, List lines, boolean extraInfo) {


        EnumLock mode = getMode(itemstack);
        switch (mode) {
            case NORTHSOUTH:
                lines.add(BetterBuildersWandsMod.proxy.Translate(BetterBuildersWandsMod.LANGID + ".hover.mode.northsouth"));
                break;
            case VERTICAL:
                lines.add(BetterBuildersWandsMod.proxy.Translate(BetterBuildersWandsMod.LANGID + ".hover.mode.vertical"));
                break;
            case VERTICALEASTWEST:
                lines.add(BetterBuildersWandsMod.proxy.Translate(BetterBuildersWandsMod.LANGID + ".hover.mode.verticaleastwest"));
                break;
            case EASTWEST:
                lines.add(BetterBuildersWandsMod.proxy.Translate(BetterBuildersWandsMod.LANGID + ".hover.mode.eastwest"));
                break;
            case HORIZONTAL:
                lines.add(BetterBuildersWandsMod.proxy.Translate(BetterBuildersWandsMod.LANGID + ".hover.mode.horizontal"));
                break;
            case VERTICALNORTHSOUTH:
                lines.add(BetterBuildersWandsMod.proxy.Translate(BetterBuildersWandsMod.LANGID + ".hover.mode.verticalnorthsouth"));
                break;
            case NOLOCK:
                lines.add(BetterBuildersWandsMod.proxy.Translate(BetterBuildersWandsMod.LANGID + ".hover.mode.nolock"));
                break;
        }
        EnumFluidLock fluidMode = getFluidMode(itemstack);
        switch (fluidMode) {
            case STOPAT:
                lines.add(BetterBuildersWandsMod.proxy.Translate(BetterBuildersWandsMod.LANGID + ".hover.fluidmode.stopat"));
                break;
            case IGNORE:
                lines.add(BetterBuildersWandsMod.proxy.Translate(BetterBuildersWandsMod.LANGID + ".hover.fluidmode.ignore"));
                break;
        }

        if(!itemstack.isItemStackDamageable() || !itemstack.isItemDamaged()) {
            lines.add(BetterBuildersWandsMod.proxy.Translate(BetterBuildersWandsMod.LANGID + ".hover.maxblocks", wand.getMaxBlocks(itemstack)));
        }
    }

    @Override
    public boolean canHarvestBlock(IBlockState blockIn) {
        return false;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState blockIn, BlockPos pos, EntityLivingBase playerIn) {
        stack.damageItem(2, playerIn);
        return true;
    }

    public boolean hitEntity(ItemStack p_77644_1_, EntityLivingBase p_77644_2_, EntityLivingBase p_77644_3_)
    {
        p_77644_1_.damageItem(2, p_77644_3_);
        return true;
    }

    public void setMode(ItemStack item, EnumLock mode) {
        NBTTagCompound tagCompound = new NBTTagCompound();
        if(item.hasTagCompound()) {
            tagCompound = item.getTagCompound();
        }
        NBTTagCompound bbwCompond = new NBTTagCompound();
        if(tagCompound.hasKey("bbw", Constants.NBT.TAG_COMPOUND)) {
            bbwCompond = tagCompound.getCompoundTag("bbw");
        }
        short shortMask = (short) (mode.mask & 7);
        bbwCompond.setShort("mask", shortMask);
        tagCompound.setTag("bbw", bbwCompond);
        item.setTagCompound(tagCompound);
    }

    public EnumLock getMode(ItemStack item) {
        if(item != null && item.getItem() != null && item.getItem() instanceof IWandItem) {
            NBTTagCompound itemBaseNBT = item.getTagCompound();
            if(itemBaseNBT != null && itemBaseNBT.hasKey("bbw", Constants.NBT.TAG_COMPOUND)) {
                NBTTagCompound itemNBT = itemBaseNBT.getCompoundTag("bbw");
                int mask = itemNBT.hasKey("mask", Constants.NBT.TAG_SHORT) ? itemNBT.getShort("mask") : EnumLock.NOLOCK.mask;
                return EnumLock.fromMask(mask);
            }
        }
        return getDefaultMode();
    }

    public void setFluidMode(ItemStack item, EnumFluidLock mode) {
        NBTTagCompound tagCompound = new NBTTagCompound();
        if(item.hasTagCompound()) {
            tagCompound = item.getTagCompound();
        }
        NBTTagCompound bbwCompond = new NBTTagCompound();
        if(tagCompound.hasKey("bbw", Constants.NBT.TAG_COMPOUND)) {
            bbwCompond = tagCompound.getCompoundTag("bbw");
        }
        short shortMask = (short) (mode.mask & 7);
        bbwCompond.setShort("fluidmask", shortMask);
        tagCompound.setTag("bbw", bbwCompond);
        item.setTagCompound(tagCompound);
    }

    public EnumFluidLock getFluidMode(ItemStack item) {
        if(item != null && item.getItem() != null && item.getItem() instanceof IWandItem) {
            NBTTagCompound itemBaseNBT = item.getTagCompound();
            if(itemBaseNBT != null && itemBaseNBT.hasKey("bbw", Constants.NBT.TAG_COMPOUND)) {
                NBTTagCompound itemNBT = itemBaseNBT.getCompoundTag("bbw");
                int mask = itemNBT.hasKey("fluidmask", Constants.NBT.TAG_SHORT) ? itemNBT.getShort("fluidmask") : EnumFluidLock.STOPAT.mask;
                return EnumFluidLock.fromMask(mask);
            }
        }
        return getDefaultFluidMode();
    }

    public IWand getWand() {
        return this.wand;
    }

    public EnumLock getDefaultMode() {
        return EnumLock.NOLOCK;
    }

    public EnumFluidLock getDefaultFluidMode() {
        return EnumFluidLock.STOPAT;
    }
}
