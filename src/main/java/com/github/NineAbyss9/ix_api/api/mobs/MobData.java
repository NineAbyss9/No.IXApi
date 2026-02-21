
package com.github.NineAbyss9.ix_api.api.mobs;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class MobData {
    private final Mob mob;
    public MobData(Mob pMob) {
        this.mob = pMob;
    }

    public ClientLevel getClientLevel() {
        return (ClientLevel)mob.level();
    }

    public ServerLevel getServerLevel() {
        return (ServerLevel)mob.level();
    }

    public boolean isInOverworld() {
        return mob.level().dimension() == Level.OVERWORLD;
    }

    public boolean isInNether() {
        return mob.level().dimension() == Level.NETHER;
    }

    public boolean isInEnd() {
        return mob.level().dimension() == Level.END;
    }

    public DifficultyInstance getDifficultyInstance() {
        return mob.level().getCurrentDifficultyAt(mob.blockPosition());
    }

    public Difficulty getDifficulty() {
        return this.getDifficultyInstance().getDifficulty();
    }

    public int getDifficultyId() {
        return this.getDifficulty().getId();
    }
}
