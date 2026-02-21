
package com.bilibili.player_ix.noixmod_api.entities.ai.goal;

import com.github.NineAbyss9.ix_api.api.mobs.IAgeableMob;
import com.bilibili.player_ix.noixmod_api.entities.servant.animal.MushroomSpider;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;

public class APIBreedGoal
extends Goal {
    private static final TargetingConditions PARTNER_TARGETING = TargetingConditions.forNonCombat()
            .range(8.0).ignoreLineOfSight();
    protected final PathfinderMob animal;
    private final Class<? extends PathfinderMob> partnerClass;
    protected final Level level;
    @Nullable
    protected PathfinderMob partner;
    private int loveTime;
    private final double speedModifier;

    public APIBreedGoal(PathfinderMob p_25122_, double p_25123_) {
        this(p_25122_, p_25123_, p_25122_.getClass());
    }

    public APIBreedGoal(PathfinderMob p_25125_, double p_25126_, Class<? extends PathfinderMob> p_25127_) {
        this.animal = p_25125_;
        this.level = p_25125_.level();
        this.partnerClass = p_25127_;
        this.speedModifier = p_25126_;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    public boolean canUse() {
        if (this.animal instanceof MushroomSpider spider) {
            if (this.checkTarget() && spider.isInLove()) {
                this.partner = this.getFreePartner();
                return true;
            }
            return false;
        } else {
            if (!((IAgeableMob) this.animal).isInLove()) {
                return false;
            } else {
                this.partner = this.getFreePartner();
                return this.checkTarget();
            }
        }
    }

    public boolean checkTarget() {
        return this.animal.getTarget() == null;
    }

    public boolean canContinueToUse() {
        return this.partner != null && this.partner.isAlive() && this.loveTime < 60 && this.checkTarget();
    }

    public void stop() {
        ((IAgeableMob)this.animal).resetLove();
        if (this.partner != null) {
            ((IAgeableMob)this.partner).resetLove();
            this.partner = null;
        }
        this.loveTime = 0;
    }

    public void tick() {
        if (this.partner != null) {
            this.animal.getLookControl().setLookAt(this.partner, 10.0F, (float)this.animal.getMaxHeadXRot());
            this.animal.getNavigation().moveTo(this.partner, this.speedModifier);
        }
        ++this.loveTime;
        if (this.loveTime >= this.adjustedTickDelay(60) && this.animal.distanceToSqr(this.partner) < 9.0) {
            this.breed();
        }
    }

    @Nullable
    private PathfinderMob getFreePartner() {
        List<? extends PathfinderMob> $$0 = this.level.getNearbyEntities(this.partnerClass, PARTNER_TARGETING,
                this.animal, this.animal.getBoundingBox().inflate(8.0));
        double $$1 = Double.MAX_VALUE;
        PathfinderMob $$2 = null;
        for (PathfinderMob $$3 : $$0) {
            if (this.canMate((IAgeableMob)$$3, (IAgeableMob)this.animal) && this.animal.distanceToSqr($$3) < $$1) {
                $$2 = $$3;
                $$1 = this.animal.distanceToSqr($$3);
            }
        }
        return $$2;
    }

    protected void breed() {
        if (this.partner != null) {
            ((IAgeableMob)this.animal).spawnChildFromBreeding((ServerLevel)this.level, (IAgeableMob)this.partner);
        }
    }

    public boolean canMate(IAgeableMob p_27569_, IAgeableMob pMob) {
        if (p_27569_ == pMob) {
            return false;
        } else if (p_27569_.getClass() != pMob.getClass()) {
            return false;
        } else {
            return p_27569_.isInLove() && pMob.isInLove();
        }
    }
}
