
package com.bilibili.player_ix.noixmod_api.entities.servant.ice;

import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiRangedBowAttackGoal;
import com.bilibili.player_ix.noixmod_api.entities.servant.core.AbstractSkeletonServant;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class StrayServant
extends AbstractSkeletonServant
{
    public StrayServant(EntityType<? extends StrayServant> p_21683_, Level p_21684_)
    {
        super(p_21683_, p_21684_);
    }

    protected void populateDefaultItems()
    {
        this.setMainHandItem(Items.BOW);
    }

    protected void registerGoals()
    {
        this.goalSelector.addGoal(1, new ApiRangedBowAttackGoal(this, 0.6,
                10, 20F));
        this.addTargetGoal();
        this.addBehaviorGoal(4, 0.6, 10F);
        this.goalSelector.addGoal(3, new FollowOwnerGoal<>(this, 1, 30F,
                10F, false));
        super.registerGoals();
    }

    public AbstractArrow getArrow(ItemStack stack, float pDistanceFactor)
    {
        Arrow arrow = (Arrow)super.getArrow(stack, pDistanceFactor);
        arrow.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 600));
        return arrow;
    }
}
