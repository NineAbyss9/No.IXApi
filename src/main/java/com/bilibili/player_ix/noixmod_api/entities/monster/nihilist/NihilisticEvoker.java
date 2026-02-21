
package com.bilibili.player_ix.noixmod_api.entities.monster.nihilist;

import com.github.NineAbyss9.ix_api.ix_api.api.ApiSpells;
import com.github.NineAbyss9.ix_api.ix_api.api.mobs.Ownable;
import com.github.NineAbyss9.ix_api.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.Nihilist;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.SpellcasterNihilist;
import com.bilibili.player_ix.noixmod_api.entities.servant.nihilistic.NihilisticServant;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NihilisticEvoker
extends SpellcasterNihilist
implements Enemy {
    public int healTicks;
    public NihilisticEvoker(EntityType<? extends SpellcasterNihilist> type, Level world) {
        super(type, world);
        this.xpReward = 10;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new CastingSpellGoal());
        this.goalSelector.addGoal(1, new SummonSpellGoal());
        this.goalSelector.addGoal(1, new HealSelfGoal());
        this.goalSelector.addGoal(1, new AttackSpellGoal());
        this.goalSelector.addGoal(1, new HealCompanionSpellGoal());
        this.goalSelector.addGoal(4, new NihilisticAvoidGoal(this));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, LivingEntity.class, 10f));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 0.75));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(3, new MobUtils.HostileNearestAttackableTargetGoal(this, false));
        super.registerGoals();
    }

    public void tick() {
        super.tick();
        if (this.healTicks > 0) {
            --this.healTicks;
            if (this.healTicks == 40) {
                this.heal(3f);
            }
            if (this.healTicks == 20) {
                this.heal(4f);
            }
        }
    }


    public boolean addEffect(MobEffectInstance p_147208_, @Nullable Entity p_147209_) {
        return false;
    }

    protected SoundEvent getCastingSoundEvent() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return SoundEvents.EVOKER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_33034_) {
        return SoundEvents.EVOKER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.EVOKER_DEATH;
    }

    private void makeParticle(Entity entity, ParticleOptions options) {
        double d0 = random.nextGaussian() * 0.02;
        if (level().isClientSide) {
            for(int i = 0; i < 20; ++i) {
                double d1 = random.nextGaussian() * 0.02;
                double d2 = random.nextGaussian() * 0.02;
                double d3 = 10.0;
                level().addParticle(options, entity.getX(1.0) - d0 * d3,
                        entity.getRandomY() - d1 * d3, entity.getRandomZ(1.0)
                                - d2 * d3, d0, d1, d2);
            }
        } else {
            AABB aabb = entity.getBoundingBox();
            ((ServerLevel)this.level()).sendParticles(options, entity.getX(), entity.getY(), entity.getZ(), 20,
                    aabb.getXsize(), aabb.getYsize(), aabb.getZsize(), d0);
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return NihilisticEvoker.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 100)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.MAX_HEALTH, 60).add(Attributes.ATTACK_DAMAGE, 5)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.6)
                .add(Attributes.ARMOR, 10);
    }

    private class AttackSpellGoal
    extends UseSpellGoalA {

        protected void castSpell() {
            LivingEntity lie = NihilisticEvoker.this.getTarget();
            if (lie != null) {
                for (int i = 0; i < 4; ++i) {
                    Level level = NihilisticEvoker.this.level();
                    EvokerFangs fangs = new EvokerFangs(EntityType.EVOKER_FANGS, level);
                    fangs.moveTo(lie.getRandomX(1.5), lie.getY(), lie.getRandomZ(1.5));
                    fangs.setOwner(NihilisticEvoker.this);
                    level.addFreshEntity(fangs);
                }
                Level level = NihilisticEvoker.this.level();
                EvokerFangs fangs = new EvokerFangs(EntityType.EVOKER_FANGS, level);
                fangs.setOwner(NihilisticEvoker.this);
                fangs.moveTo(lie.getRandomX(0.5), lie.getY(), lie.getRandomZ(0.5));
                level.addFreshEntity(fangs);
            }
            NihilisticEvoker.this.makeGroundParticle();
            MobUtils.push(4, 0.2, 4, NihilisticEvoker.this, 0, 1, 0);
            MobUtils.rangeHurt(4, 0.2, 4, NihilisticEvoker.this,
                    NihilisticEvoker.this.damageSources().indirectMagic(NihilisticEvoker
                            .this, null), 6f);
        }

        @Override
        protected int getCastingTime() {
            return 40;
        }

        @Override
        public void tick() {
            super.tick();
        }

        @Override
        protected int getCastWarmupTime() {
            return 25;
        }

        @Override
        protected int getCastingInterval() {
            return 100;
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_ATTACK;
        }

        @Override
        protected ApiSpells.ApiSpell getSpell() {
            return ApiSpells.ApiSpell.DARK;
        }
    }

    private class SummonSpellGoal
    extends UseSpellGoalA {

        @Override
        protected void castSpell() {
            for (int i = 0;i < Maths.random.nextInt(4) + 2;++i) {
                if (NihilisticEvoker.this.level() instanceof ServerLevel level) {
                    NihilisticServant servant = NoixmodAPIEntities.NIHILISTIC_SERVANT.get().create(level);
                    if (servant != null) {
                        BlockPos.MutableBlockPos pos = NihilisticEvoker.this.blockPosition().offset(Maths.randomInteger(3),
                                0, Maths.randomInteger(3)).below().mutable();
                        servant.moveTo(pos, 0, 0);
                        servant.setOwner(NihilisticEvoker.this);
                        servant.finalizeSpawn(level, level.getCurrentDifficultyAt(servant.blockPosition()),
                                MobSpawnType.MOB_SUMMONED);
                        level.addFreshEntity(servant);
                        servant.spawnAnim();
                    }
                }
            }
        }

        @Override
        protected int getCastingTime() {
            return 60;
        }

        @Override
        protected int getCastingInterval() {
            return 400;
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_SUMMON;
        }

        @Override
        protected ApiSpells.ApiSpell getSpell() {
            return ApiSpells.ApiSpell.NIHILISTIC;
        }
    }

    private class HealSelfGoal
    extends UseSpellGoalA {
        public HealSelfGoal() {
        }

        @Override
        protected void castSpell() {
            NihilisticEvoker.this.heal(2f);
            NihilisticEvoker.this.healTicks = 60;
            makeParticle(NihilisticEvoker.this, ParticleTypes.TOTEM_OF_UNDYING);
        }

        @Override
        protected int getCastingTime() {
            return 20;
        }

        @Override
        protected int getCastingInterval() {
            return 500;
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.BELL_RESONATE;
        }

        @Override
        protected ApiSpells.ApiSpell getSpell() {
            return ApiSpells.ApiSpell.REGEN;
        }

        @Override
        public boolean canUse() {
            if (NihilisticEvoker.this.getHealth() >= NihilisticEvoker.this.getMaxHealth() - 5) {
                return false;
            }
            return super.canUse();
        }

        @Override
        protected boolean needTarget() {
            return false;
        }
    }

    private class HealCompanionSpellGoal
    extends UseSpellGoalA {
        @Override
        protected void castSpell() {
            List<Nihilist> list = NihilisticEvoker.this.level().getEntitiesOfClass(Nihilist.class,
                    NihilisticEvoker.this.getBoundingBox().inflate(16));
            for (Nihilist nihilist : list) {
                if (nihilist instanceof Ownable ownable) {
                    if (ownable.isHostile()) {
                        nihilist.heal(6f);
                        makeParticle(nihilist, ParticleTypes.HAPPY_VILLAGER);
                    }
                } else {
                    nihilist.heal(6f);
                    makeParticle(nihilist, ParticleTypes.HAPPY_VILLAGER);
                }
            }
        }

        @Override
        protected int getCastingTime() {
            return 30;
        }

        @Override
        protected int getCastingInterval() {
            return 600;
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.BELL_RESONATE;
        }

        @Override
        protected ApiSpells.ApiSpell getSpell() {
            return ApiSpells.ApiSpell.DARK;
        }

        @Override
        public boolean canUse() {
            return super.canUse();
        }
    }
}
