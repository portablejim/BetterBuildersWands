package portablejim.bbw.core;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;

/**
 * Events for supporting wands.
 */
public class BlockEvents {
    @SubscribeEvent
    public void blockHighlightEvent(DrawBlockHighlightEvent event) {
        //FMLLog.info("Happened!" + event.target.toString());
    }
}
