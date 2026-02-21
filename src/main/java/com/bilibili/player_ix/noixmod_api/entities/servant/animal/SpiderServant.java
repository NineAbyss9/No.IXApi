
package com.bilibili.player_ix.noixmod_api.entities.servant.animal;

import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiOwnerTargetGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class SpiderServant
extends AgeableAnimalServant {
    public SpiderServant(EntityType<? extends SpiderServant> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
    }

    @Override
    protected void registerGoals() {
        addBehaviorGoal(5, 0.5, 10f);
        targetSelector.addGoal(0, new ApiOwnerTargetGoal(this));
        targetSelector.addGoal(0, new OwnerHurtTargetGoal<>(this));
        targetSelector.addGoal(1, new OwnableTargetGoal<>(this, false));
    }


}
