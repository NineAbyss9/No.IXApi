
package com.bilibili.player_ix.noixmod_api.client.particle;

import com.github.NineAbyss9.ix_api.ix_api.api.annotation.OnlyInClient;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

@OnlyInClient
public class PurpleFlame extends RisingParticle {
    private static final Random RANDOM = new Random();
    public PurpleFlame(ClientLevel p_107631_, double p_107632_, double p_107633_, double p_107634_, double p_107635_, double p_107636_, double p_107637_) {
        super(p_107631_, p_107632_, p_107633_, p_107634_, p_107635_, p_107636_, p_107637_);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
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

    @Override
    public void move(double p_106817_, double p_106818_, double p_106819_) {
        this.setBoundingBox(this.getBoundingBox().move(p_106817_, p_106818_, p_106819_));
        this.setLocationFromBoundingbox();
    }

    public static class PurpleFlameProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet set;
        public PurpleFlameProvider(SpriteSet pSet) {
            this.set = pSet;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel clientLevel, double v, double v1, double v2, double v3, double v4, double v5) {
            PurpleFlame flame = new PurpleFlame(clientLevel, v, v1, v2, v3, v4, v5);
            flame.pickSprite(this.set);
            return flame;
        }
    }

    public static class RisingPurpleFlameProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet set;
        public RisingPurpleFlameProvider(SpriteSet pSet) {
            this.set = pSet;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel clientLevel, double v, double v1, double v2, double v3, double v4, double v5) {
            double var = RANDOM.nextDouble() - 0.8;
            double d = var <= 0 ? 0.2 : var;
            PurpleFlame particle = new PurpleFlame(clientLevel, v, v1, v2, 0, d, 0);
            particle.pickSprite(this.set);
            return particle;
        }
    }
}
