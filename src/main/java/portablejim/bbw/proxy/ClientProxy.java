package portablejim.bbw.proxy;

import net.minecraftforge.common.MinecraftForge;
import portablejim.bbw.core.BlockEvents;
import portablejim.bbw.core.client.KeyEvents;

/**
 * Created by james on 16/10/15.
 */
public class ClientProxy extends CommonProxy {
    private KeyEvents keyevents;
    private BlockEvents blockEvents;

    @Override
    public void RegisterEvents() {
        keyevents = new KeyEvents();
        blockEvents = new BlockEvents();
        MinecraftForge.EVENT_BUS.register(blockEvents);

    }
}
