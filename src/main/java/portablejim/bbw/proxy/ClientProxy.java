package portablejim.bbw.proxy;

import portablejim.bbw.core.client.KeyEvents;

/**
 * Created by james on 16/10/15.
 */
public class ClientProxy extends CommonProxy {
    private KeyEvents keyevents;

    @Override
    public void RegisterEvents() {
        keyevents = new KeyEvents();

    }
}
