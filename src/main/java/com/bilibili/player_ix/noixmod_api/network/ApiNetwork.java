
package com.bilibili.player_ix.noixmod_api.network;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.bilibili.player_ix.noixmod_api.network.packet.BossBarUpdatePacket;
import com.bilibili.player_ix.noixmod_api.network.packet.ClientSmashParticlePacket;
import com.bilibili.player_ix.noixmod_api.network.packet.InfernalAnvilRenameItemPacket;
import com.google.common.base.Predicates;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ApiNetwork {
    private static final String PROTOCOL = "noixapi_packet";
    public static SimpleChannel INSTANCE;
    private static int id = 0;

    public static void register() {
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(NoixmodAPI.MOD_ID, "main"),
                () -> PROTOCOL, Predicates.alwaysTrue(), Predicates.alwaysTrue()
        );
        INSTANCE.registerMessage(nextId(), BossBarUpdatePacket.class, BossBarUpdatePacket::encode,
                BossBarUpdatePacket::decode, BossBarUpdatePacket::handle
        );
        INSTANCE.registerMessage(nextId(), InfernalAnvilRenameItemPacket.class, InfernalAnvilRenameItemPacket::encode,
                InfernalAnvilRenameItemPacket::decode, InfernalAnvilRenameItemPacket::handle);
        INSTANCE.registerMessage(nextId(), ClientSmashParticlePacket.class, ClientSmashParticlePacket::encode,
                ClientSmashParticlePacket::decode, ClientSmashParticlePacket::handle);
    }

    private static int nextId() {
        return id++;
    }

    public static <M> void sendToClient(ServerPlayer player, M msg) {
        INSTANCE.sendTo(msg, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static <M> void sendToServer(M message) {
        INSTANCE.sendToServer(message);
    }
}
