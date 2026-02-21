
package com.bilibili.player_ix.noixmod_api.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class WindParticle
extends TextureSheetParticle {
    private final SpriteSet set;
    public WindParticle(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed,
                        double pZSpeed, SpriteSet setIn) {
        super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
        this.set = setIn;
        this.setSpriteFromAge(set);
        this.gravity = 0.0F;
    }

    public void tick() {
        super.tick();
        this.setSpriteFromAge(set);
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public static class NoMoveProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet setter;
        public NoMoveProvider(SpriteSet setIn) {
            setter = setIn;
        }

        @Nullable
        public Particle createParticle(SimpleParticleType type, ClientLevel clientLevel, double v, double v1, double v2, double v3, double v4, double v5) {
            return new WindParticle(clientLevel, v, v1, v2,0, 0, 0, setter);
        }
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet setter;
        public Provider(SpriteSet setIn) {
            setter = setIn;
        }

        @Nullable
        public Particle createParticle(SimpleParticleType type, ClientLevel clientLevel, double v, double v1, double v2, double v3, double v4, double v5) {
            return new WindParticle(clientLevel, v, v1, v2,v3, v4, v5, setter);
        }
    }
}
