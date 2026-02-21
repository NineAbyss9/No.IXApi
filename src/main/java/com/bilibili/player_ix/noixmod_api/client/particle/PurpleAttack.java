
package com.bilibili.player_ix.noixmod_api.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class PurpleAttack
extends TextureSheetParticle {
    private final SpriteSet set;

    public PurpleAttack(ClientLevel pLevel, double pX, double pY, double pZ, double p_108332_, double p_108333_, double p_108334_, SpriteSet spriteSets) {
        super(pLevel, pX, pY, pZ, p_108332_, p_108333_, p_108334_);
        this.friction = 0.999f;
        this.gravity = 0.75f;
        this.speedUpWhenYMotionIsBlocked = false;
        this.set = spriteSets;
        this.hasPhysics = true;
        this.xd *= 0.800000011920929;
        this.yd *= 0.800000011920929;
        this.zd *= 0.800000011920929;
        this.yd = this.random.nextFloat() * 0.4F + 0.05F;
        this.quadSize *= this.random.nextFloat() * 2.0F + 0.2F;
        this.lifetime = (int)(16.0 / (Math.random() * 0.8 + 0.2));
        this.setSpriteFromAge(spriteSets);
    }

    public float getQuadSize(float p_107089_) {
        float $$1 = ((float)this.age + p_107089_) / (float)this.lifetime;
        return this.quadSize * (1.0F - $$1 * $$1);
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public void tick() {
        super.tick();
        this.setSpriteFromAge(set);
    }

    public record PurpleAttackProvider(SpriteSet set) implements ParticleProvider<SimpleParticleType> {
        public Particle createParticle(SimpleParticleType type, ClientLevel clientLevel, double v, double v1, double v2, double v3,
                                       double v4, double v5) {
            return new PurpleAttack(clientLevel, v, v1, v2, v3, v4, v5, this.set);
        }
    }
}
