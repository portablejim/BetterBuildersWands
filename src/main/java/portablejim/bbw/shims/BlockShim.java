package portablejim.bbw.shims;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import portablejim.bbw.basics.Point3d;

/**
 * Created by james on 16/12/16.
 */
public class BlockShim {
    public static ItemStack getPickBlock(Block block, IWorldShim world, IPlayerShim player, Point3d pos) {
        return block.getPickBlock(player.getPlayer().rayTrace(player.getReach(), 1F), world.getWorld(),
                pos.x, pos.y, pos.z,
                player.getPlayer());
    }
}
