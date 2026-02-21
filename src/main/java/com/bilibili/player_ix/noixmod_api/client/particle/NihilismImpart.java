package com.bilibili.player_ix.noixmod_api.client.particle;

import com.github.NineAbyss9.ix_api.util.Maths;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class NihilismImpart
extends TextureSheetParticle {
    private static final double nextDouble = Maths.nextDouble;
    private final SpriteSet spriteSet;

    public NihilismImpart(ClientLevel world, double x, double y, double z, double vx, double vy, double vz, SpriteSet spriteSets) {
        super(world, x, y, z, 0.5 - nextDouble, vy, 0.5 - nextDouble);
        this.friction = 0.96F;
        this.gravity = -0.1F;
        this.speedUpWhenYMotionIsBlocked = true;
        this.spriteSet = spriteSets;
        this.yd *= 0.2;
        if (vx == 0.0 && vz == 0.0) {
            this.xd *= 0.1;
            this.zd *= 0.1;
        }
        this.quadSize *= 1F;
        this.lifetime = (int)(8.0 / (Math.random() * 0.8 + 0.2));
        this.hasPhysics = false;
        this.setSpriteFromAge(spriteSets);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    private boolean isCloseToScopingPlayer() {
        Minecraft $$0 = Minecraft.getInstance();
        LocalPlayer $$1 = $$0.player;
        return $$1 != null && $$1.getEyePosition().distanceToSqr(this.x, this.y, this.z) <= 16.0 && $$0.options.getCameraType().isFirstPerson() && $$1.isScoping();
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.spriteSet);
        if (this.isCloseToScopingPlayer()) {
            this.setAlpha(0.0F);
        } else {
            this.setAlpha(Mth.lerp(0.05F, this.alpha, 1.0F));
        }
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(@NotNull SimpleParticleType typeIn, @NotNull ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new NihilismImpart(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }
}
