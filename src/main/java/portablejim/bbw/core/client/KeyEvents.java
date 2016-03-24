package portablejim.bbw.core.client;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
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
        MinecraftForge.EVENT_BUS.register(this);
        isPressed = false;
    }

    @SubscribeEvent
    public void KeyEvent(InputEvent event) {
        boolean currentIsPressed = keyBinding.isPressed();
        if(currentIsPressed != isPressed) {
            isPressed = currentIsPressed;
            PacketWandActivate packet = new PacketWandActivate(currentIsPressed);
            BetterBuildersWandsMod.instance.networkWrapper.sendToServer(packet);
        }

    }


}
