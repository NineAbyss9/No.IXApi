
package com.bilibili.player_ix.noixmod_api.server;

import com.github.NineAbyss9.ix_api.api.mobs.OwnableData;
import com.bilibili.player_ix.noixmod_api.register.ApiAgent;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class ServerAgent
implements ApiAgent {
    public ServerAgent() {
    }

    @Override
    public Player getPlayerInstance() {
        return null;
    }

    @Override
    public void addBossBar(UUID id, Mob mob) {}

    @Override
    public void removeBossBar(UUID id, Mob mob) {}

    public OwnableData getOwnableData() {
        return null;
    }
}
