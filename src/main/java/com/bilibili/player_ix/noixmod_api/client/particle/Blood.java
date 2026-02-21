
package com.bilibili.player_ix.noixmod_api.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class Blood
extends TextureSheetParticle {
    private final SpriteSet spriteSet;

    protected Blood(ClientLevel p_108328_, double p_108329_, double p_108330_, double p_108331_, double p_108332_,
                    double p_108333_, double p_108334_, SpriteSet spriteSet1) {
        super(p_108328_, p_108329_, p_108330_, p_108331_, p_108332_, p_108333_, p_108334_);
        this.spriteSet = spriteSet1;
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
        this.setSpriteFromAge(spriteSet1);
    }

    public float getQuadSize(float p_107089_) {
        float $$1 = ((float)this.age + p_107089_) / (float)this.lifetime;
        return this.quadSize * (1.0F - $$1 * $$1);
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public void tick() {
        super.tick();
        this.setSpriteFromAge(spriteSet);
    }

    public record BloodParticleProvide(SpriteSet set)
            implements ParticleProvider<SimpleParticleType> {
        public Particle createParticle(SimpleParticleType type, ClientLevel clientLevel, double v, double v1, double v2,
                                       double v3, double v4, double v5) {
            return new Blood(clientLevel, v, v1, v2, v3, v4, v5, this.set);
        }
    }
}
