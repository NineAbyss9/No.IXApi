
package com.bilibili.player_ix.noixmod_api.entities.monster.horror;

import com.github.NineAbyss9.ix_api.api.mobs.OwnableMob;
import com.github.NineAbyss9.ix_api.util.Maths;
import com.github.NineAbyss9.ix_api.util.ParticleUtil;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiMeleeAttackGoal;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.AbstractHorrorMob;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.NineAbyss9.math.MathSupport;

public class Detractor extends AbstractHorrorMob {
    public Detractor(EntityType<? extends AbstractHorrorMob> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new ApiMeleeAttackGoal(this, 1,
                Maths.square(1.7D)));
        OwnableMob.addBehaviorGoals(this, 4, 0.8D, 10F, true, true);
        this.targetSelector.addGoal(1, new HorrorHurtByTargetGoal(this,
                AbstractHorrorMob.class).setAlertOthers());
        this.targetSelector.addGoal(2, new MobUtils.HostileNearestAttackableTargetGoal(this, false));
    }

    public void tick() {
        super.tick();
        if (!this.level().isClientSide || MathSupport.random.nextBoolean()) {
            return;
        }
        ParticleUtil.addRedStoneParticle(this, this.getRandomX(0.5), this.getRandomY(),
                this.getRandomZ(0.5), 0, 0.2, 0);
    }

    public void aiStep() {
        super.aiStep();
        if (this.tickCount % 80 == 0) {
            this.heal(1F);
        }
    }

    public int getExperienceReward() {
        return 5;
    }
}
