
package com.bilibili.player_ix.noixmod_api.entities.projectile;

import com.github.NineAbyss9.ix_api.api.mobs.Nihilistic;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.NineAbyss9.math.AbyssMath;
import org.NineAbyss9.util.Option;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIMobEffects;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIParticleTypes;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import com.bilibili.player_ix.noixmod_api.util.WorldUtil;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.NineAbyss9.util.ValueHolder;

import java.util.concurrent.ThreadLocalRandom;

public class NihilisticFireball
extends AbstractHurtingProjectile
implements Nihilistic {
    public double radius = 2D;
    public float damage = 8.0F;
    private static final EntityDataAccessor<Boolean> DATA_CAN_BE_HIT;
    public NihilisticFireball(EntityType<? extends NihilisticFireball> p_36833_, Level p_36834_) {
        super(p_36833_, p_36834_);
    }

    public NihilisticFireball(Level level, LivingEntity living, double d, double dou, double o) {
        super(NoixmodAPIEntities.NIHILISTIC_FIREBALL.get(), living, d, dou, o, level);
    }

    public NihilisticFireball(double v, double v1, double v2, double v3, double v4, double v5, Level level) {
        super(NoixmodAPIEntities.NIHILISTIC_FIREBALL.get(), v, v1, v2, v3, v4, v5, level);
    }

    protected void defineSynchedData()
    {
        super.defineSynchedData();
        this.entityData.define(DATA_CAN_BE_HIT, false);
    }

    public boolean hurt(DamageSource pSource, float pAmount)
    {
        if (!(pSource.getEntity() instanceof Player player))
        {
            return false;
        }
        if (this.isInvulnerableTo(pSource)) {
            return false;
        }
        this.markHurt();
        if (!this.level().isClientSide) {
            Vec3 vec3 = player.getLookAngle();
            this.setDeltaMovement(vec3);
            this.xPower = vec3.x * 0.1D;
            this.yPower = vec3.y * 0.1D;
            this.zPower = vec3.z * 0.1D;
            this.setOwner(player);
        }
        return true;
    }

    public EntityType<?> getType() {
        return NoixmodAPIEntities.NIHILISTIC_FIREBALL.get();
    }

    public void addAdditionalSaveData(CompoundTag p_36848_) {
        super.addAdditionalSaveData(p_36848_);
    }

    public void readAdditionalSaveData(CompoundTag p_36844_) {
        super.readAdditionalSaveData(p_36844_);
    }

    public boolean isOnFire() {
        return false;
    }

    public boolean isPickable() {
        return this.entityData.get(DATA_CAN_BE_HIT);
    }

    public void setCanBeHit()
    {
        this.entityData.set(DATA_CAN_BE_HIT, true);
    }

    public void tick() {
        super.tick();
        if (this.tickCount % 75 == 0) {
            if (this.level().isClientSide) {
                return;
            }
            MobUtils.rangeHurt(4, 4, 4, this, this.getDs(), this.damage);
            this.playSound(SoundEvents.GENERIC_EXPLODE);
            ServerLevel world = (ServerLevel)this.level();
            world.sendParticles(ParticleTypes.WITCH, this.getX(), this.getY() + 0.5, this.getZ(), 10,
                    1, 1, 1, 0.1);
            for (int i = 0; i < 2; ++i) {
                NihilisticFire fire = (NoixmodAPIEntities.NIHILISTIC_FIRE.get()).create(world);
                if (fire == null) continue;
                if (!(this.getOwner() instanceof LivingEntity)) break;
                fire.setOwner((LivingEntity)this.getOwner());
                fire.moveTo(this.getX() + AbyssMath.trueOrFalse(1.0D), this.getY(), this.getZ() +
                        AbyssMath.trueOrFalse(1.0D));
                world.addFreshEntity(fire);
            }
            this.remove(RemovalReason.KILLED);
        }
    }

    public boolean isInvulnerableTo(DamageSource p_20122_) {
        return !p_20122_.is(DamageTypes.GENERIC_KILL);
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public DamageSource getDs() {
        return this.damageSources().indirectMagic(ValueHolder.nullToOther(this.getOwner(), this), this.getOwner());
    }

    public void onHit(HitResult pResult) {
        if (this.level().isClientSide) {
            super.onHit(pResult);
            return;
        }
        super.onHit(pResult);
        MobUtils.rangeHurt(4, 4, 4, this, this.getDs(), this.damage);
        this.playSound(SoundEvents.GENERIC_EXPLODE, 1f, 1f);
        for (int i = 0;i < 2;++i)
        {
            NihilisticFire fire = (NoixmodAPIEntities.NIHILISTIC_FIRE.get()).create(this.level());
            if (fire == null) continue;
            if (!(this.getOwner() instanceof LivingEntity)) break;
            fire.setOwner((LivingEntity)this.getOwner());
            fire.moveTo(this.getX() + AbyssMath.trueOrFalse(1.0D), this.getY(), this.getZ() +
                    AbyssMath.trueOrFalse(1.0D));
            this.level().addFreshEntity(fire);
        }
        double d = ThreadLocalRandom.current().nextGaussian() * 0.1d;
        WorldUtil.sendParticles(NoixmodAPIParticleTypes.PURPLE_ATTACK.get(), this, 12, d);
        this.discard();
    }

    protected boolean canHitEntity(Entity pEntity) {
        if (pEntity instanceof LivingEntity living && !MobUtils.canHurt(living, this)) {
            return false;
        }
        return super.canHitEntity(pEntity);
    }

    public void onHitEntity(EntityHitResult pResult) {
        if (this.level().isClientSide) {
            return;
        }
        Entity entity = pResult.getEntity();
        if (entity instanceof LivingEntity living && MobUtils.canHurt(living, this)) {
            living.addEffect(new MobEffectInstance(NoixmodAPIMobEffects.NIHILISTIC.get(), 40), living);
        }
    }

    public void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
    }

    protected ParticleOptions getTrailParticle() {
        return Option.of(NoixmodAPIParticleTypes.NIHILISTIC_FIRE.get()).ifOrElse(
                ThreadLocalRandom.current().nextFloat() <= 0.01, NoixmodAPIParticleTypes.DARK_SPELL.get());
    }

    public void setMoveDown() {
        this.setNoGravity(false);
        this.setDeltaMovement(0d, 0d, 0d);
    }

    static {
        DATA_CAN_BE_HIT = SynchedEntityData.defineId(NihilisticFireball.class, EntityDataSerializers.BOOLEAN);
    }
}
