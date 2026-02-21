
package com.bilibili.player_ix.noixmod_api.network.packet;

import com.github.NineAbyss9.ix_api.ix_api.api.mobs.OwnableData;
import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.bilibili.player_ix.noixmod_api.client.ClientAgent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class OwnableDataNextFlagPacket {
    private final int id;
    private final OwnableData ownableData;
    public OwnableDataNextFlagPacket(int pId, OwnableData data) {
        this.id = pId;
        ownableData = data;
    }

    public static void encode(OwnableDataNextFlagPacket packet, FriendlyByteBuf buffer) {
        buffer.writeInt(packet.id);
    }

    public static OwnableDataNextFlagPacket decode(FriendlyByteBuf buffer) {
        return new OwnableDataNextFlagPacket(buffer.readInt(), ClientAgent.ownableDataInstance);
    }

    public static void handle(OwnableDataNextFlagPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            OwnableData data = packet.ownableData;
            if (ctx.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                data = NoixmodAPI.agent.getOwnableData();
            }
            data.nextFlag();
        });
        ctx.get().setPacketHandled(true);
    }
}
