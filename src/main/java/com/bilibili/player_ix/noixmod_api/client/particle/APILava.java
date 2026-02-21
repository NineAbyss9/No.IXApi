
package com.bilibili.player_ix.noixmod_api.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class APILava implements ParticleProvider<SimpleParticleType> {
    private final SpriteSet set;
    public APILava(SpriteSet setter) {
        set = setter;
    }

    @Nullable
    @Override
    public Particle createParticle(SimpleParticleType dustParticle, ClientLevel clientLevel, double v, double v1, double v2,
                                   double v3, double v4, double v5) {
        TextureSheetParticle particle = DripParticle.createLavaLandParticle(dustParticle, clientLevel, v, v1, v2, v3, v4, v5);
        particle.pickSprite(this.set);
        return particle;
    }
}
