
package com.bilibili.player_ix.noixmod_api.client;

import com.github.NineAbyss9.ix_api.ix_api.api.mobs.OwnableData;
import com.bilibili.player_ix.noixmod_api.register.ApiAgent;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class ClientAgent
implements ApiAgent {
    public static OwnableData ownableDataInstance;
    public ClientAgent() {
    }

    @Override
    public Player getPlayerInstance() {
        return Minecraft.getInstance().player;
    }

    public OwnableData getOwnableData() {
        return ownableDataInstance;
    }

    @Override
    public void addBossBar(UUID id, Mob mob) {
        BossBar.addBossBar(id, mob);
    }

    @Override
    public void removeBossBar(UUID id, Mob mob) {
        BossBar.removeBossBar(id, mob);
    }
}
