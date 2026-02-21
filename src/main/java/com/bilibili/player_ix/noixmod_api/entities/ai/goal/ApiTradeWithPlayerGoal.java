
package com.bilibili.player_ix.noixmod_api.entities.ai.goal;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.trading.Merchant;

import java.util.EnumSet;

public class ApiTradeWithPlayerGoal extends Goal {
    protected final Mob mob;
    protected final Merchant merchant;
    public ApiTradeWithPlayerGoal(Merchant p_25958_) {
        this.mob = (Mob)p_25958_;
        this.merchant = p_25958_;
        this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!this.mob.isAlive()) {
            return false;
        } else if (this.mob.isInWater()) {
            return false;
        } else if (!this.mob.onGround()) {
            return false;
        } else if (this.mob.hurtMarked) {
            return false;
        } else if (this.mob.getTarget() != null) {
            return false;
        } else {
            Player $$0 = this.merchant.getTradingPlayer();
            if ($$0 == null) {
                return false;
            } else return !(this.mob.distanceToSqr($$0) > 16.0);
        }
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse();
    }

    @Override
    public void start() {
        this.mob.getNavigation().stop();
    }

    @Override
    public void tick() {
        Player player = this.merchant.getTradingPlayer();
        if (player != null) {
            this.mob.getLookControl().setLookAt(player, 30, 30);
        }
    }

    @Override
    public void stop() {
        this.merchant.setTradingPlayer(null);
    }
}
