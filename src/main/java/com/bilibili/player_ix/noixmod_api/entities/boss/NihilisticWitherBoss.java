
package com.bilibili.player_ix.noixmod_api.entities.boss;

import com.github.NineAbyss9.ix_api.api.mobs.ApiNihilisticBoss;
import com.bilibili.player_ix.noixmod_api.entities.servant.nihilistic.NihilisticWither;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.level.Level;

public class NihilisticWitherBoss
extends NihilisticWither
implements ApiNihilisticBoss {
    public NihilisticWitherBoss(EntityType<NihilisticWitherBoss> type, Level level) {
        super(type, level);
    }

    public boolean isBoss() {
        return true;
    }

    public boolean isHostile() {
        return true;
    }

    public boolean wouldHaveOwner() {
        return false;
    }

    protected void addTargetGoals() {
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class,
                true, this::canAttack));
    }
}
