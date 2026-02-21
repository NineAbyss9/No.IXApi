
package com.bilibili.player_ix.noixmod_api.client.particle;

import com.github.NineAbyss9.ix_api.api.annotation.OnlyInClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class NihilisticSpell extends TextureSheetParticle {
    private static final Random random = new Random();
    private final SpriteSet set;

    public NihilisticSpell(ClientLevel world, double x, double y, double z, double vx, double vy, double vz, SpriteSet spriteSets) {
        super(world, x, y, z, 0.5 - random.nextDouble(), vy, 0.5 - random.nextDouble());
        this.friction = 0.96F;
        this.gravity = -0.1F;
        this.speedUpWhenYMotionIsBlocked = true;
        this.set = spriteSets;
        this.yd *= 0.2;
        if (vx == 0.0 && vz == 0.0) {
            this.xd *= 0.1;
            this.zd *= 0.1;
        }
        this.quadSize *= 1f;
        this.lifetime = (int)(8.0 / (Math.random() * 0.8 + 0.2));
        this.hasPhysics = false;
        this.setSpriteFromAge(spriteSets);
        if (this.isCloseToScopingPlayer()) {
            this.setAlpha(0.0F);
        }
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    private boolean isCloseToScopingPlayer() {
        Minecraft $$0 = Minecraft.getInstance();
        LocalPlayer $$1 = $$0.player;
        return $$1 != null && $$1.getEyePosition().distanceToSqr(this.x, this.y, this.z) <= 16.0 &&
                $$0.options.getCameraType().isFirstPerson() && $$1.isScoping();
    }

    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.set);
        if (this.isCloseToScopingPlayer()) {
            this.setAlpha(0.0F);
        } else {
            this.setAlpha(Mth.lerp(0.05F, this.alpha, 1.0F));
        }
    }

    @OnlyInClient
    public static class NihilisticSpellProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public NihilisticSpellProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(@NotNull SimpleParticleType typeIn, @NotNull ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            NihilisticSpell spell = new NihilisticSpell(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
            spell.setColor((float)xSpeed, (float) ySpeed, (float) zSpeed);
            return spell;
        }
    }

    @OnlyInClient
    public static class DarkSpellProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public DarkSpellProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(@NotNull SimpleParticleType typeIn, @NotNull ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            NihilisticSpell spell = new NihilisticSpell(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
            spell.setColor(0, 0, 0);
            return spell;
        }
    }

    @OnlyInClient
    public static class BloodSpellProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sets;

        public BloodSpellProvider(SpriteSet pSet) {
            this.sets = pSet;
        }

        @Nullable
        @Override
        public Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel clientLevel, double v, double v1, double v2, double v3, double v4, double v5) {
            NihilisticSpell spell = new NihilisticSpell(clientLevel, v, v1, v2, v3, v4, v5, this.sets);
            spell.setColor(0.2F, 0F, 0F);
            return spell;
        }
    }
}
