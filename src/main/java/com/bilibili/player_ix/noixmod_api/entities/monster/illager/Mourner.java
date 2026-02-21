
package com.bilibili.player_ix.noixmod_api.entities.monster.illager;

import com.github.NineAbyss9.ix_api.api.mobs.OwnableMob;
import com.github.NineAbyss9.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiAvoidTargetGoal;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.ApiSpellcaster;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPISounds;
import com.bilibili.player_ix.noixmod_api.util.EntityEventHandler;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import com.bilibili.player_ix.noixmod_api.util.OwnerSummon;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Mourner extends ApiSpellcaster {
    @Nullable
    private Sheep wlTarget;
    private int dead = 0;
    private int damageSpellCooldown;
    private final OwnerSummon ownerSummon = new OwnerSummon(this);
    public Mourner(EntityType<? extends Mourner> p_32105_, Level p_32106_) {
        super(p_32105_, p_32106_);
        this.xpReward = 15;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new MournerCastingSpellGoal());
        this.goalSelector.addGoal(1, new SummonGoal());
        this.goalSelector.addGoal(1, new AttackGoal());
        this.goalSelector.addGoal(2, new WololoSpellGoal());
        this.goalSelector.addGoal(3, new ApiAvoidTargetGoal(this, Maths.square(2.5F),
                0.6, 1));
        OwnableMob.addBehaviorGoals(this, 4, 0.8, 7F, true, true);
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, Raider.class).setAlertOthers());
        this.targetSelector.addGoal(2, new MobUtils.HostileNearestAttackableTargetGoal(this, true));
    }

    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            if (this.random.nextBoolean()) {
                this.level().addParticle(ParticleTypes.FALLING_WATER, this.getRandomX(0.5),
                        this.getY() + 1.5, this.getRandomZ(0.5), 0, 0, 0);
            }
        }
        if (this.damageSpellCooldown > 0) {
            this.damageSpellCooldown--;
        }
    }

    @Nullable
    public Sheep getWlTarget() {
        return wlTarget;
    }

    public void setWlTarget(@Nullable Sheep wlTarget) {
        this.wlTarget = wlTarget;
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return NoixmodAPISounds.CRY.get();
    }

    protected SoundEvent getHurtSound(DamageSource p_33034_) {
        return SoundEvents.VILLAGER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.VILLAGER_DEATH;
    }

    public SoundEvent getCelebrateSound() {
        return SoundEvents.EMPTY;
    }

    protected float getSoundVolume() {
        return 0.7F;
    }

    public int getDead() {
        return this.dead;
    }

    public void setDead(int count) {
        this.dead = Math.min(NoixmodAPIMainConfig.MournerDamage.get().intValue(), count);
    }

    public void setDeadPlus() {
        if (this.dead < NoixmodAPIMainConfig.MournerDamage.get().intValue()) {
            ++this.dead;
        }
    }

    public void addAdditionalSaveData(CompoundTag p_37870_) {
        p_37870_.putInt("Dead", this.getDead());
        super.addAdditionalSaveData(p_37870_);
    }

    public void readAdditionalSaveData(CompoundTag p_37862_) {
        if (p_37862_.contains("Dead")) {
            this.setDead(p_37862_.getInt("Dead"));
        }
        super.readAdditionalSaveData(p_37862_);
    }

    public static void init() {
        Raid.RaiderType.create("mourner", NoixmodAPIEntities.MOURNER.get(), new int[] {
            0, 0, 0, 0, 3, 2, 1, 1
        });
    }

    //Damage like warden
    private class AttackGoal extends UseSpellGoal {

        public boolean canUse() {
            if (Mourner.this.damageSpellCooldown > 0) {
                return false;
            }
            return super.canUse();
        }

        protected void castSpell() {
            if (!Mourner.this.level().isClientSide) {
                EntityEventHandler.broadcastEntityEvent(Mourner.this, 5);
            }
        }

        protected int getCastingTime() {
            return 20;
        }

        protected int getCastingInterval() {
            return Maths.toTick(5);
        }

        @Nullable
        protected SoundEvent getPrepareSound() {
            return SoundEvents.BELL_RESONATE;
        }

        protected IllagerSpellType getSpellType() {
            return IllagerSpellType.ATTACK;
        }
    }

    private class SummonGoal extends UseSpellGoal {

        protected void castSpell() {
            for (int dead = 0; dead < Mourner.this.getDead(); ++dead) {
                DeadIllagerSkull skull = new DeadIllagerSkull(NoixmodAPIEntities.DEAD_ILLAGER_SKULL.get(),
                        Mourner.this.level());
                skull.setBoundOrigin(Mourner.this.blockPosition());
                Mourner.this.ownerSummon.integerSummon(skull, 4);
                Mourner.this.damageSpellCooldown = 60;
            }
        }

        protected int getCastingTime() {
            return 60;
        }

        protected int getCastWarmupTime() {
            return 2 * Mourner.this.getDead();
        }

        protected int getCastingInterval() {
            return 0;
        }

        public boolean canUse() {
            List<DeadIllagerSkull> skulls = Mourner.this.level().getEntitiesOfClass(DeadIllagerSkull.class,
                    Mourner.this.getBoundingBox().inflate(32));
            if (!skulls.isEmpty()) {
                return false;
            }
            if (Mourner.this.getDead() <= 0) {
                return false;
            }
            return super.canUse();
        }

        @Nullable
        protected SoundEvent getPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_SUMMON;
        }

        protected IllagerSpellType getSpellType() {
            return IllagerSpellType.RANGE;
        }
    }

    private class WololoSpellGoal extends UseSpellGoal {
        private final TargetingConditions wololoTargeting = TargetingConditions.forNonCombat().range(16.0)
                .selector((entity) -> ((Sheep) entity).getColor() == DyeColor.BLUE);

        public boolean canUse() {
            if (Mourner.this.getTarget() != null) {
                return false;
            } else if (Mourner.this.tickCount < this.nextAttackTickCount) {
                return false;
            } else if (Mourner.this.isCastingSpell()) {
                return false;
            } else {
                List<Sheep> sheep = Mourner.this.level().getNearbyEntities(Sheep.class, this.wololoTargeting,
                        Mourner.this, Mourner.this.getBoundingBox()
                        .inflate(16, 4, 16));
                if (sheep.isEmpty()) {
                    return false;
                } else {
                    Mourner.this.setWlTarget(sheep.get(Mourner.this.getRandom().nextInt(sheep.size())));
                    return true;
                }
            }
        }

        public boolean canContinueToUse() {
            return Mourner.this.getWlTarget() != null && this.attackWarmupDelay > 0;
        }

        public void stop() {
            super.stop();
            Mourner.this.setWlTarget(null);
        }

        protected void castSpell() {
            Sheep sheep = Mourner.this.getWlTarget();
            if (sheep != null && sheep.isAlive()) {
                sheep.setColor(DyeColor.RED);
            }
        }

        protected int getCastingTime() {
            return 30;
        }

        protected int getCastingInterval() {
            return 400;
        }

        @Nullable
        protected SoundEvent getPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_WOLOLO;
        }

        protected IllagerSpellType getSpellType() {
            return IllagerSpellType.ATTACK;
        }
    }

    private class MournerCastingSpellGoal extends CastingSpellGoal {
        public void tick() {
            LivingEntity target = Mourner.this.getTarget();
            Sheep sheep = Mourner.this.getWlTarget();
            if (target != null) {
                Mourner.this.lookControl.setLookAt(target, Mourner.this.getMaxHeadYRot(), Mourner.this.getMaxHeadXRot());
            } else if (sheep != null) {
                Mourner.this.getLookControl().setLookAt(sheep, Mourner.this.getMaxHeadYRot(),
                        Mourner.this.getMaxHeadXRot());
            }
        }
    }
}
