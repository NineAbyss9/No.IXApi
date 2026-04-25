
package com.bilibili.player_ix.noixmod_api.client;

import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.AbstractHorrorMob;
import com.bilibili.player_ix.noixmod_api.world.HorrorModeManager;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
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
import org.NineAbyss9.math.MathSupport;
import org.joml.Matrix4f;

import javax.annotation.Nullable;
import java.util.*;

@Mod.EventBusSubscriber(modid = NoixmodAPI.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {
    private ClientEvents() {
    }

    @SubscribeEvent
    public static void setupFogColor(ViewportEvent.ComputeFogColor color) {
        if (NoixmodAPIMainConfig.TERRIBLE_SKY.get()) {
            color.setRed(FogHandler.currentRed);
            color.setGreen(FogHandler.currentGreen);
            color.setBlue(FogHandler.currentBlue);
            return;
        }
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            ApostleBoss apostle = ClientEvents.findApostle(player);
            HorrorCamera camera = ClientEvents.findCamera(player);
             if (apostle != null) {
                color.setRed(0.1F);
                color.setBlue(0);
                color.setGreen(0);
            } else if (camera != null) {
                color.setGreen(0);
                color.setBlue(0);
                color.setRed(0.05F);
            }
        }
    }

    @SubscribeEvent
    public static void renderFog(ViewportEvent.RenderFog event) {
        if (!NoixmodAPIMainConfig.TERRIBLE_SKY.get() || findApostle(Minecraft.getInstance().player) != null) return;
        event.setNearPlaneDistance(FogHandler.limitFogDis);  // 开始起雾
        event.setFarPlaneDistance(FogHandler.maxFogDis);   // 完全看不清
        event.setCanceled(true);//Disable the Vanilla logic
    }

    public static final ResourceLocation SUN_LOCATION = new ResourceLocation(NoixmodAPI.MOD_ID, "textures/environment/sun.png");

    //private static boolean renderStrangeLight = false;

    @SubscribeEvent
    public static void renderTheHorror(RenderLevelStageEvent event) {
        if (!NoixmodAPIMainConfig.TERRIBLE_SKY.get()) return;
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null || event.getStage() != RenderLevelStageEvent.Stage.AFTER_SKY) return;
        PoseStack pPoseStack = event.getPoseStack();
        float pPartialTick = event.getPartialTick();
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        RenderSystem.enableBlend();
        //The Horror
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE,
                GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        pPoseStack.pushPose();
        float f11 = 1.0F - level.getRainLevel(pPartialTick);
        RenderSystem.setShaderColor(2.0F, 1.0F, 1.0F, f11);
        pPoseStack.mulPose(Axis.YP.rotationDegrees(-180.0F));
        pPoseStack.mulPose(Axis.XP.rotationDegrees(level.getTimeOfDay(pPartialTick) * 360.0F));
        Matrix4f matrix4f1 = pPoseStack.last().pose();
        float f12 = 20.0F;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, SUN_LOCATION);
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(matrix4f1, -f12, 100.0F, -f12).uv(0.0F, 0.0F).endVertex();
        bufferbuilder.vertex(matrix4f1, f12, 100.0F, -f12).uv(1.0F, 0.0F).endVertex();
        bufferbuilder.vertex(matrix4f1, f12, 100.0F, f12).uv(1.0F, 1.0F).endVertex();
        bufferbuilder.vertex(matrix4f1, -f12, 100.0F, f12).uv(0.0F, 1.0F).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
        pPoseStack.popPose();
    }

    public static final List<SoundEvent> randomAmbientSounds
            = List.of(SoundEvents.ZOMBIE_AMBIENT, SoundEvents.SKELETON_AMBIENT);

    public static final List<SoundEvent> terribleSounds = List.of(SoundEvents.CREEPER_PRIMED);

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (!NoixmodAPIMainConfig.TERRIBLE_SKY.get()) return;
        var player = Minecraft.getInstance().player;
        var level = Minecraft.getInstance().level;
        if (player == null || level == null) return;
        FogHandler.setFogDynamically(level, player);
    }

    @SubscribeEvent
    public static void onClientPlayerTick(TickEvent.PlayerTickEvent event) {
        if (HorrorModeManager.horrorModeEnabled()) {
            Player player = event.player;
            Level level = player.level();
            BlockPos pos = player.blockPosition();
            if (level.canSeeSky(pos)) {
                if (level.getGameTime() % 600L == 0L) {
                    if (MathSupport.threadSafeRandom.nextFloat() < 0.005F) {
                        level.playLocalSound(pos, level.getBlockState(pos.below()).getSoundType().getStepSound(), SoundSource.HOSTILE,
                                0.5F, 1.0F, false);
                    }
                }
            } else {
                if (level.getGameTime() % 300L == 0L) {
                    if (MathSupport.threadSafeRandom.nextFloat() < 0.008F) {
                        level.playLocalSound(pos, level.getBlockState(pos.below()).getSoundType().getStepSound(), SoundSource.HOSTILE,
                                0.5F, 1.0F, false);
                    }
                    if (MathSupport.threadSafeRandom.nextFloat() < 0.01F) {
                        if (MathSupport.threadSafeRandom.nextBoolean()) {
                            level.playLocalSound(pos, randomAmbientSounds.get(MathSupport.threadSafeRandom.nextInt(
                                    randomAmbientSounds.size())), SoundSource.HOSTILE, 0.25F, 1.0F, false);
                        } else {
                            level.playLocalSound(pos, terribleSounds.get(MathSupport.threadSafeRandom.nextInt(
                                    terribleSounds.size())), SoundSource.HOSTILE, 0.75F, 1.0F, false);
                        }
                    }
                    if (MathSupport.threadSafeRandom.nextFloat() < 0.02F) {
                        level.playLocalSound(pos, SoundEvents.AMBIENT_CAVE.value(), SoundSource.NEUTRAL,
                                0.5F, 1.0F, false);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onRenderLiving(RenderLivingEvent.Pre<AbstractHorrorMob, ?> event) {
        if (!HorrorModeManager.horrorModeEnabled()) return;
        var entity = event.getEntity();
        if (!(entity instanceof AbstractHorrorMob)) return;
        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        // 距离检查
        if (player.distanceTo(entity) > 24D) return;
        // 射线检测
        Level level = entity.level();
        Vec3 from = player.getEyePosition(1.0F);
        Vec3 to = entity.getEyePosition(1.0F);
        BlockHitResult hit = level.clip(new ClipContext(from, to,
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity));
        // 如果射线碰到方块且不是生物本身
        if (hit.getType() == HitResult.Type.BLOCK) {
            double hitDist = hit.getLocation().distanceTo(to);
            if (hitDist > 0.25D) {//0.5
                event.setCanceled(true);  // 不渲染
            }
        }
    }

    public static void onWorldTick(TickEvent.LevelTickEvent event) {
        Level level = event.level;
        if (NoixmodAPIMainConfig.HorrorMode.get() && level.isClientSide) {
            HorrorModeManager manager = HorrorModeManager.horrorModeManagers.get(level);
            if (manager == null) return;
            manager.tick(level);
        }
    }

    //From Polarice's codes
    public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        Minecraft minecraft = Minecraft.getInstance();
        if (event.getLevel().isClientSide) {
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
        if (entity.level().isClientSide) {
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
        if (entity == null) return null;
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
        if (entity == null) return null;
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
