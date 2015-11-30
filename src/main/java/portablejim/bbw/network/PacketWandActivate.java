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

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import portablejim.bbw.BetterBuildersWandsMod;
import portablejim.bbw.core.items.IWandItem;

import java.util.UUID;

/**
 * Packet the client sends to the server to tell it to activate or deactivate
 * for the player that sent it.
 */

public class PacketWandActivate implements IMessage {
    public boolean keyActive;

    @SuppressWarnings("UnusedDeclaration")
    public PacketWandActivate() {}

    public PacketWandActivate(boolean keyActive) {
        this.keyActive = keyActive;
    }

    @Override
    public void toBytes(ByteBuf buffer) {
        buffer.writeBoolean(keyActive);
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        keyActive = buffer.readBoolean();
    }

    public static class Handler extends GenericHandler<PacketWandActivate> {
        @Override
        public void processMessage(PacketWandActivate packetWandActivate, MessageContext context) {
            EntityPlayerMP player = context.getServerHandler().playerEntity;
            UUID playerName = player.getUniqueID();


            if(packetWandActivate.keyActive && player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() != null
                    && player.getCurrentEquippedItem().getItem() instanceof IWandItem) {
                ItemStack wandItemstack = player.getCurrentEquippedItem();
                IWandItem wandItem = (IWandItem) wandItemstack.getItem();
                wandItem.nextMode(wandItemstack, player);
                player.addChatMessage(new ChatComponentTranslation(BetterBuildersWandsMod.LANGID + ".chat.mode." + wandItem.getMode(wandItemstack).toString().toLowerCase()));
            }
        }
    }
}
