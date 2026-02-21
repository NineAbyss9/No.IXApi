
package com.bilibili.player_ix.noixmod_api.client.sound;

import com.bilibili.player_ix.noixmod_api.client.ClientEvents;
import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import com.bilibili.player_ix.noixmod_api.util.ControlledAnimation;
import com.github.NineAbyss9.ix_api.ix_api.api.mobs.Ownable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class BossLoopMusic extends AbstractTickableSoundInstance {
    protected final Mob mobEntity;
    private final float trueVolume;
    private int ticksExisted = 0;
    private int timeUntilFade;
    ControlledAnimation volumeControl;
    protected SoundEvent postBossMusic;

    public BossLoopMusic(SoundEvent soundEvent, Mob mobEntity) {
        this(soundEvent, mobEntity, 1.0F);
    }

    public BossLoopMusic(SoundEvent soundEvent, Mob mobEntity, float volume) {
        this(soundEvent, SoundEvents.EMPTY, mobEntity, volume);
    }

    public BossLoopMusic(SoundEvent soundEvent, SoundEvent postBossMusic, @NotNull Mob mobEntity, float volume) {
        super(soundEvent, SoundSource.RECORDS, SoundInstance.createUnseededRandom());
        this.mobEntity = mobEntity;
        this.postBossMusic = postBossMusic;
        this.x = mobEntity.getX();
        this.y = mobEntity.getY();
        this.z = mobEntity.getZ();
        this.looping = true;
        this.delay = 0;
        this.volumeControl = new ControlledAnimation(40);
        this.volumeControl.setTimer(20);
        this.volume = this.volumeControl.getAnimationFraction();
        this.trueVolume = volume;
        this.timeUntilFade = 80;
    }

    public boolean canPlaySound() {
        return ClientEvents.BOSS_MUSIC == this;
    }

    public void tick() {
        if (!NoixmodAPIMainConfig.PlayBossMusic.get()){
            ClientEvents.BOSS_MUSIC = null;
            this.stop();
        }
        boolean target = this.mobEntity.getTarget() instanceof Player || (this.mobEntity.getTarget() instanceof Ownable
                ownable && ownable.getOwner() instanceof Player);
        if (!target || this.mobEntity.isRemoved()
                || this.mobEntity.isDeadOrDying()
                || !this.mobEntity.isAlive()){
            if (this.mobEntity.isDeadOrDying()){
                this.timeUntilFade = 0;
                if (this.mobEntity.level().isClientSide){
                    Minecraft minecraft = Minecraft.getInstance();
                    SoundManager soundHandler = minecraft.getSoundManager();
                    if (!this.isStopped()){
                        soundHandler.queueTickingSound(new PostBossMusic(this.postBossMusic, this.mobEntity));
                    }
                }
            }
            if (this.timeUntilFade > 0) {
                this.timeUntilFade--;
            } else {
                this.volumeControl.decreaseTimer();
            }
        } else {
            this.volumeControl.increaseTimer();
            this.timeUntilFade = 60;
        }
        this.x = this.mobEntity.getX();
        this.y = this.mobEntity.getY();
        this.z = this.mobEntity.getZ();
        this.volume = this.volumeControl.getAnimationFraction() / this.trueVolume;
        if (this.volumeControl.getAnimationFraction() < 0.025) {
            ClientEvents.BOSS_MUSIC = null;
            this.stop();
        }
        if (this.ticksExisted % 100 == 0) {
            Minecraft.getInstance().getMusicManager().stopPlaying();
        }
        this.ticksExisted++;
    }
}
