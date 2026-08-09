
package com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster;

import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIItems;
import com.bilibili.player_ix.noixmod_api.util.OwnerSummon;
import com.github.NineAbyss9.ix_api.api.item.ItemStacks;
import com.github.NineAbyss9.ix_api.api.mobs.ApiMobType;
import com.github.NineAbyss9.ix_api.api.mobs.ApiPathfinderMob;
import com.github.NineAbyss9.ix_api.api.mobs.Nihilistic;
import com.github.NineAbyss9.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.api.entity.IX;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import com.bilibili.player_ix.noixmod_api.util.WorldUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public abstract class Nihilist
extends ApiPathfinderMob
implements PowerableMob, Nihilistic
{
    private final OwnerSummon summon = new OwnerSummon(this);
    protected Nihilist(EntityType<? extends Nihilist> type, Level supered) {
        super(type, supered);
    }

    public boolean canAttack(LivingEntity lie) {
        if (!MobUtils.canHurt(lie, this)) {
            return false;
        }
        if (lie instanceof IX) {
            return false;
        }
        return super.canAttack(lie);
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        Entity entity = pSource.getEntity();
        if (!MobUtils.canHurt(this, entity)) {
            return false;
        }
        if (pSource.getEntity() instanceof Nihilistic) {
            return false;
        }
        return super.hurt(pSource, pAmount);
    }

    public boolean isPowered() {
        return false;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new LookAtLordGoal(this));
    }

    public enum NihilistArmPose {
        ATTACKING,
        BOW_AND_ARROW,
        CROSSBOW_HOLD,
        CROSSBOW_CHARGE,
        CROSSED,
        DIE,
        NONE,
        SPELL_CASTING,
        SPELL_AND_WEAPON,
        THROWING,
        ROAR,
        ZOMBIE_ATTACKING
    }

    public EntityDimensions getDimensions(Pose pose) {
        return super.getDimensions(pose).scale(0.9f);
    }

    public NihilistArmPose getArmPose() {
        if (!this.getLord().isEmpty()) {
            return NihilistArmPose.SPELL_CASTING;
        }
        return NihilistArmPose.CROSSED;
    }

    public boolean requiresCustomPersistence() {
        return this.getSpawnType() == MobSpawnType.STRUCTURE || super.requiresCustomPersistence();
    }

    public boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
        if (this.getSpawnType() == MobSpawnType.EVENT) return false;
        return super.removeWhenFarAway(pDistanceToClosestPlayer);
    }

    public OwnerSummon getSummon() {
        return summon;
    }

    public List<LivingEntity> getLord() {
        return this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(32),
                entity -> entity instanceof IX);
    }

    public boolean canBeLeader() {
        return !this.isAlive();
    }

    public MobType getMobType() {
        return ApiMobType.NIHILISTIC;
    }

    public void makeGroundParticle() {
        if (!this.level().isClientSide) {
            WorldUtil.sendParticles(ParticleTypes.WITCH, this, 12, 2, 0, 2, 0);
        }
    }

    protected void dropCustomDeathLoot(DamageSource pSource, int pLooting, boolean pRecentlyHit) {
        this.spawnAtLocation(ItemStacks.ofRanged(NoixmodAPIItems.NIHILISTIC_ASH.get(), 3));
        super.dropCustomDeathLoot(pSource, pLooting, pRecentlyHit);
    }

    public boolean isHorror() {
        return NoixmodAPIMainConfig.HorrorMode.get();
    }

    public static AttributeSupplier.Builder createMonsterAttributes() {
        return ApiPathfinderMob.createPathAttributes();
    }

    public static class NihilistAvoidGoal
    extends AvoidEntityGoal<LivingEntity> {
        public NihilistAvoidGoal(PathfinderMob mob) {
            this(mob, Maths.square(3f), 0.6, 1);
        }

        public NihilistAvoidGoal(PathfinderMob p_25027_, float p_25029_, double p_25030_, double p_25031_) {
            this(p_25027_, p_25029_, p_25030_, p_25031_, lie -> lie instanceof LivingEntity && EntitySelector
                    .NO_CREATIVE_OR_SPECTATOR.test(lie));
        }

        public NihilistAvoidGoal(PathfinderMob p_25033_, float p_25035_, double p_25036_, double p_25037_,
                                 Predicate<LivingEntity> p_25038_) {
            super(p_25033_, LivingEntity.class, living -> living instanceof LivingEntity && EntitySelector
                    .NO_CREATIVE_OR_SPECTATOR.test(living), p_25035_, p_25036_, p_25037_, p_25038_);
        }

        public boolean canUse() {
            if (this.toAvoid instanceof Mob target) {
                if (target.getTarget() == this.mob) {
                    return super.canUse();
                }
                return false;
            } else return super.canUse() && this.toAvoid instanceof Player;
        }
    }

    public static class LookAtLordGoal extends Goal {
        protected final Nihilist nihilist;
        protected LivingEntity lord;//1.4.4 : improve performance
        public LookAtLordGoal(Nihilist nihilistIn) {
            this.nihilist = nihilistIn;
            this.setFlags(EnumSet.of(Flag.LOOK));
        }

        public void tick() {
            this.nihilist.getLookControl().setLookAt(this.lord, 100F, 30F);
        }

        public boolean canUse() {
            if (this.nihilist.getTarget() != null) {
                return false;
            }
            if (this.nihilist.getLord().isEmpty()) {
                return false;
            }
            this.lord = this.nihilist.getLord().get(0);
            return true;
        }

        public boolean canContinueToUse() {
            return this.nihilist.getTarget() != null && !this.nihilist.getLord().isEmpty();
        }
    }
}
