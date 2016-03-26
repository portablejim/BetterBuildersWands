package portablejim.bbw.core;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
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
        if(event.getTarget() != null && event.getTarget().typeOfHit == RayTraceResult.Type.BLOCK) {
            IPlayerShim playerShim = new BasicPlayerShim(event.getPlayer());
            if(event.getPlayer().capabilities.isCreativeMode) {
                playerShim = new CreativePlayerShim(event.getPlayer());
            }

            ItemStack wandItemstack = playerShim.getHeldWandIfAny();
            IBlockState state = event.getPlayer().getEntityWorld().getBlockState(event.getTarget().getBlockPos());

            IWorldShim worldShim = new BasicWorldShim(event.getPlayer().getEntityWorld());
            if(wandItemstack != null && wandItemstack.getItem() instanceof IWandItem) {
                IWandItem wandItem = (IWandItem) wandItemstack.getItem();
                IWand wand = wandItem.getWand();

                WandWorker worker = new WandWorker(wand, playerShim, worldShim);

                Point3d clickedPos = new Point3d(event.getTarget().getBlockPos().getX(), event.getTarget().getBlockPos().getY(), event.getTarget().getBlockPos().getZ());
                ItemStack  sourceItems = worker.getProperItemStack(worldShim, playerShim, clickedPos);

                if (sourceItems != null && sourceItems.getItem() instanceof ItemBlock) {
                    int numBlocks = Math.min(wand.getMaxBlocks(wandItemstack), playerShim.countItems(sourceItems));

                    LinkedList<Point3d> blocks = worker.getBlockPositionList(clickedPos, event.getTarget().sideHit, numBlocks, wandItem.getMode(event.getPlayer().getActiveItemStack()), wandItem.getFaceLock(event.getPlayer().getActiveItemStack()));
                    if (blocks.size() > 0) {
                        GlStateManager.disableTexture2D();
                        GlStateManager.disableBlend();
                        GlStateManager.depthMask(true);
                        GL11.glLineWidth(2.5F);
                        for (Point3d block : blocks) {
                            Block blockb = Blocks.bedrock;
                            EntityPlayer player = event.getPlayer();
                            double partialTicks = event.getPartialTicks();
                            double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)partialTicks;
                            double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)partialTicks;
                            double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)partialTicks;
                            RenderGlobal.drawOutlinedBoundingBox(blockb.getSelectedBoundingBox(state, worldShim.getWorld(), new BlockPos(block.x, block.y, block.z)).expand(-0.005, -0.005, -0.005).offset(block.x-d0, block.y-d1, block.z-d2), 255, 255, 255, 100);

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
