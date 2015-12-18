package portablejim.bbw.core.conversion;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import portablejim.bbw.BetterBuildersWandsMod;
import portablejim.bbw.basics.Point3d;
import portablejim.bbw.shims.IWorldShim;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created by james on 17/12/15.
 */
public class StackedBlockManager {
    private HashMap<String, ItemStack> cache;
    Method getStackedBlockMethod;

    public StackedBlockManager() {
        cache = new HashMap<String, ItemStack>();
        try {
            getStackedBlockMethod = Block.class.getDeclaredMethod("createStackedBlock", int.class);
            getStackedBlockMethod.setAccessible(true);
        }
        catch (NoSuchMethodException e) {
            BetterBuildersWandsMod.logger.error("No Method Block.getStackedBlock(int)!");
        }
    }

    public ItemStack getStackedBlock(IWorldShim world, Point3d blockPos) {
        Block block = world.getBlock(blockPos);
        int meta = world.getMetadata(blockPos);
        String blockName = Block.blockRegistry.getNameForObject(block);
        String blockIdentifier = String.format("%s|%d", blockName, meta);

        if(!cache.containsKey(blockIdentifier)) {
            cache.put(blockIdentifier, callGetStackedBlock(block, meta));
        }
        return cache.get(blockIdentifier);
    }

    private ItemStack callGetStackedBlock(Block block, int meta) {
        try {
            Class<?> clazz = block.getClass();
            while(clazz != null) {
                Method[] methods = clazz.getDeclaredMethods();
                for(Method method : methods) {
                    if(method.getName().equals("createStackedBlock")) {
                        getStackedBlockMethod = method;
                        break;
                    }
                }
                clazz = clazz.getSuperclass();
            }
            if(getStackedBlockMethod != null) {
                getStackedBlockMethod.setAccessible(true);
                return (ItemStack) getStackedBlockMethod.invoke(block, meta);
            }
        }
        catch (IllegalAccessException e) {
            BetterBuildersWandsMod.logger.error("getStackedBlockMethod Illegal to call");
        }
        catch (InvocationTargetException e) {
            BetterBuildersWandsMod.logger.error("getStackedBlockMethod Invoker error");
        }
        return null;
    }
}
