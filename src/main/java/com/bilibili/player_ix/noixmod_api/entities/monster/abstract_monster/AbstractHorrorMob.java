
package com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster;

import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiMeleeAttackGoal;
import com.bilibili.player_ix.noixmod_api.server.HorrorModeSavedData;
import com.github.NineAbyss9.ix_api.api.mobs.ApiPathfinderMob;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIDamageSource;
import com.github.NineAbyss9.ix_api.util.ParticleUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractHorrorMob extends ApiPathfinderMob implements Enemy {
    public AbstractHorrorMob(EntityType<? extends AbstractHorrorMob> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    protected void addAttackGoal() {
        this.goalSelector.addGoal(0, new ApiMeleeAttackGoal(this, 1.0D));
    }

    protected void addGoals(int i) {
        this.goalSelector.addGoal(i, new FloatGoal(this));
        this.goalSelector.addGoal(i, new RandomStrollGoal(this, 0.8D));
    }

    protected void targetGoals() {
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this,
                Player.class, false));
        this.targetSelector.addGoal(2, new HorrorHurtByTargetGoal(this, AbstractHorrorMob.class));
    }

    public int getLevel() {
        return 0;
    }

    public boolean canAttack(LivingEntity p_21171_) {
        if (p_21171_ instanceof AbstractHorrorMob) {
            return false;
        }
        return super.canAttack(p_21171_);
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (NoixmodAPIDamageSource.sourceEntity(pSource) instanceof AbstractHorrorMob) {
            return false;
        }
        return super.hurt(pSource, pAmount);
    }

    protected void actuallyHurt(DamageSource p_21240_, float p_21241_) {
        if (NoixmodAPIDamageSource.sourceEntity(p_21240_) instanceof AbstractHorrorMob) {
            return;
        }
        super.actuallyHurt(p_21240_, p_21241_);
    }

    public void die(int pIndex) {
        if (this.isServerSide()) {
            var ser = this.serverLevel();
            HorrorModeSavedData.getInstanceUnsafe().updateNextMobWillSpawn(pIndex);
            ParticleUtil.sendParticles(ser, ParticleTypes.LARGE_SMOKE, this.position(), 5,
                    0.15, 0.5, 0.15, 0.05);
        }
    }

    protected static class HorrorHurtByTargetGoal extends HurtByTargetGoal {
        public HorrorHurtByTargetGoal(PathfinderMob p_26039_, Class<?>... toIgnore) {
            super(p_26039_, toIgnore);
        }

        protected boolean canAttack(@Nullable LivingEntity p_26151_, TargetingConditions p_26152_) {
            if (p_26151_ instanceof AbstractHorrorMob) {
                return false;
            }
            return super.canAttack(p_26151_, p_26152_);
        }
    }
}
