
package com.bilibili.player_ix.noixmod_api.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@OnlyIn(value = Dist.CLIENT)
public class WormParticle
extends TextureSheetParticle {
    private final SpriteSet set;
    public WormParticle(ClientLevel p_108328_, double p_108329_, double p_108330_, double p_108331_, double p_108332_, double p_108333_, double p_108334_, SpriteSet spriteSet) {
        super(p_108328_, p_108329_, p_108330_, p_108331_, p_108332_, p_108333_, p_108334_);
        this.set = spriteSet;
        this.hasPhysics = true;
        this.friction = 0.999f;
        this.gravity = 0.75f;
        this.speedUpWhenYMotionIsBlocked = false;
        this.xd *= 0.8;
        this.yd *= 0.8;
        this.zd *= 0.8;
        this.yd = this.random.nextFloat() * 0.4F + 0.05F;
        this.quadSize *= this.random.nextFloat() * 2.0F + 0.2F;
        this.lifetime = (int)(16.0 / (Math.random() * 0.8 + 0.2));
        this.setSpriteFromAge(spriteSet);
    }

    @OnlyIn(value = Dist.CLIENT)
    public static class Provider
    implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet setter;

        public Provider(SpriteSet set) {
            this.setter = set;
        }

        @Nullable
        @Override
        public Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel clientLevel, double v, double v1, double v2, double v3, double v4, double v5) {
            return new WormParticle(clientLevel, v, v1, v2, v3, v4, v5, this.setter);
        }
    }

    @Override
    public float getQuadSize(float p_107681_) {
        float $$1 = ((float)this.age + p_107681_) / (float)this.lifetime;
        return this.quadSize * (1.0F - $$1 * $$1);
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.set);
    }

    @NotNull
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }
}
