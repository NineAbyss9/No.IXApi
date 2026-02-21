
package com.bilibili.player_ix.noixmod_api.client.particle;

import com.github.NineAbyss9.ix_api.api.annotation.OnlyInClient;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@OnlyInClient
public class NihilisticFireParticle
extends TextureSheetParticle {
    private final SpriteSet set;
    public NihilisticFireParticle(ClientLevel p_108328_, double p_108329_, double p_108330_, double p_108331_, double p_108332_, double p_108333_, double p_108334_, SpriteSet p_108335) {
        super(p_108328_, p_108329_, p_108330_, p_108331_, p_108332_, p_108333_, p_108334_);
        this.set = p_108335;
        this.friction = 0.96F;
        this.xd = this.xd * 0.01 + p_108332_;
        this.yd = this.yd * 0.01 + p_108333_;
        this.zd = this.zd * 0.01 + p_108334_;
        this.gravity = -0.01F;
        this.quadSize = 0.4F;
        this.lifetime = 10 + p_108328_.random.nextInt(2);
        this.setSpriteFromAge(this.set);
    }

    @Override
    public void tick() {
        this.setSpriteFromAge(this.set);
        super.tick();
    }

    @NotNull
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public static class FireProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet pSet;
        public FireProvider(SpriteSet spriteSet) {
            this.pSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel clientLevel, double v, double v1, double v2, double v3, double v4, double v5) {
            return new NihilisticFireParticle(clientLevel, v, v1, v2, v3, v4, v5, this.pSet);
        }
    }

    public static class SmallFireProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet pSet;
        public SmallFireProvider(SpriteSet spriteSet) {
            this.pSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel clientLevel, double v, double v1, double v2, double v3, double v4, double v5) {
            NihilisticFireParticle fire = new NihilisticFireParticle(clientLevel, v, v1, v2, v3, v4, v5, this.pSet);
            fire.scale(0.6F);
            return fire;
        }
    }
}
