package portablejim.bbw.core;

import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;
import portablejim.bbw.BetterBuildersWandsMod;
import portablejim.bbw.basics.Point3d;
import portablejim.bbw.core.items.IWandItem;
import portablejim.bbw.shims.BasicPlayerShim;

import java.util.ArrayList;

/**
 * /wandOops command.
 */
public class OopsCommand extends CommandBase {
    @Override
    public String getName() {
        return "wandOops";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/wandOops";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] arguments) throws CommandException {
        if(sender instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) sender;
            ItemStack currentItemstack = BasicPlayerShim.getHeldWandIfAny(player);
            if(currentItemstack != null && currentItemstack.getItem() != null
                    && currentItemstack.getItem() instanceof IWandItem) {
                NBTTagCompound tagComponent = currentItemstack.getTagCompound();

                NBTTagCompound bbwCompound;
                if(tagComponent != null && tagComponent.hasKey("bbw", Constants.NBT.TAG_COMPOUND) && tagComponent.getCompoundTag("bbw").hasKey("lastPlaced", Constants.NBT.TAG_INT_ARRAY)) {
                    bbwCompound = tagComponent.getCompoundTag("bbw");
                    ArrayList<Point3d> pointList = unpackNbt(bbwCompound.getIntArray("lastPlaced"));
                    int outputMultiplier = 0;
                    for (Point3d point : pointList) {
                        IBlockState pointState = player.getEntityWorld().getBlockState(new BlockPos(point.x, point.y, point.z));
                        String pointStateString = pointState.toString();
                        if(pointStateString != null && bbwCompound.hasKey("lastBlock")
                                && pointStateString.equals(bbwCompound.getString("lastBlock"))) {
                            player.getEntityWorld().setBlockToAir(new BlockPos(point.x, point.y, point.z));
                            outputMultiplier++;
                        }
                    }
                    if(bbwCompound.hasKey("lastItemBlock", Constants.NBT.TAG_STRING) && bbwCompound.hasKey("lastPerBlock", Constants.NBT.TAG_INT) && bbwCompound.hasKey("lastBlockMeta")) {
                        String itemBlockName = bbwCompound.getString("lastItemBlock");
                        int meta = bbwCompound.getInteger("lastBlockMeta");
                        ItemStack itemStack = GameRegistry.makeItemStack(itemBlockName, meta, 1, "");
                        if(!itemStack.getHasSubtypes()) {
                            // If no subtypes, diffirent meta will mean an invalid block, so remove custom meta.
                            itemStack = GameRegistry.makeItemStack(itemBlockName, 0, 1, "");
                        }
                        int count = bbwCompound.getInteger("lastPerBlock") * outputMultiplier;
                        int stackSize = itemStack.getMaxStackSize();
                        int fullStacks = count / stackSize;
                        for(int i = 0; i < fullStacks; i++) {
                            ItemStack newStack = itemStack.copy();
                            newStack.setCount(stackSize);
                            player.getServerWorld().spawnEntity(new EntityItem(player.getEntityWorld(), player.posX, player.posY, player.posZ, newStack));
                        }
                        ItemStack finalStack = itemStack.copy();
                        finalStack.setCount(count % stackSize);
                        player.getServerWorld().spawnEntity(new EntityItem(player.getEntityWorld(), player.posX, player.posY, player.posZ, finalStack));

                        bbwCompound.removeTag("lastPlaced");
                        bbwCompound.removeTag("lastBlock");
                        bbwCompound.removeTag("lastItemBlock");
                        bbwCompound.removeTag("lastBlockMeta");
                        bbwCompound.removeTag("lastPerBlock");
                    }
                }
                else {
                    throw new WrongUsageException(BetterBuildersWandsMod.LANGID + ".chat.error.noundo");
                }
            }
            else {
                throw new WrongUsageException(BetterBuildersWandsMod.LANGID + ".chat.error.nowand");
            }
        }
        else {
            throw new WrongUsageException(BetterBuildersWandsMod.LANGID + ".chat.error.bot");
        }
    }

    protected ArrayList<Point3d> unpackNbt(int[] placedBlocks) {
        ArrayList<Point3d> output = new ArrayList<Point3d>();
        int countPoints = placedBlocks.length / 3;
        for(int i = 0; i < countPoints * 3; i += 3) {
            output.add(new Point3d(placedBlocks[i], placedBlocks[i+1], placedBlocks[i+2]));
        }

        return output;
    }


    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }
}
