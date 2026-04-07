
package com.bilibili.player_ix.noixmod_api.client;

import com.bilibili.player_ix.noixmod_api.world.HorrorModeManager;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.bilibili.player_ix.noixmod_api.client.sound.BossLoopMusic;
import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import com.bilibili.player_ix.noixmod_api.entities.boss.ApostleBoss;
import com.bilibili.player_ix.noixmod_api.entities.boss.EvokerIllager;
import com.bilibili.player_ix.noixmod_api.entities.projectile.HorrorCamera;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPISounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.*;

@Mod.EventBusSubscriber(modid = NoixmodAPI.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {
    private ClientEvents() {
    }

    @SubscribeEvent
    public static void apostleFog(ViewportEvent.ComputeFogColor color) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player != null) {
            ApostleBoss apostle = ClientEvents.findApostle(player);
            HorrorCamera camera = ClientEvents.findCamera(player);
            if (apostle != null) {
                color.setRed(0.05F);
                color.setBlue(0);
                color.setGreen(0);
            } else if (camera != null) {
                color.setGreen(0);
                color.setBlue(0);
                color.setRed(0.05F);
            } else if (NoixmodAPIMainConfig.DARK_SKY.get()) {
                color.setRed(0.05f);
                color.setGreen(0);
                color.setBlue(0);
            }
        }
    }

    public static void onWorldTick(TickEvent.LevelTickEvent event) {
        Level level = event.level;
        if (NoixmodAPIMainConfig.HorrorMode.get() && level.isClientSide) {
            HorrorModeManager manager = HorrorModeManager.horrorModeManagers.get(level);
            if (manager == null) return;
            manager.tick();
        }
    }

    //From Polarice's codes
    public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        Minecraft minecraft = Minecraft.getInstance();
        if (event.getLevel() instanceof ClientLevel) {
            if (entity instanceof Mob mob && !mob.isNoAi()) {
                if (entity instanceof ApostleBoss) {
                    minecraft.getMusicManager().stopPlaying();
                }
                if (entity instanceof EvokerIllager) {
                    minecraft.getMusicManager().stopPlaying();
                }
            }
        }
    }

    public static void onEntityTick(LivingEvent.LivingTickEvent event) {
        Entity entity = event.getEntity();
        if (entity.level() instanceof ClientLevel) {
            if (entity instanceof ApostleBoss apostle && !apostle.isNoAi()) {
                SoundEvent soundEvent = NoixmodAPISounds.APOSTLE_MUSIC.get();
                playBossMusic(soundEvent, apostle);
            }
            if (entity instanceof EvokerIllager illager && !illager.isNoAi()) {
                SoundEvent soundEvent = NoixmodAPISounds.EI_MUSIC.get();
                playBossMusic(soundEvent, illager);
            }
        }
    }

    public static AbstractTickableSoundInstance BOSS_MUSIC;

    public static void playBossMusic(SoundEvent soundEvent, Mob mob) {
        playBossMusic(soundEvent, SoundEvents.EMPTY, mob);
    }

    public static void playBossMusic(SoundEvent soundEvent, SoundEvent post, Mob mob){
        playBossMusic(soundEvent, post, mob, 1.0F);
    }

    public static void playBossMusic(@Nullable SoundEvent soundEvent, SoundEvent post, Mob mob, float volume) {
        if (NoixmodAPIMainConfig.PlayBossMusic.get()) {
            Minecraft minecraft = Minecraft.getInstance();
            if (soundEvent != null && mob.isAlive()) {
                if (BOSS_MUSIC == null) {
                    BOSS_MUSIC = new BossLoopMusic(soundEvent, post, mob, volume);
                }
            } else {
                BOSS_MUSIC = null;
            }
            if (BOSS_MUSIC != null && !minecraft.getSoundManager().isActive(BOSS_MUSIC)) {
                Minecraft.getInstance().getSoundManager().play(BOSS_MUSIC);
            }
        }
    }
    //To here

    @Nullable
    public static ApostleBoss findApostle(Entity entity) {
        List<ApostleBoss> list = entity.level().getEntitiesOfClass(ApostleBoss.class, entity.getBoundingBox()
                .inflate(128));
        ApostleBoss boss = null;
        for (ApostleBoss apostle : list) {
            if (apostle != null) {
                boss = apostle;
            }
        }
        return boss;
    }

    @Nullable
    public static HorrorCamera findCamera(Entity entity) {
        List<HorrorCamera> cameras = entity.level().getEntitiesOfClass(HorrorCamera.class, entity.getBoundingBox()
                .inflate(66));
        HorrorCamera camera = null;
        for (HorrorCamera horrorCamera : cameras) {
            if (horrorCamera != null) {
                camera = horrorCamera;
            }
        }
        return camera;
    }
}
