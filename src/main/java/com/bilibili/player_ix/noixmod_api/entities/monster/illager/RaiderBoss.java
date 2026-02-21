
package com.bilibili.player_ix.noixmod_api.entities.monster.illager;

import com.github.NineAbyss9.ix_api.ix_api.api.mobs.ApiIllagerBoss;
import com.github.NineAbyss9.ix_api.ix_api.api.mobs.OwnableMob;
import com.github.NineAbyss9.ix_api.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiMeleeAttackGoal;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.ApiSpellcaster;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;

public class RaiderBoss
extends ApiSpellcaster
implements ApiIllagerBoss {
    public RaiderBoss(EntityType<? extends RaiderBoss> p_32105_, Level p_32106_) {
        super(p_32105_, p_32106_);
        this.xpReward = XP_REWARD_BOSS;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(2, new ApiMeleeAttackGoal(this, 1, Maths.square(3)));
        OwnableMob.addBehaviorGoals(this, 4, 0.7, 10F, true, false);
        this.targetSelector.addGoal(1, new MobUtils.HostileNearestAttackableTargetGoal(this, false));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, Raider.class).setAlertOthers());
        super.registerGoals();
    }

    public SoundEvent getCelebrateSound() {
        return SoundEvents.VINDICATOR_CELEBRATE;
    }
}
