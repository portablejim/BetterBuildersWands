package portablejim.bbw.core.items;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;
import portablejim.bbw.BetterBuildersWandsMod;
import portablejim.bbw.core.wands.IWand;
import portablejim.bbw.basics.EnumLock;
import portablejim.bbw.basics.Point3d;
import portablejim.bbw.core.WandWorker;
import portablejim.bbw.shims.BasicPlayerShim;
import portablejim.bbw.shims.BasicWorldShim;
import portablejim.bbw.shims.CreativePlayerShim;
import portablejim.bbw.shims.IPlayerShim;
import portablejim.bbw.shims.IWorldShim;

import java.util.LinkedList;
import java.util.List;

/**
 * Class to cover common functions between wands.
 */
public abstract class ItemBasicWand extends Item implements IWandItem{
    public IWand wand;

    public ItemBasicWand() {
        super();
        this.setCreativeTab(CreativeTabs.tabTools);
        this.setMaxStackSize(1);
    }

    public boolean onItemUse(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if(wand == null) {
            return false;
        }

        if(!world.isRemote) {
            IPlayerShim playerShim = new BasicPlayerShim(player);
            if (player.capabilities.isCreativeMode) {
                playerShim = new CreativePlayerShim(player);
            }
            IWorldShim worldShim = new BasicWorldShim(world);

            WandWorker worker = new WandWorker(this.wand, playerShim, worldShim);

            Point3d clickedPos = new Point3d(x, y, z);

            ItemStack targetItemstack = worker.getEquivalentItemStack(clickedPos);
            int numBlocks = Math.min(this.wand.getMaxBlocks(itemstack), playerShim.countItems(targetItemstack));
            FMLLog.info("Max blocks: %d (%d|%d", numBlocks, this.wand.getMaxBlocks(itemstack), playerShim.countItems(targetItemstack));

            if (targetItemstack != null && targetItemstack.isItemEqual(new ItemStack(worldShim.getBlock(clickedPos), 1, worldShim.getMetadata(clickedPos)))) {
                LinkedList<Point3d> blocks = worker.getBlockPositionList(clickedPos, ForgeDirection.getOrientation(side), numBlocks, getMode(itemstack), getFaceLock(itemstack));

                worker.placeBlocks(itemstack, blocks, clickedPos);
            }

        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack itemstack, EntityPlayer player, List lines, boolean extraInfo) {

        EnumLock mode = getMode(itemstack);
        switch (mode) {
            case NORTHSOUTH:
                lines.add(StatCollector.translateToLocal(BetterBuildersWandsMod.LANGID + ".hover.mode.northsouth"));
                break;
            case VERTICAL:
                lines.add(StatCollector.translateToLocal(BetterBuildersWandsMod.LANGID + ".hover.mode.vertical"));
                break;
            case VERTICALEASTWEST:
                lines.add(StatCollector.translateToLocal(BetterBuildersWandsMod.LANGID + ".hover.mode.verticaleastwest"));
                break;
            case EASTWEST:
                lines.add(StatCollector.translateToLocal(BetterBuildersWandsMod.LANGID + ".hover.mode.eastwest"));
                break;
            case HORIZONTAL:
                lines.add(StatCollector.translateToLocal(BetterBuildersWandsMod.LANGID + ".hover.mode.horizontal"));
                break;
            case VERTICALNORTHSOUTH:
                lines.add(StatCollector.translateToLocal(BetterBuildersWandsMod.LANGID + ".hover.mode.verticalnorthsouth"));
                break;
            case NOLOCK:
                lines.add(StatCollector.translateToLocal(BetterBuildersWandsMod.LANGID + ".hover.mode.nolock"));
                break;
        }
    }

    @Override
    public boolean canHarvestBlock(Block par1Block, ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack itemStack, World world, Block block, int x, int y, int z, EntityLivingBase entityLivingBase) {
        itemStack.damageItem(2, entityLivingBase);
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

    public IWand getWand() {
        return this.wand;
    }

    public EnumLock getDefaultMode() {
        return EnumLock.NOLOCK;
    }
}
