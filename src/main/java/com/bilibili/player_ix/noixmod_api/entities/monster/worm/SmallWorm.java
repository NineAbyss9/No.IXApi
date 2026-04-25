
package com.bilibili.player_ix.noixmod_api.entities.monster.worm;

import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiMeleeAttackGoal;
import com.bilibili.player_ix.noixmod_api.entities.servant.worm.AbstractWorm;
import com.bilibili.player_ix.noixmod_api.entities.servant.worm.Worm;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class SmallWorm
extends AbstractWorm {
    public SmallWorm(EntityType<? extends SmallWorm> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
        this.xpReward = this.isHostile() ? 1 : 0;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new ApiMeleeAttackGoal(this, 1, false, false));
        this.addBehaviorGoal(4, 0.75, 10f);
        this.addTargetGoal(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Worm.createBaseAttributes().add(Attributes.MAX_HEALTH, 10);
    }

    @Nullable
    public AbstractWorm getBreedMob() {
        return NoixmodAPIEntities.SMALL_WORM.get().create(this.level());
    }
}
