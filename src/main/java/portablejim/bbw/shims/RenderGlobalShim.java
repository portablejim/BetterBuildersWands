package portablejim.bbw.shims;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.math.AxisAlignedBB;

import java.lang.reflect.Method;

/**
 * Created by james on 25/06/16.
 */
public class RenderGlobalShim {

    public static void drawOutlinedBox(AxisAlignedBB box, int r, int g, int b, int a) {
        RenderGlobal.drawOutlinedBoundingBox(box, r, g, b, a);
    }
}
