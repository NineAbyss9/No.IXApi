
package com.bilibili.player_ix.noixmod_api.entities.boss.hermit;

import com.github.NineAbyss9.ix_api.ix_api.api.mobs.ApiNihilisticBoss;
import com.bilibili.player_ix.noixmod_api.api.entity.IX;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.SpellcasterNihilist;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class Hermit
extends SpellcasterNihilist
implements IX, ApiNihilisticBoss {
    private static final EntityDataAccessor<Integer> DATA_FLAGS;
    public Hermit(EntityType<Hermit> type, Level world) {
        super(type, world);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS, 0);
    }

    protected SoundEvent getCastingSoundEvent() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    public Entity ixSelf() {
        return this;
    }

    static {
        DATA_FLAGS = SynchedEntityData.defineId(Hermit.class, EntityDataSerializers.INT);
    }
}
