
package com.bilibili.player_ix.noixmod_api.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LightTexture;

/**Based on Polarice3's ShockwaveParticle
*/
public class CircleParticle extends ImmobileCircleParticle {
    private final SpriteSet spriteSet;
    @SuppressWarnings("FieldMayBeFinal")
    private float originSize = 0.0F;
    private float finalSize;
    private float speed;
    private final boolean reverse;
    CircleParticle(ClientLevel pLevel, double p_108329_, double p_108330_, double p_108331_, float red, float green, float blue, int life, boolean reverse, SpriteSet set) {
        super(pLevel, p_108329_, p_108330_, p_108331_, 0, 0, 0);
        this.spriteSet = set;
        this.pickSprite(set);
        this.quadSize = 0.0F;
        this.lifetime = life;
        this.gravity = 0.0F;
        this.xd = 0.0D;
        this.yd = 0.0D;
        this.zd = 0.0D;
        this.rCol = red;
        this.gCol = green;
        this.bCol = blue;
        this.reverse = reverse;
    }

    CircleParticle(ClientLevel pLevel, double p_108329_, double p_108330_, double p_108331_, float red, float green, float blue, int life, SpriteSet set) {
        this(pLevel, p_108329_, p_108330_, p_108331_, red, green, blue, life, false, set);
    }

    protected int getLightColor(float pPartialTick) {
        return LightTexture.FULL_BRIGHT;
    }

    public float getQuadSize(float partialTicks) {
        if (this.reverse){
            return Math.max(this.originSize / (this.age + partialTicks + 1), this.quadSize);
        } else {
            return this.quadSize;
        }
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime)
            remove();
        else {
            if (this.speed > 0) {
                if (this.originSize <= this.finalSize) {
                    if (this.quadSize < this.finalSize) {
                        this.quadSize += this.speed;
                    }
                } else {
                    if (this.quadSize > this.finalSize) {
                        this.quadSize -= this.speed;
                    }
                }
                this.setSpriteFromAge(this.spriteSet);
            }
        }
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public record Provider(SpriteSet spriteSet) implements ParticleProvider<CircleParticleOption> {
        public Particle createParticle(CircleParticleOption circleParticle, ClientLevel clientLevel, double v, double v1, double v2,
                                       double v3, double v4, double v5) {
            CircleParticle particle = new CircleParticle(clientLevel, v, v1, v2, circleParticle.getRed(), circleParticle.getGreen(),
                    circleParticle.getBlue(), 30, spriteSet);
            particle.speed = circleParticle.getSpeed();
            particle.finalSize = circleParticle.getSize();
            particle.setAlpha(1F);
            return particle;
        }
    }
}
