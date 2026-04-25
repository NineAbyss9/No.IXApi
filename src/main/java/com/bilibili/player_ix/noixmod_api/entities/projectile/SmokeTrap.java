
package com.bilibili.player_ix.noixmod_api.entities.projectile;

import com.github.NineAbyss9.ix_api.util.ParticleUtil;
import com.bilibili.player_ix.noixmod_api.entities.servant.core.OwnedEntity;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;

public class SmokeTrap extends OwnedEntity {
    public int warmupDelayTicks;
    private boolean sentSpikeEvent;
    private int lifeTicks = 40;
    private boolean clientSideAttackStarted;

    public SmokeTrap(EntityType<? extends SmokeTrap> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            this.level().addParticle(ParticleTypes.LARGE_SMOKE, this.getX(), this.getY() + 0.5,
                    this.getZ(), 0.0, 0.3, 0.0);
            if (this.clientSideAttackStarted) {
                --this.lifeTicks;
            }
        } else if (--this.warmupDelayTicks < 0) {
            if (this.warmupDelayTicks == -16) {
                List<LivingEntity> $$7 = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox()
                        .inflate(1, 0.2, 1), living -> MobUtils.canHurt(living, this));
                for (LivingEntity $$8 : $$7) {
                    this.dealDamageTo($$8);
                }
            }
            if (!this.sentSpikeEvent) {
                this.level().broadcastEntityEvent(this, (byte) 4);
                this.sentSpikeEvent = true;
            }
            if (this.lifeTicks == 1) {
                if (!this.level().isClientSide) {
                    ParticleUtil.sendParticles((ServerLevel)this.level(), ParticleTypes.LARGE_SMOKE, this.position(),
                            20, 0, 0, 0, this.random.nextGaussian() * 0.3);
                }
            }
            if (--this.lifeTicks < 0) {
                this.discard();
            }
        }
    }

    public void dealDamageTo(LivingEntity target) {
        LivingEntity $$1 = this.getOwner();
        if (!MobUtils.canHurt(target, this)) {
            return;
        }
        if ($$1 == null) {
            target.hurt(this.damageSources().magic(), 10f);
        }
        if ($$1 != null) {
            target.hurt(this.damageSources().indirectMagic(this, $$1), 10.0f);
        }
    }

    public void handleEntityEvent(byte $$0) {
        super.handleEntityEvent($$0);
        if ($$0 == 4) {
            this.clientSideAttackStarted = true;
            if (!this.isSilent()) {
                this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.FIRE_EXTINGUISH,
                        this.getSoundSource(), 1.0f, this.random.nextFloat() * 0.2f + 0.85f, false);
            }
        }
    }
}
