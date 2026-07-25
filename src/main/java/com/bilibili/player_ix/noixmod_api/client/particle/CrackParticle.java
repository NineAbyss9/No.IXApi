
package com.bilibili.player_ix.noixmod_api.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class CrackParticle
extends TextureSheetParticle {
    protected CrackParticle(ClientLevel pLevel, double pX, double pY, double pZ, SpriteSet pSet) {
        super(pLevel, pX, pY, pZ, 0, 0, 0);
        this.setLifetime(200);
        this.pickSprite(pSet);
        this.gravity = 0.0F;
        this.hasPhysics = false;
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static record Provider(SpriteSet set) implements ParticleProvider<SimpleParticleType> {
        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ,
                                       double pXSpeed, double pYSpeed, double pZSpeed) {
            return new CrackParticle(pLevel, pX, pY, pZ, set);
        }
    }
}
