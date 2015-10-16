package portablejim.bbw.core.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import portablejim.bbw.BetterBuildersWandsMod;
import portablejim.bbw.network.PacketWandActivate;

/**
 * Created by james on 16/10/15.
 */
public class KeyEvents {
    public KeyBinding keyBinding = new KeyBinding("bbw.key.mode", Keyboard.KEY_M, "bbw.key.category");

    private boolean isPressed;

    public KeyEvents() {
        ClientRegistry.registerKeyBinding(keyBinding);
        FMLCommonHandler.instance().bus().register(this);
        isPressed = false;
    }

    @SubscribeEvent
    public void KeyEvent(InputEvent event) {
        boolean currentIsPressed = keyBinding.getIsKeyPressed();
        if(currentIsPressed != isPressed) {
            isPressed = currentIsPressed;
            PacketWandActivate packet = new PacketWandActivate(currentIsPressed);
            BetterBuildersWandsMod.instance.networkWrapper.sendToServer(packet);
        }

    }


}
