package portablejim.bbw.core;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import org.lwjgl.opengl.GL11;
import portablejim.bbw.core.wands.IWand;
import portablejim.bbw.basics.Point3d;
import portablejim.bbw.core.items.IWandItem;
import portablejim.bbw.shims.BasicPlayerShim;
import portablejim.bbw.shims.BasicWorldShim;
import portablejim.bbw.shims.CreativePlayerShim;
import portablejim.bbw.shims.IPlayerShim;
import portablejim.bbw.shims.IWorldShim;

import java.util.LinkedList;

/**
 * Events for supporting wands.
 */
public class BlockEvents {
    @SubscribeEvent
    public void blockHighlightEvent(DrawBlockHighlightEvent event) {
        if(event.currentItem != null && event.currentItem.getItem() instanceof IWandItem
                && event.target != null && event.target.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            IPlayerShim playerShim = new BasicPlayerShim(event.player);
            if(event.player.capabilities.isCreativeMode) {
                playerShim = new CreativePlayerShim(event.player);
            }
            IWorldShim worldShim = new BasicWorldShim(event.player.getEntityWorld());
            if(event.currentItem.getItem() instanceof IWandItem) {
                IWandItem wandItem = (IWandItem) event.currentItem.getItem();
                IWand wand = wandItem.getWand();

                WandWorker worker = new WandWorker(wand, playerShim, worldShim);

                Point3d clickedPos = new Point3d(event.target.getBlockPos().getX(), event.target.getBlockPos().getY(), event.target.getBlockPos().getZ());
                ItemStack  sourceItems = worker.getProperItemStack(worldShim, playerShim, clickedPos);

                if (sourceItems != null && sourceItems.getItem() instanceof ItemBlock) {
                    int numBlocks = Math.min(wand.getMaxBlocks(event.currentItem), playerShim.countItems(sourceItems));

                    LinkedList<Point3d> blocks = worker.getBlockPositionList(clickedPos, event.target.sideHit, numBlocks, wandItem.getMode(event.currentItem), wandItem.getFaceLock(event.currentItem));
                    if (blocks.size() > 0) {
                        GlStateManager.disableTexture2D();
                        GlStateManager.disableBlend();
                        GlStateManager.depthMask(true);
                        GL11.glLineWidth(2.5F);
                        for (Point3d block : blocks) {
                            Block blockb = Blocks.bedrock;
                            EntityPlayer player = event.player;
                            double partialTicks = event.partialTicks;
                            double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)partialTicks;
                            double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)partialTicks;
                            double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)partialTicks;
                            RenderGlobal.func_181561_a(blockb.getSelectedBoundingBox(worldShim.getWorld(), new BlockPos(block.x, block.y, block.z)).contract(0.005, 0.005, 0.005).offset(-d0, -d1, -d2));

                        }
                        GL11.glEnable(GL11.GL_TEXTURE_2D);
                        GL11.glDisable(GL11.GL_BLEND);
                        GlStateManager.enableTexture2D();
                        GlStateManager.enableBlend();
                        GlStateManager.depthMask(false);
                    }
                }
            }
        }
        //FMLLog.info("Happened!" + event.target.toString());
    }
}
