package portablejim.bbw.core.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * Created by james on 3/10/15.
 */
public class UnbreakableWand extends Item {
    public UnbreakableWand() {
        super();
        this.setUnlocalizedName("BetterBuildersWands:unbreakableWand");
        this.setTextureName("minecraft:stick");
        this.setCreativeTab(CreativeTabs.tabTools);
    }
}
