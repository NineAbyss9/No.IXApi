
package com.bilibili.player_ix.noixmod_api.network.packet;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class BossBarUpdatePacket {
    private final UUID uuid;
    private final int boss;
    private final boolean remove;

    public BossBarUpdatePacket(UUID uuid, int boss, boolean remove) {
        this.uuid = uuid;
        this.boss = boss;
        this.remove = remove;
    }

    public BossBarUpdatePacket(UUID pId, Mob boss, boolean remove) {
        this.uuid = pId;
        this.boss = boss.getId();
        this.remove = remove;
    }

    public static void encode(BossBarUpdatePacket packet, FriendlyByteBuf buffer) {
        buffer.writeUUID(packet.uuid);
        buffer.writeInt(packet.boss);
        buffer.writeBoolean(packet.remove);
    }

    public static BossBarUpdatePacket decode(FriendlyByteBuf buffer) {
        return new BossBarUpdatePacket(buffer.readUUID(), buffer.readInt(), buffer.readBoolean());
    }

    public static void handle(BossBarUpdatePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player player;
            if (ctx.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                player = NoixmodAPI.agent.getPlayerInstance();
            } else {
                player = ctx.get().getSender();
            }
            if (player != null) {
                Entity entity = player.level().getEntity(packet.boss);
                if (entity instanceof Mob mob) {
                    if (packet.remove) {
                        NoixmodAPI.agent.removeBossBar(packet.uuid, mob);
                    } else {
                        NoixmodAPI.agent.addBossBar(packet.uuid, mob);
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
