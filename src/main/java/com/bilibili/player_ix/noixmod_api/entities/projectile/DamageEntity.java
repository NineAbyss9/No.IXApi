
package com.bilibili.player_ix.noixmod_api.entities.projectile;

import com.github.NineAbyss9.ix_api.util.ParticleUtil;
import com.bilibili.player_ix.noixmod_api.entities.servant.OwnedEntity;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import com.bilibili.player_ix.noixmod_api.util.WorldUtil;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class DamageEntity extends OwnedEntity {
    public float radius = 1;
    public DamageSource source = this.damageSources().magic();
    public float damage = 6F;
    public int lifeTicks = 40;
    public float speed = 0.0f;
    private boolean dieSendParticles;
    private ParticleOptions dieParticle;
    public ParticleOptions options = ParticleTypes.EFFECT;
    public DamageEntity(EntityType<? extends DamageEntity> type, Level level) {
        super(type, level);
    }

    public void tick() {
        --this.lifeTicks;
        if (this.level().isClientSide) {
            this.level().addParticle(this.options, this.getRandomX(0.5), this.getRandomY(),
                    this.getRandomZ(0.5), 0, 0, 0);
        }
        if (this.lifeTicks <= 0) {
            if (this.dieSendParticles) {
                this.dieParticles(null);
            }
            this.damage();
            this.remove(RemovalReason.KILLED);
        }
        super.tick();
    }

    public Component getName() {
        return Component.empty();
    }

    public void damage() {
        MobUtils.rangeHurt(radius, radius, radius, this.getOwner() == null ? this : this.getOwner(), this.source,
                this.damage);
    }

    public void dieParticles(@Nullable ParticleOptions option) {
        if (this.lifeTicks > 0) {
            this.dieParticle = option;
            this.dieSendParticles = true;
        } else {
            ServerLevel level = WorldUtil.getServerLevel(this);
            ParticleUtil.sendParticles(level, this.dieParticle, this.position(), 30, 1, 1, 1, speed);
        }
    }
}
