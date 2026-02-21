
package com.bilibili.player_ix.noixmod_api.entities.servant.worm;

import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiMeleeAttackGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;

public class WormKing
extends AbstractWorm {
    public WormKing(EntityType<? extends AbstractWorm> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new ApiMeleeAttackGoal(this, 1, false, false));
        this.addBehaviorGoal(3, 0.8, 14f);
        this.addTargetGoal();
    }

    public void tick() {
        super.tick();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return WormKing.createPathAttributes().add(Attributes.ATTACK_DAMAGE, 9)
                .add(Attributes.ARMOR, 4)
                .add(Attributes.FOLLOW_RANGE, 120).add(Attributes.MOVEMENT_SPEED, 0.35)
                .add(Attributes.MAX_HEALTH, 160);
    }

    private class WormKingGoal
    extends Goal {
        private int cooldown;
        public WormKingGoal() {
        }

        public void start() {
            super.start();
            this.cooldown = WormKing.this.tickCount + this.getCooldown();
        }

        public boolean canUse() {
            return this.cooldown <= 0;
        }

        public int getCooldown() {
            return 0;
        }
    }
}
