
package com.bilibili.player_ix.noixmod_api.entities.servant.nihilistic;

import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIDamageSource;
import com.github.NineAbyss9.ix_api.api.mobs.Nihilistic;
import com.github.NineAbyss9.ix_api.api.mobs.Ownable;
import com.github.NineAbyss9.ix_api.api.mobs.OwnableMob;
import com.github.NineAbyss9.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import com.bilibili.player_ix.noixmod_api.entities.boss.Apostle;
import com.bilibili.player_ix.noixmod_api.entities.projectile.summon.SummonEntity;
import com.bilibili.player_ix.noixmod_api.entities.servant.core.AbstractStatue;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import com.bilibili.player_ix.noixmod_api.util.OwnerSummon;
import com.bilibili.player_ix.noixmod_api.util.WorldUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class NihilisticStatue
extends AbstractStatue
implements Nihilistic {
    private int summonCoolDown = 60;
    @Nullable
    public Ownable ownable;
    public int summonNum = ThreadLocalRandom.current().nextInt(6);
    private int ownerInvTicks;
    public NihilisticStatue(EntityType<? extends NihilisticStatue> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
    }

    public boolean isAlliedTo(@Nullable Entity p_20355_) {
        if (p_20355_ == null) {
            return false;
        }
        if (p_20355_ == this) {
            return true;
        }
        if (p_20355_ instanceof Ownable own) {
            return this.isAlliedTo(own.getOwner());
        }
        return super.isAlliedTo(p_20355_);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new FollowOwnerGoal<>(this, 0,
                30f, 5f, false, 100, true));
        this.targetSelector.addGoal(1, new OwnerHurtTargetGoal<>(this));
    }

    public void aiStep() {
        super.aiStep();
        this.setYHeadRot(0);
        float f8 = (ThreadLocalRandom.current().nextFloat() - 0.5F) * 8.0F;
        float f9 = (ThreadLocalRandom.current().nextFloat() - 0.5F) * 4.0F;
        float f11 = (ThreadLocalRandom.current().nextFloat() - 0.5F) * 8.0F;
        this.level().addParticle(ParticleTypes.WITCH, this.getX() + f8, this.getY() + 2.0 +
                f9, this.getZ() + f11, 0.0, 0.0, 0.0);
        LivingEntity owner = this.getOwner();
        if (owner != null) {
            if (this.tickCount % this.getHealCoolDown() == 0) {
                this.heal(this.getHealValue());
                if (this.level() instanceof ServerLevel level) level.sendParticles(ParticleTypes.SOUL, this.getX(),
                        this.getRandomY(), this.getZ(), 12, 2, 0, 2,
                        ThreadLocalRandom.current().nextGaussian() * 0.3D);
            }
        }
        if (!this.onGround()) {
            MobUtils.moveToGround(this);
        }
    }

    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.getSummonCoolDown() > 0) {
            --this.summonCoolDown;
        }
        if (this.ownerInvTicks > 0) {
            --this.ownerInvTicks;
        }
    }

    public void tick() {
        super.tick();
        if (this.getSummonCoolDown() == 0) {
            if (this.level() instanceof ServerLevel level) {
                int i = NoixmodAPIMainConfig.HorrorMode.get() ? Integer.MAX_VALUE : 12;
                if (OwnerSummon.canSummon(level, OwnableMob.ownerOrThis(this, this), i)) {
                    this.summonServant(ownable);
                }
            }
        }
        LivingEntity owner = this.getOwner();
        if (owner != null && this.isAlive()) {
            if (this.ownerInvTicks > 100) {
                if (owner instanceof Apostle apostle) {
                    apostle.setInvTime(100);
                }
                this.targetList().forEach(this::target);
            }
            if (this.ownerInvTicks == 100) {
                if (owner instanceof Apostle apostle) {
                    apostle.setInvTime(0);
                }
            }
            if (this.ownerInvTicks <= 0) {
                this.setOwnerInvTicks(200);
            }
        }
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.is(DamageTypeTags.IS_FALL)) {
            return false;
        }
        return super.hurt(pSource, pAmount);
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("SummonNumber", this.summonNum);
        tag.putInt("SummonCooldown", this.summonCoolDown);
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.summonNum = tag.getInt("SummonNumber");
        this.summonCoolDown = tag.getInt("SummonCooldown");
    }

    public void die(DamageSource p_21014_) {
        this.setOwnerInvTicks(0);
        LivingEntity owner = this.getOwner();
        if (owner != null) {
            if (owner instanceof Apostle apostle) {
                apostle.setStatueCooldown();
                apostle.setInvTime(0);
            }
            if (!owner.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN))) {
                owner.hurt(this.damageSources().starve(), 4);
            } else {
                owner.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 400,
                        0));
                owner.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 400, 0));
            }
        }
        MobUtils.rangeHurt(6, 6, 6, this, NoixmodAPIDamageSource.nihility(this),
                12, lie -> lie != owner && lie != this);
        this.playSound(SoundEvents.FIRE_EXTINGUISH);
        if (!this.level().isClientSide) {
            WorldUtil.sendParticles(ParticleTypes.LARGE_SMOKE, this, 50, 0.35D);
        }
        this.discard();
        super.die(p_21014_);
    }

    public List<Mob> targetList() {
        return this.level().getEntitiesOfClass(Mob.class, this.getBoundingBox().inflate(128, 6, 128));
    }

    public void target(Mob target) {
        if (target.getTarget() == this.getOwner()) {
            target.setTarget(this);
        }
    }

    public int getOwnerInvTicks() {
        return this.ownerInvTicks;
    }

    public void setOwnerInvTicks(int i) {
        this.ownerInvTicks = i;
    }

    public int getHealCoolDown() {
        LivingEntity lie = this.getOwner();
        if (lie != null) {
            float f = lie.getHealth() <= 0 ? 1 : lie.getHealth();
            float v = lie.getMaxHealth();
            int va = (int)(v / f);
            return Math.max(va, 80);
        }
        return 160;
    }

    public float getHealValue() {
        LivingEntity lie = this.getOwner();
        if (lie == null) {
            return 1f;
        }
        float f = lie.getHealth() <= 0 ? 1 : lie.getHealth();
        float v = lie.getMaxHealth();
        float va = v / f;
        return Math.min(va, 2f);
    }

    public void summonServant(@Nullable Ownable ownable) {
        if (this.level() instanceof ServerLevel ser) {
            int i = NoixmodAPIMainConfig.HorrorMode.get() ? Integer.MAX_VALUE : 12;
            if (!OwnerSummon.canSummon(ser, OwnableMob.ownerOrThis(this, this), i)) {
                return;
            }
            this.ownable = switch (this.summonNum) {
                case 0 ->
                    NoixmodAPIEntities.ZOMBIE_VINDICATOR.get().create(ser);
                case 1 ->
                    NoixmodAPIEntities.MAGICAL_CLONE.get().create(ser);
                case 2 ->
                    new NihilisticWither(NoixmodAPIEntities.NIHILISTIC_WITHER.get(), ser);
                case 3 ->
                    NoixmodAPIEntities.NIHILISTIC_SERVANT.get().create(ser);
                case 4 ->
                    NoixmodAPIEntities.DROWNED_SERVANT.get().create(ser);
                default ->
                    NoixmodAPIEntities.NIHILISTIC_BLAZE.get().create(ser);
            };
            BlockPos pos = this.blockPosition();
            BlockPos d = pos.offset(Maths.randomInteger(3, ThreadLocalRandom.current()), 0,
                    Maths.randomInteger(3, ThreadLocalRandom.current()));
            if (ownable instanceof Mob mob) {
                SummonEntity entity = new SummonEntity(NoixmodAPIEntities.SUMMON_ENTITY.get(), ser);
                entity.entity(mob);
                entity.setDangerous(false);
                entity.setOwner(ownerOrThis(this, this));
                entity.moveTo(d, 0, 0);
                ser.addFreshEntity(entity);
            }
            if (this.ownable instanceof NihilisticWither) {
                this.setSummonCoolDown(Maths.toTick(300));
            } else {
                this.setSummonCoolDown(100);
            }
        }
    }

    public int getSummonCoolDown() {
        return this.summonCoolDown;
    }

    public void setSummonCoolDown(int summonCoolDown) {
        this.summonCoolDown = summonCoolDown;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return NihilisticStatue.createPathAttributes().add(Attributes.MAX_HEALTH, 40)
                .add(Attributes.ARMOR, 10).add(Attributes.KNOCKBACK_RESISTANCE, 1)
                .add(Attributes.FOLLOW_RANGE, 100);
    }

    public boolean addEffect(MobEffectInstance p_147208_, @Nullable Entity p_147209_) {
        return false;
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return SoundEvents.AMBIENT_CAVE.get();
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource p_21239_) {
        return SoundEvents.STONE_BREAK;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return SoundEvents.RESPAWN_ANCHOR_DEPLETE.get();
    }
}