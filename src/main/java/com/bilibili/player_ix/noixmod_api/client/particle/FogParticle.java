
package com.bilibili.player_ix.noixmod_api.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.util.Mth;

public class FogParticle
extends TextureSheetParticle {
    public FogParticle(ClientLevel p_108328_, double p_108329_, double p_108330_, double p_108331_) {
        super(p_108328_, p_108329_, p_108330_, p_108331_, 0, 0, 0);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.age++;
        if (this.age >= this.lifetime) {
            this.remove();
        } else {
            if (this.gravity > 0.0F) {
                this.yd -= 0.04D * (double) this.gravity;
            }
            this.move(this.xd, this.yd, this.zd);
            this.xd *= 0.94F;
            this.yd *= 0.85F;
            this.zd *= 0.94F;
        }
    }

    @Override
    public void render(VertexConsumer p_107678_, Camera p_107679_, float p_107680_) {
        this.alpha = 1.0F - Mth.clamp((this.age + p_107680_) / this.lifetime, 0.0F, 1.0F);
        super.render(p_107678_, p_107679_, p_107680_);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class FogProvider implements ParticleProvider<FogParticleOption> {
        private final SpriteSet setter;
        public FogProvider(SpriteSet set) {
            setter = set;
        }

        @Override
        public Particle createParticle(FogParticleOption type, ClientLevel clientLevel, double v, double v1,
                                                 double v2, double v3, double v4, double v5) {
            FogParticle particle = new FogParticle(clientLevel, v, v1, v2);
            particle.setAlpha(1.0f);
            particle.setColor((float)v3, (float)v4, (float)v5);
            particle.pickSprite(setter);
            return particle;
        }
    }
}
