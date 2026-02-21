
package com.bilibili.player_ix.noixmod_api.entities.projectile;

import com.github.NineAbyss9.ix_api.util.ParticleUtil;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIParticleTypes;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class SwordWave
extends AbstractHurtingProjectile {
    private static final EntityDataAccessor<Integer> DATA_LIFE;
    private static final EntityDataAccessor<ParticleOptions> DATA_PARTICLE;
    public SwordWave(EntityType<? extends SwordWave> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public SwordWave(EntityType<? extends SwordWave> pEntityType, double pX, double pY, double pZ,
                     double pOffsetX, double pOffsetY, double pOffsetZ, Level pLevel) {
        super(pEntityType, pX, pY, pZ, pOffsetX, pOffsetY, pOffsetZ, pLevel);
    }

    public SwordWave(EntityType<? extends SwordWave> pEntityType, LivingEntity pShooter, double pOffsetX,
                     double pOffsetY, double pOffsetZ, Level pLevel) {
        super(pEntityType, pShooter, pOffsetX, pOffsetY, pOffsetZ, pLevel);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_LIFE, 200);
        this.entityData.define(DATA_PARTICLE, NoixmodAPIParticleTypes.COLORED_ASH.get());
    }

    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            this.level().addParticle(this.getParticle(), this.getRandomX(0.5), this.getRandomY(),
                    this.getRandomZ(0.5), 0.0, 0.0, 0.0);
        }
        this.reduceLife();
    }

    protected void onHit(HitResult pResult) {
        if (!this.level().isClientSide) {
            ParticleUtil.sendParticles((ServerLevel)this.level(), this.getParticle(), this.position().add(0, 0.3, 0),
                    12, 0.5, 0.5, 0.5, 0);
        }
        super.onHit(pResult);
    }

    protected boolean canHitEntity(Entity pEntity) {
        if (pEntity instanceof LivingEntity entity && !MobUtils.canHurt(entity, this)) {
            return false;
        }
        return super.canHitEntity(pEntity);
    }

    private ParticleOptions getParticle() {
        return this.entityData.get(DATA_PARTICLE);
    }

    public void setParticle(ParticleOptions pOptions) {
        this.entityData.set(DATA_PARTICLE, pOptions);
    }

    public int getLife() {
        return this.entityData.get(DATA_LIFE);
    }

    public void setLife(int pLife) {
        this.entityData.set(DATA_LIFE, pLife);
    }

    public void remove(RemovalReason pReason) {
        if (pReason.equals(RemovalReason.DISCARDED)
            && this.getLife() > 0) {
            return;
        }
        super.remove(pReason);
    }

    public void reduceLife() {
        this.setLife(this.getLife() - 1);
        if (this.getLife() <= 0) {
            this.remove(RemovalReason.KILLED);
        }
    }

    static {
        DATA_LIFE = SynchedEntityData.defineId(SwordWave.class, EntityDataSerializers.INT);
        DATA_PARTICLE = SynchedEntityData.defineId(SwordWave.class, EntityDataSerializers.PARTICLE);
    }
}
