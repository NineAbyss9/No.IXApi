
package com.bilibili.player_ix.noixmod_api.server;

import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import com.bilibili.player_ix.noixmod_api.entities.boss.ApostleBoss;
import com.bilibili.player_ix.noixmod_api.network.ApiNetwork;
import com.bilibili.player_ix.noixmod_api.network.packet.BossBarUpdatePacket;
import com.google.common.collect.Sets;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;

import java.util.Set;

public class ApiBossEvent
extends ServerBossEvent {
    protected final Mob mob;
    public final Set<ServerPlayer> serverPlayers = Sets.newHashSet();
    public ApiBossEvent(Mob boss, Component p_8300_, BossBarColor p_8301_, boolean isDark,
                        boolean shouldRenderFog) {
        super(p_8300_, p_8301_, BossBarOverlay.PROGRESS);
        this.mob = boss;
        this.setName(p_8300_);
        this.setVisible(true);
        this.darkenScreen = isDark;
        this.createWorldFog = shouldRenderFog;
    }

     public void update() {
        this.setProgress(this.mob.getHealth() / this.mob.getMaxHealth());
         if (this.mob instanceof ApostleBoss boss && !NoixmodAPIMainConfig.HorrorMode.get()) {
             this.setName(Component.translatable("title.noixmodapi.apostle_" + boss.getTitleNumber())
                     .withStyle(ChatFormatting.DARK_PURPLE));
         }
         for (ServerPlayer player : this.serverPlayers) {
             if (this.mob.hasLineOfSight(player)) {
                 super.addPlayer(player);
             }
         }
     }

    public void addPlayer(ServerPlayer pPlayer) {
        ApiNetwork.sendToClient(pPlayer, new BossBarUpdatePacket(this.getId(), this.mob, false));
        if (this.mob.hasLineOfSight(pPlayer)) {
            super.addPlayer(pPlayer);
        } else {
            this.serverPlayers.add(pPlayer);
        }
    }

    public void removePlayer(ServerPlayer pPlayer) {
        super.removePlayer(pPlayer);
        this.serverPlayers.remove(pPlayer);
        ApiNetwork.sendToClient(pPlayer, new BossBarUpdatePacket(this.getId(), this.mob, true));
    }
}
