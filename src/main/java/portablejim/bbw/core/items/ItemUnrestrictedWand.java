package portablejim.bbw.core.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import portablejim.bbw.basics.EnumFluidLock;
import portablejim.bbw.core.wands.IWand;
import portablejim.bbw.basics.EnumLock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The actual item: Simple wand with no durability. Similar to current Builder's Wand.
 */
public class ItemUnrestrictedWand extends ItemBasicWand{

    protected Set<Integer> subItemMetas = new HashSet<Integer>();

    public ItemUnrestrictedWand(IWand wand, String name, String texture) {
        super();
        this.setUnlocalizedName("betterbuilderswands.wand" + name);
        //this.setTextureName("betterbuilderswands:wand" + texture);
        this.wand = wand;
    }

    public EnumLock getFaceLock(ItemStack itemStack) {
        if(getMode(itemStack) == EnumLock.HORIZONTAL) {
            return EnumLock.HORIZONTAL;
        }
        return EnumLock.NOLOCK;
    }

    @Override
    public void nextMode(ItemStack itemStack, EntityPlayer player) {
        switch(getMode(itemStack)) {

            case NORTHSOUTH:
                setMode(itemStack, EnumLock.EASTWEST);
                break;
            case VERTICAL:
                setMode(itemStack, EnumLock.NORTHSOUTH);
                break;
            case VERTICALEASTWEST:
                setMode(itemStack, EnumLock.NOLOCK);
                break;
            case EASTWEST:
                setMode(itemStack, EnumLock.VERTICALNORTHSOUTH);
                break;
            case HORIZONTAL:
                setMode(itemStack, EnumLock.VERTICAL);
                break;
            case VERTICALNORTHSOUTH:
                setMode(itemStack, EnumLock.VERTICALEASTWEST);
                break;
            case NOLOCK:
                setMode(itemStack, EnumLock.HORIZONTAL);
                break;
        }
    }

    @Override
    public void nextFluidMode(ItemStack itemStack, EntityPlayer player) {
        switch(getFluidMode(itemStack)) {
            case STOPAT:
                setFluidMode(itemStack, EnumFluidLock.IGNORE);
                break;
            case IGNORE:
                setFluidMode(itemStack, EnumFluidLock.STOPAT);
                break;
        }
    }

    public ItemUnrestrictedWand addSubMeta(int meta) {
        this.subItemMetas.add(meta);
        return this;
    }

    @SuppressWarnings("unchecked")
    public void getSubItems(Item item, CreativeTabs creativeTabs, List list) {
        if(subItemMetas.isEmpty()) {
            list.add(new ItemStack(item, 1, 0));
        }
        else {
            ArrayList<Integer> metas = new ArrayList<Integer>(this.subItemMetas);
            Collections.sort(metas);
            for (Integer meta : metas) {
                list.add(new ItemStack(item, 1, meta));

            }
        }
    }

}
