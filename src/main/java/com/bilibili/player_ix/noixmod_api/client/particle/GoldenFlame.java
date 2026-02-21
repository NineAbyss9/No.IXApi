
package com.bilibili.player_ix.noixmod_api.client.particle;

import com.github.NineAbyss9.ix_api.ix_api.api.annotation.OnlyInClient;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

@OnlyInClient
public class GoldenFlame extends RisingParticle {
    public GoldenFlame(ClientLevel p_108328_, double p_108329_, double p_108330_, double p_108331_, double p_108332_, double p_108333_, double p_108334_) {
        super(p_108328_, p_108329_, p_108330_, p_108331_, p_108332_, p_108333_, p_108334_);
    }

    public void tick() {
        super.tick();
    }

    public int getLightColor(float p_106821_) {
        float $$1 = ((float)this.age + p_106821_) / (float)this.lifetime;
        $$1 = Mth.clamp($$1, 0.0F, 1.0F);
        int $$2 = super.getLightColor(p_106821_);
        int $$3 = $$2 & 255;
        int $$4 = $$2 >> 16 & 255;
        $$3 += (int)($$1 * 15.0F * 16.0F);
        if ($$3 > 240) {
            $$3 = 240;
        }
        return $$3 | $$4 << 16;
    }

    public void move(double p_106817_, double p_106818_, double p_106819_) {
        this.setBoundingBox(this.getBoundingBox().move(p_106817_, p_106818_, p_106819_));
        this.setLocationFromBoundingbox();
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public static class GoldenFlameProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet set;

        public GoldenFlameProvider(SpriteSet pSet) {
            this.set = pSet;
        }

        @Nullable
        public Particle createParticle(SimpleParticleType type, ClientLevel clientLevel, double v, double v1, double v2, double v3, double v4, double v5) {
            GoldenFlame particle = new GoldenFlame(clientLevel, v, v1, v2, v3, v4, v5);
            particle.pickSprite(this.set);
            return particle;
        }
    }
}
