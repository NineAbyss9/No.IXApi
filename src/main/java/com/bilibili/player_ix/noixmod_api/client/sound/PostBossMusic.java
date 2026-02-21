
package com.bilibili.player_ix.noixmod_api.client.sound;

import com.bilibili.player_ix.noixmod_api.client.ClientEvents;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.NotNull;

public class PostBossMusic extends AbstractTickableSoundInstance {
    protected final Mob mobEntity;

    public PostBossMusic(SoundEvent soundEvent, @NotNull Mob mobEntity) {
        super(soundEvent, SoundSource.RECORDS, SoundInstance.createUnseededRandom());
        this.mobEntity = mobEntity;
        this.x = mobEntity.getX();
        this.y = mobEntity.getY();
        this.z = mobEntity.getZ();
        this.looping = false;
        this.delay = 0;
        this.volume = 1.0F;
    }

    @Override
    public boolean canPlaySound() {
        return ClientEvents.BOSS_MUSIC == null;
    }

    @Override
    public void tick() {
    }
}
