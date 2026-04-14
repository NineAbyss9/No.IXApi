
package com.bilibili.player_ix.noixmod_api.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;

//堕落
public class Corruption
extends TextureSheetParticle {
    protected Corruption(ClientLevel pLevel, double pX, double pY, double pZ) {
        super(pLevel, pX, pY, pZ);
    }

    protected Corruption(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
        super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
        this.friction = 0.96F;
        this.hasPhysics = false;
        this.xd = this.xd * 0.01 + pXSpeed;
        this.yd = this.yd * 0.01 + pYSpeed;
        this.zd = this.zd * 0.01 + pZSpeed;
        this.gravity = -0.01F;
        this.quadSize = 0.2F;
        this.lifetime = 25 + pLevel.random.nextInt(2);
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public static record Provider(SpriteSet set) implements net.minecraft.client.particle.ParticleProvider<SimpleParticleType> {
        public Particle createParticle(SimpleParticleType p_107929_, ClientLevel p_107930_, double p_107931_,
                                       double p_107932_, double p_107933_, double p_107934_, double p_107935_, double p_107936_) {
            Corruption corruption = new Corruption(p_107930_, p_107931_, p_107932_, p_107933_, p_107934_, p_107935_, p_107936_);
            corruption.pickSprite(set);
            return corruption;
        }
    }
}
