
package com.bilibili.player_ix.noixmod_api.entities.servant;

import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiMeleeAttackGoal;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiRangedBowAttackGoal;
import com.bilibili.player_ix.noixmod_api.entities.servant.core.AbstractSkeletonServant;
import com.bilibili.player_ix.noixmod_api.util.OwnerSummon;
import com.github.NineAbyss9.ix_api.util.Maths;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.concurrent.ThreadLocalRandom;

public class SkeletonServant
extends AbstractSkeletonServant
{
    private final OwnerSummon ownerSummon = new OwnerSummon(this);
    public SkeletonServant(EntityType<? extends SkeletonServant> p_21683_, Level p_21684_)
    {
        super(p_21683_, p_21684_);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new ApiRangedBowAttackGoal(this, 0.6,
                10, 20F));
        this.goalSelector.addGoal(2, new AttackGoal(this, 1.0D));
        this.addTargetGoal();
        this.addBehaviorGoal(4, 0.6, 10F);
        this.goalSelector.addGoal(3, new FollowOwnerGoal<>(this, 1, 30F,
                10F, false));
        super.registerGoals();
    }

    public boolean burnInSun() {
        return false;
    }

    public void performRangedAttack(LivingEntity livingEntity, float v) {
        AbstractArrow arrow = this.getArrow(this.getProjectile(this.getItemInHand(ProjectileUtil
                .getWeaponHoldingHand(this, item -> item instanceof BowItem))), v);
        double x = ownerSummon.projectileDouble(livingEntity)[0];
        double y = ownerSummon.projectileDouble(livingEntity)[1];
        double z = ownerSummon.projectileDouble(livingEntity)[2];
        arrow.shoot(x, y, z, 2F, 0.8F);
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0f, 1.0f / (ThreadLocalRandom.current()
                .nextFloat() * 0.4f + 0.8f));
        this.level().addFreshEntity(arrow);
    }

    protected void populateDefaultItems()
    {
        this.setMainHandItem(Items.BOW);
    }

    public static class AttackGoal extends ApiMeleeAttackGoal
    {
        public AttackGoal(PathfinderMob finder, double speed) {
            super(finder, speed, Maths.square(2.5));
        }

        public boolean canUse() {
            if (this.checkItem()) {
                return false;
            }
            return super.canUse();
        }

        public boolean canContinueToUse() {
            if (this.checkItem()) {
                return false;
            }
            return super.canContinueToUse();
        }

        private boolean checkItem() {
            return this.mob.getMainHandItem().is(Items.BOW);
        }
    }
}
