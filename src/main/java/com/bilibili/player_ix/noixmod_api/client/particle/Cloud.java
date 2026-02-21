
package com.bilibili.player_ix.noixmod_api.client.particle;

import com.github.NineAbyss9.ix_api.ix_api.util.Colors;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class Cloud extends TextureSheetParticle {
    Cloud(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
        super(pLevel, pX, pY, pZ, 0.0, 0.0, 0.0);
        this.scale(3.0F);
        this.setSize(0.25F, 0.25F);
        this.lifetime = this.random.nextInt(50) + 280;
        this.gravity = 3.0E-6F;
        this.xd = pXSpeed;
        this.yd = pYSpeed + (double)(this.random.nextFloat() / 500.0F);
        this.zd = pZSpeed;
        Colors.setColor(this, pXSpeed, pYSpeed, pZSpeed);
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ < this.lifetime && !(this.alpha <= 0.0F)) {
            this.xd += this.random.nextFloat() / 5000.0F * (float)(this.random.nextBoolean() ? 1 : -1);
            this.zd += this.random.nextFloat() / 5000.0F * (float)(this.random.nextBoolean() ? 1 : -1);
            this.yd -= this.gravity;
            this.move(this.xd, this.yd, this.zd);
            if (this.age >= this.lifetime - 60 && this.alpha > 0.01F) {
                this.alpha -= 0.015F;
            }
        } else {
            this.remove();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet p_107507_) {
            this.sprites = p_107507_;
        }

        public Particle createParticle(SimpleParticleType p_107518_, ClientLevel p_107519_, double p_107520_, double p_107521_, double
                p_107522_, double p_107523_, double p_107524_, double p_107525_) {
            Cloud cloud = new Cloud(p_107519_, p_107520_, p_107521_, p_107522_, 0.0, 0.0, 0.0);
            cloud.pickSprite(sprites);
            return cloud;
        }
    }

    public static class BlackProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public BlackProvider(SpriteSet p_107507_) {
            this.sprites = p_107507_;
        }

        public Particle createParticle(SimpleParticleType p_107518_, ClientLevel p_107519_, double p_107520_, double p_107521_, double
                p_107522_, double p_107523_, double p_107524_, double p_107525_) {
            Cloud cloud = new Cloud(p_107519_, p_107520_, p_107521_, p_107522_, 0.0, 0.0, 0.0);
            cloud.pickSprite(sprites);
            return cloud;
        }
    }
}
