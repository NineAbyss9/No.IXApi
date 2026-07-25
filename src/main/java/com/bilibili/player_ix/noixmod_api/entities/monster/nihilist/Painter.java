
package com.bilibili.player_ix.noixmod_api.entities.monster.nihilist;

import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.SpellcasterNihilist;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class Painter
extends SpellcasterNihilist {
    private final ServerBossEvent bossEvent;
    public Painter(EntityType<? extends Painter> type, Level world) {
        super(type, world);
        this.bossEvent = new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.PROGRESS);
    }

    protected void registerGoals() {
        super.registerGoals();
    }

    public void tick() {
        super.tick();
    }

    @Nullable
    protected SoundEvent getCastingSoundEvent() {
        return null;
    }
}
