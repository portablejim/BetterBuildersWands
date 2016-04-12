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
    public KeyBinding keyBindingFluid = new KeyBinding("bbw.key.fluidmode", Keyboard.KEY_F, "bbw.key.category");

    private boolean isPressed;
    private boolean isPressedFluid;

    public KeyEvents() {
        ClientRegistry.registerKeyBinding(keyBinding);
        ClientRegistry.registerKeyBinding(keyBindingFluid);
        FMLCommonHandler.instance().bus().register(this);
        isPressed = false;
        isPressedFluid = false;
    }

    @SubscribeEvent
    public void KeyEvent(InputEvent event) {
        boolean currentIsPressed = keyBinding.getIsKeyPressed();
        boolean currentFluidIsPressed = keyBindingFluid.isPressed();
        if(currentIsPressed != isPressed || currentFluidIsPressed != isPressedFluid) {
            isPressed = currentIsPressed;
            PacketWandActivate packet = new PacketWandActivate(currentIsPressed, currentFluidIsPressed);
            BetterBuildersWandsMod.instance.networkWrapper.sendToServer(packet);
        }

    }


}
