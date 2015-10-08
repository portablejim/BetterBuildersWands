package portablejim.bbw.core.items;

/**
 * The actual item: Simple wand with no durability. Similar to current Builder's Wand.
 */
public class ItemUnbreakableWand extends ItemBasicWand{
    public ItemUnbreakableWand() {
        super();
        this.setUnlocalizedName("betterbuilderswands:unbreakableWand");
        this.setTextureName("betterbuilderswands:wandUnbreakable");
    }
}
