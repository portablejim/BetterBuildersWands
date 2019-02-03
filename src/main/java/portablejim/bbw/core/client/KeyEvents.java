package portablejim.bbw.core.client;

import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
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
    public KeyBinding keyBindingFluid = new KeyBinding("bbw.key.fluidmode", KeyConflictContext.IN_GAME, KeyModifier.ALT, Keyboard.KEY_F, "bbw.key.category");

    private boolean isPressed;
    private boolean isPressedFluid;

    public KeyEvents() {
        ClientRegistry.registerKeyBinding(keyBinding);
        ClientRegistry.registerKeyBinding(keyBindingFluid);
        MinecraftForge.EVENT_BUS.register(this);
        isPressed = false;
        isPressedFluid = false;
    }

    @SubscribeEvent
    public void KeyEvent(InputEvent event) {
        boolean currentIsPressed = keyBinding.isPressed();
        boolean currentFluidIsPressed = keyBindingFluid.isPressed();
        if(currentIsPressed != isPressed || currentFluidIsPressed != isPressedFluid) {
            isPressed = currentIsPressed;
            PacketWandActivate packet = new PacketWandActivate(currentIsPressed, currentFluidIsPressed);
            BetterBuildersWandsMod.instance.networkWrapper.sendToServer(packet);
        }

    }


}
