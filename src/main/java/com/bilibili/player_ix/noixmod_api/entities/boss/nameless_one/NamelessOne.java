
//换成自己的程序包
package com.bilibili.player_ix.noixmod_api.entities.boss.nameless_one;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public class NamelessOne
extends Monster {
    private int spellTick;
    private int teleportCooldown;
    public AnimationState staffAttack = new AnimationState();
    public AnimationState boltAttack = new AnimationState();
    public AnimationState summon = new AnimationState();
    private static final EntityDataAccessor<Integer> DATA_BOSS_FLAGS;
    private static final EntityDataAccessor<Boolean> DATA_SECOND_PHASE;
    private static final EntityDataAccessor<Boolean> DATA_DAY;
    private static final float DAMAGE_CAPE = 20f;
    public NamelessOne(EntityType<? extends NamelessOne> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.setDay(p_33003_.isDay());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_BOSS_FLAGS, 0);
        this.entityData.define(DATA_SECOND_PHASE, false);
        this.entityData.define(DATA_DAY, false);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> p_21104_) {
        if (DATA_BOSS_FLAGS.equals(p_21104_)) {
            if (this.level().isClientSide) {
                //这块代码可能写的有点缺漏
                switch (this.getBossFlag()) {
                    case 1: {
                        this.stopAllAnimations();
                        this.staffAttack.startIfStopped(this.tickCount);
                        break;
                    }
                    case 2: {
                        this.stopAllAnimations();
                        this.boltAttack.startIfStopped(this.tickCount);
                        break;
                    }
                    case 3: {
                        this.stopAllAnimations();
                        this.summon.startIfStopped(this.tickCount);
                        break;
                    }
                    case 0:
                    default: {
                        break;
                    }
                }
            }
        }
        super.onSyncedDataUpdated(p_21104_);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, LivingEntity.class, 12f));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1) {
            @Override
            public boolean canUse() {
                if (this.mob.getTarget() != null) {
                    return false;
                }
                return super.canUse();
            }

            @Override
            public boolean canContinueToUse() {
                if (this.mob.getTarget() != null) {
                    return false;
                }
                return super.canContinueToUse();
            }
        });
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            ServerLevel level = this.getServerLevel();
            long time = this.isDay() ? 7200L : 0L;
            //锁白天或夜天
            level.setDayTime(time);
        }
    }

    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.healTicker() != 0 && this.tickCount % this.healTicker() == 0) {
            this.heal(1f);
        }
        if (this.teleportCooldown > 0) {
            this.teleportCooldown--;
        }
    }

    public boolean hurt(DamageSource damageSource, float amount) {
        amount = Math.min(DAMAGE_CAPE, amount);
        if (!this.isDay()) {
            amount /= 10;
        }
        if (this.getLightLevel() <= 0.0f) {
            return false;
        }
        return super.hurt(damageSource, amount);
    }

    //动态减伤不会写qwq
    public void setHealth(float p_21154_) {
        float health = this.getHealth();
        float delta = p_21154_ - health;
        if (delta < 0.0f) {
            if (this.getLightLevel() <= 0.0f) {
                return;
            }
            if (delta < -DAMAGE_CAPE) {
                p_21154_ = health - 20.0f;
            }
        }
        super.setHealth(p_21154_);
    }

    @SuppressWarnings("deprecation")
    public float getLightLevelDependentMagicValue() {
        return super.getLightLevelDependentMagicValue();
    }

    private float getLightLevel() {
        float var = this.getLightLevelDependentMagicValue();
        return nights().isEmpty() ? var : Math.max(var - 6.0f, 0.0f);
    }

    private boolean isLightBlock(BlockState state, BlockPos position) {
        return state.getLightBlock(this.level(), position) > 0;
    }

    public boolean isCastingSpell() {
        return this.spellTick > 0;
    }

    private List<HeartOfDarkNight> nights() {
        //下面3个double为寻找暗夜之心的范围
        double range0 = 32;
        double range1 = 16;
        double range2 = 32;
        return this.level().getEntitiesOfClass(HeartOfDarkNight.class, this.getBoundingBox().inflate(range0, range1, range2),
                night -> night.getOwner() == this);
    }

    /**使用这个方法的例子:<blockquote><pre>
     *     {@code if (!this.level().isClientSide && this.tickCount % this.healTicker()==0 && this.healTicker() != 0) {
     *         this.heal(1f);
     *     }}
     * </pre></blockquote>*/
    private int healTicker() {
        float f = this.getLightLevel();
        if (f < 2) {
            return 10;
        } else if (f < 4) {
            return 20;
        } else if (f < 7) {
            return 40;
        } else {
            return 0;
        }
    }

    /**为了简便，这里写一个转换器*/
    public ServerLevel getServerLevel() {
        //抛异常是为了及时发现bug.
        if (this.level().isClientSide) {
            throw new ClassCastException("ClientLevel cannot cast to be server level.");
        }
        return (ServerLevel)this.level();
    }

    //下面两个是设置锁白天或夜天的
    public boolean isDay() {
        return this.entityData.get(DATA_DAY);
    }

    private void setDay(boolean day) {
        this.entityData.set(DATA_DAY, day);
    }

    //传送
    private void escapeTeleport() {
        if (this.teleportCooldown > 0) {
            return;
        }
        //设置tp冷却
        this.teleportCooldown = 140;
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();
        //抄的使徒代码（），优化了点
        for (int i = 0; i < 99; ++i) {
            double randomX = x + (this.getRandom().nextDouble() - 0.5) * 12;
            double ry = y + (this.getRandom().nextDouble() - 0.5) * 3;
            double randomZ = z + (this.getRandom().nextDouble() - 0.5) * 12;
            if (this.randomTeleport(randomX, ry, randomZ, false)) {
                double dis = 0.5;
                this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1f, 0.5f);
                this.getServerLevel().sendParticles(ParticleTypes.SMOKE, x, y + 1, z, 30, dis, dis, dis, 0.1);
                break;
            }
        }
    }

    @Override
    public int getExperienceReward() {
        if (this.level().isDay()) {
            return 1000;
        } else {
            return 12000;
        }
    }

    public int getBossFlag() {
        return this.entityData.get(DATA_BOSS_FLAGS);
    }

    private void setBossFlag(int flag) {
        this.entityData.set(DATA_BOSS_FLAGS, flag);
    }

    private List<AnimationState> allAnimations() {
        List<AnimationState> states = new ArrayList<>();
        states.add(this.staffAttack);
        states.add(this.boltAttack);
        states.add(this.summon);
        return states;
    }

    private void stopAllAnimations() {
        for (AnimationState state : allAnimations()) {
            state.stop();
        }
    }

    //这里是召唤列表
    @Unmodifiable
    @Contract(pure = true)
    private Map<Integer, EntityType<?>> summonMap() {
        return ImmutableMap.of(1, EntityType.HUSK, 2, EntityType.ZOMBIE, 3, EntityType.ZOMBIE_VILLAGER,
                4, EntityType.ZOMBIFIED_PIGLIN, 5, EntityType.WITHER_SKELETON, 6, EntityType.STRAY,
                7, EntityType.SKELETON);
    }

    public float getMaxSummonHealth() {
        if (this.isDay()) {
            if (this.isSecondPhase()) {
                return 200.0f;
            }
            return Mth.randomBetween(this.random, 50.0f, 100.0f);
        } else {
            if (this.isSecondPhase()) {
                return 500.0f;
            } else {
                return 300.0f;
            }
        }
    }

    private void addServantEffects(LivingEntity servant) {
        servant.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, -1, 1));
        servant.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, -1, 1));
        servant.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, -1, 1));
    }

    public boolean isSecondPhase() {
        return this.entityData.get(DATA_SECOND_PHASE);
    }

    public void setSecondPhase(boolean flag) {
        this.entityData.set(DATA_SECOND_PHASE, flag);
    }

    public float getMaxHealth(EntityType<? extends LivingEntity> type) {
        LivingEntity living = type.create(this.level());
        float d = 0.0f;
        if (living != null) {
            AttributeInstance instance = living.getAttribute(Attributes.MAX_HEALTH);
            if (instance != null) {
                d = (float)instance.getValue();
            }
        }
        return d;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 672).add(Attributes.ARMOR, 10)
                .add(Attributes.ATTACK_DAMAGE, 1).add(Attributes.ARMOR_TOUGHNESS, 8)
                .add(Attributes.MOVEMENT_SPEED, 0.23000054643535).add(Attributes.FOLLOW_RANGE, 64);
    }

    static {
        DATA_BOSS_FLAGS = SynchedEntityData.defineId(NamelessOne.class, EntityDataSerializers.INT);
        DATA_SECOND_PHASE = SynchedEntityData.defineId(NamelessOne.class, EntityDataSerializers.BOOLEAN);
        DATA_DAY = SynchedEntityData.defineId(NamelessOne.class, EntityDataSerializers.BOOLEAN);
    }

    private class CastingSpellGoal extends Goal {
        public CastingSpellGoal() {
            this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
        }

        @Override
        public void start() {
            NamelessOne.this.getNavigation().stop();
        }

        @Override
        public void tick() {
            LivingEntity target = NamelessOne.this.getTarget();
            if (target != null) {
                NamelessOne.this.getLookControl().setLookAt(target, NamelessOne.this.getMaxHeadYRot(),
                        NamelessOne.this.getMaxHeadXRot());
            }
        }

        @Override
        public boolean canUse() {
            return NamelessOne.this.isCastingSpell();
        }

        @Override
        public boolean canContinueToUse() {
            return NamelessOne.this.isCastingSpell();
        }
    }
}
