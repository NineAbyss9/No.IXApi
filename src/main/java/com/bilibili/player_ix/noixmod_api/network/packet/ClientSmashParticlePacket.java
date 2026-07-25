
package com.bilibili.player_ix.noixmod_api.network.packet;

import com.bilibili.player_ix.noixmod_api.util.ApiParticleUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientSmashParticlePacket
{
    public final BlockPos pos;
    public final int count;
    public ClientSmashParticlePacket(int pX, int pY, int pZ, int pCount) {
        this.pos = new BlockPos(pX, pY, pZ);
        this.count = pCount;
    }

    public ClientSmashParticlePacket(BlockPos pPos, int pCount) {
        this.pos = pPos;
        this.count = pCount;
    }

    public static void encode(ClientSmashParticlePacket packet, FriendlyByteBuf buffer) {
        buffer.writeInt(packet.pos.getX());
        buffer.writeInt(packet.pos.getY());
        buffer.writeInt(packet.pos.getZ());
        buffer.writeInt(packet.count);
    }

    public static ClientSmashParticlePacket decode(FriendlyByteBuf buffer) {
        return new ClientSmashParticlePacket(buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt());
    }

    public static void handle(ClientSmashParticlePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                Minecraft minecraft = Minecraft.getInstance();
                Level level = minecraft.level;
                if (level == null) return;
                ApiParticleUtil.spawnSmashAttackParticles(level, packet.pos, packet.count);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
