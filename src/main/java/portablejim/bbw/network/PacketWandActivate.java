/* This file is part of VeinMiner.
 *
 *    VeinMiner is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Lesser General Public License as
 *    published by the Free Software Foundation, either version 3 of
 *     the License, or (at your option) any later version.
 *
 *    VeinMiner is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public
 *    License along with VeinMiner.
 *    If not, see <http://www.gnu.org/licenses/>.
 */

package portablejim.bbw.network;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import portablejim.bbw.BetterBuildersWandsMod;
import portablejim.bbw.core.items.IWandItem;
import portablejim.bbw.shims.BasicPlayerShim;

import java.util.UUID;

/**
 * Packet the client sends to the server to tell it to activate or deactivate
 * for the player that sent it.
 */

public class PacketWandActivate implements IMessage {
    public boolean keyActive;
    public boolean keyFluidActive;

    @SuppressWarnings("UnusedDeclaration")
    public PacketWandActivate() {}

    public PacketWandActivate(boolean keyActive, boolean keyFluidActive) {
        this.keyActive = keyActive;
        this.keyFluidActive = keyFluidActive;
    }

    @Override
    public void toBytes(ByteBuf buffer) {
        buffer.writeBoolean(keyActive);
        buffer.writeBoolean(keyFluidActive);
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        keyActive = buffer.readBoolean();
        keyFluidActive = buffer.readBoolean();
    }

    public static class Handler extends GenericHandler<PacketWandActivate> {
        @Override
        public void processMessage(PacketWandActivate packetWandActivate, MessageContext context) {
            EntityPlayerMP player = context.getServerHandler().playerEntity;
            UUID playerName = player.getUniqueID();

           ItemStack wand = BasicPlayerShim.getHeldWandIfAny(player);
            if(packetWandActivate.keyActive && wand != null && wand.getItem() instanceof IWandItem) {
                IWandItem wandItem = (IWandItem) wand.getItem();
                wandItem.nextMode(wand, player);
                player.sendMessage(new TextComponentTranslation(BetterBuildersWandsMod.LANGID + ".chat.mode." + wandItem.getMode(wand).toString().toLowerCase()));
            }
            if(packetWandActivate.keyFluidActive && wand != null && wand.getItem() != null
                    && wand.getItem() instanceof IWandItem) {
                ItemStack wandItemstack = wand;
                IWandItem wandItem = (IWandItem) wandItemstack.getItem();
                wandItem.nextFluidMode(wandItemstack, player);
                player.sendMessage(new TextComponentTranslation(BetterBuildersWandsMod.LANGID + ".chat.fluidmode." + wandItem.getMode(wand).toString().toLowerCase()));
            }
        }
    }
}
