package portablejim.bbw.core.conversion;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import portablejim.bbw.BetterBuildersWandsMod;
import portablejim.bbw.basics.Point3d;
import portablejim.bbw.shims.IWorldShim;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Manage calling getStackedBlock(int) using reflection.
 */
public class StackedBlockManager {
    private HashMap<String, ItemStack> cache;
    Method getStackedBlockMethod;
    String getStackedBlockMethodName;

    public StackedBlockManager() {
        cache = new HashMap<String, ItemStack>();
        try {
            getStackedBlockMethodName = "func_180643_i";
            getStackedBlockMethod = Block.class.getDeclaredMethod(getStackedBlockMethodName, IBlockState.class);
            if(getStackedBlockMethod == null) {
                getStackedBlockMethodName = "createStackedBlock";
                getStackedBlockMethod = Block.class.getDeclaredMethod(getStackedBlockMethodName, IBlockState.class);
            }
            getStackedBlockMethod.setAccessible(true);
            BetterBuildersWandsMod.logger.info("Access transform success createStackedBlock (" + getStackedBlockMethodName + ").");
        }
        catch (NoSuchMethodException e) {
            BetterBuildersWandsMod.logger.error("No Method Block.getStackedBlock(int)!");
        }
    }

    public ItemStack getStackedBlock(IWorldShim world, Point3d blockPos) {
        Block block = world.getBlock(blockPos);
        IBlockState state = world.getWorld().getBlockState(blockPos.toBlockPos());
        String blockName = Block.blockRegistry.getNameForObject(block).toString();
        String blockIdentifier = String.format("%s|%s", blockName, state.toString());

        if(!cache.containsKey(blockIdentifier)) {
            cache.put(blockIdentifier, callGetStackedBlock(block, state));
        }
        return cache.get(blockIdentifier);
    }

    private ItemStack callGetStackedBlock(Block block, IBlockState state) {
        try {
            Class<?> clazz = block.getClass();
            while(clazz != null) {
                Method[] methods = clazz.getDeclaredMethods();
                for(Method method : methods) {
                    if(method.getName().equals(getStackedBlockMethodName) || method.getName().equals("createStackedBlock")) {
                        getStackedBlockMethod = method;
                        break;
                    }
                }
                clazz = clazz.getSuperclass();
            }
            if(getStackedBlockMethod != null) {
                getStackedBlockMethod.setAccessible(true);
                return (ItemStack) getStackedBlockMethod.invoke(block, state);
            }
        }
        catch (IllegalAccessException e) {
            BetterBuildersWandsMod.logger.error("getStackedBlockMethod Illegal to call");
        }
        catch (InvocationTargetException e) {
            BetterBuildersWandsMod.logger.error("getStackedBlockMethod Invoker error");
        }
        catch (NoClassDefFoundError e) {
            BetterBuildersWandsMod.logger.error("No such method: " + e.getLocalizedMessage());
        }
        return null;
    }
}
