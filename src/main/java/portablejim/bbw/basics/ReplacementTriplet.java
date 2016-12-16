package portablejim.bbw.basics;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

/**
 * Created by james on 16/12/16.
 */
public class ReplacementTriplet {
    public IBlockState source, target;
    public ItemStack items;

    public ReplacementTriplet(IBlockState source, ItemStack items, IBlockState target) {
        this.source = source;
        this.target = target;
        this.items = items;
    }
}

