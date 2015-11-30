package portablejim.bbw.core;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;
import portablejim.bbw.IWand;
import portablejim.bbw.basics.EnumLock;
import portablejim.bbw.basics.Point3d;
import portablejim.bbw.core.items.IWandItem;
import portablejim.bbw.core.wands.UnbreakingWand;
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
                IWand wand = wandItem.getWand(event.currentItem);

                WandWorker worker = new WandWorker(wand, playerShim, worldShim);

                Point3d clickedPos = new Point3d(event.target.blockX, event.target.blockY, event.target.blockZ);

                ItemStack targetItemstack = worker.getEquivalentItemStack(clickedPos);
                int numBlocks = Math.min(wand.getMaxBlocks(), playerShim.countItems(targetItemstack));

                LinkedList<Point3d> blocks = worker.getBlockPositionList(clickedPos, ForgeDirection.getOrientation(event.target.sideHit), numBlocks, wand.getMode(), wandItem.getFaceLock(event.currentItem));
                if (blocks.size() > 0) {
                    GL11.glDisable(GL11.GL_TEXTURE_2D);
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glDepthMask(true);
                    GL11.glLineWidth(2.5F);
                    for (Point3d block : blocks) {
                        AxisAlignedBB boundingBox = AxisAlignedBB.getBoundingBox(block.x, block.y, block.z, block.x + 1, block.y + 1, block.z + 1).contract(0.005, 0.005, 0.005);
                        RenderGlobal.drawOutlinedBoundingBox(boundingBox.getOffsetBoundingBox(-event.player.posX, -event.player.posY, -event.player.posZ), 0xC0C0C0);
                    }
                    GL11.glEnable(GL11.GL_TEXTURE_2D);
                    GL11.glDisable(GL11.GL_BLEND);
                }
            }
        }
        //FMLLog.info("Happened!" + event.target.toString());
    }
}
