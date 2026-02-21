
package com.bilibili.player_ix.noixmod_api.client.particle;

import com.github.NineAbyss9.ix_api.util.Maths;
import com.github.NineAbyss9.ix_api.api.annotation.OnlyInClient;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@OnlyInClient
public class SummonParticle
extends TextureSheetParticle {
    private final SpriteSet spriteSet;
    public SummonParticle(ClientLevel p_108328_, double p_108329_, double p_108330_, double p_108331_, SpriteSet pSet) {
        super(p_108328_, p_108329_, p_108330_, p_108331_, 0, 0, 0);
        this.spriteSet = pSet;
        this.friction = 0.96F;
        this.gravity = -0.1F;
        this.speedUpWhenYMotionIsBlocked = true;
        this.xd *= 0.0;
        this.yd *= 0.9;
        this.zd *= 0.0;
        this.xd += xd;
        this.yd += yd;
        this.zd += zd;
        this.quadSize *= 1.25F;
        this.lifetime = (int)(8.0F / Maths.randomBetween(this.random, 0.5F, 1.0F));
        this.lifetime = Math.max(this.lifetime, 1);
        this.setSpriteFromAge(spriteSet);
        this.hasPhysics = true;
    }

    @Override
    public void tick() {
        this.setSpriteFromAge(spriteSet);
        super.tick();
    }

    @NotNull
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @OnlyInClient
    public static class SummonProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet set;

        public SummonProvider(SpriteSet pSet) {
            this.set = pSet;
        }

        @Nullable
        @Override
        public Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel clientLevel, double v, double v1, double v2, double v3, double v4, double v5) {
            return new SummonParticle(clientLevel, v, v1, v2, this.set);
        }
    }
}
