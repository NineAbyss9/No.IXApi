
package com.bilibili.player_ix.noixmod_api.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BaseAshSmokeParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

public class ColoredAsh
extends BaseAshSmokeParticle {
    public ColoredAsh(ClientLevel pLevel, double pX, double pY, double pZ, float pR, float pG, float pB, SpriteSet pSprites) {
        super(pLevel, pX, pY, pZ, 0.1F, -0.1F, 0.1F, 0.0, 0.0,
                0.0, 1.0F, pSprites, 0.5F, 40, 0.1F, true);
        this.setColor(pR, pG, pB);
    }

    public record ColoredAshProvider(SpriteSet spriteSet) implements ParticleProvider<SimpleParticleType> {
        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new ColoredAsh(pLevel, pX, pY, pZ, (float)pXSpeed, (float)pYSpeed, (float)pZSpeed, this.spriteSet);
        }
    }
}
