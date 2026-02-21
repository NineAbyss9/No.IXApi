
package com.bilibili.player_ix.noixmod_api.register;

import com.github.NineAbyss9.ix_api.api.mobs.OwnableData;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public interface ApiAgent {
    Player getPlayerInstance();

    OwnableData getOwnableData();

    void addBossBar(UUID id, Mob mob);

    void removeBossBar(UUID id, Mob mob);
}
