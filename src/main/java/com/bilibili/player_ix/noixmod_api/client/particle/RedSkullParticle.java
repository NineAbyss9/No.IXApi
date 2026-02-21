
package com.bilibili.player_ix.noixmod_api.client.particle;

import com.github.NineAbyss9.ix_api.ix_api.api.annotation.OnlyInClient;
import com.github.NineAbyss9.ix_api.ix_api.util.Maths;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

@OnlyInClient
public class RedSkullParticle extends TextureSheetParticle {
    private final SpriteSet set;
    public RedSkullParticle(ClientLevel client, double x, double y, double z, double vx, double p_108333_, double vz, SpriteSet spriteSets) {
        super(client, x, y, z, 0.5 - Maths.RANDOM.nextDouble(), p_108333_, 0.5 - Maths.RANDOM.nextDouble());
        this.friction = 0.96F;
        this.gravity = -0.1F;
        this.speedUpWhenYMotionIsBlocked = true;
        this.set = spriteSets;
        this.yd *= 0.2;
        if (vx == 0.0 && vz == 0.0) {
            this.xd *= 0.1;
            this.zd *= 0.1;
        }
        this.quadSize *= 1f;
        this.lifetime = (int)(8.0 / (Math.random() * 0.8 + 0.2));
        this.hasPhysics = false;
        this.setSpriteFromAge(spriteSets);
        if (this.isCloseToScopingPlayer()) {
            this.setAlpha(0.0F);
        }
    }

    private boolean isCloseToScopingPlayer() {
        Minecraft $$0 = Minecraft.getInstance();
        LocalPlayer $$1 = $$0.player;
        return $$1 != null && $$1.getEyePosition().distanceToSqr(this.x, this.y, this.z) <= 16.0 && $$0.options.getCameraType().isFirstPerson() && $$1.isScoping();
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.set);
        if (this.isCloseToScopingPlayer()) {
            this.setAlpha(0.0F);
        } else {
            this.setAlpha(Mth.lerp(0.05F, this.alpha, 1.0F));
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet set;

        public Provider(SpriteSet setter) {
            this.set = setter;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel clientLevel, double v, double v1, double v2, double v3, double v4, double v5) {
            return new RedSkullParticle(clientLevel, v, v1, v2, v3, v4, v5, this.set);
        }
    }
}
