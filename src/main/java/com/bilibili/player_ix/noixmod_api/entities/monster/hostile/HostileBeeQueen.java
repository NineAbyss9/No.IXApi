
package com.bilibili.player_ix.noixmod_api.entities.monster.hostile;

import com.github.NineAbyss9.ix_api.api.mobs.ApiBoss;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.BeeQueen;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class HostileBeeQueen
extends BeeQueen
implements ApiBoss {
    private final ServerBossEvent bossEvent = new ServerBossEvent(this.getDisplayName(),
                    BossEvent.BossBarColor.YELLOW, BossEvent.BossBarOverlay.PROGRESS);
    public HostileBeeQueen(EntityType<? extends BeeQueen> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
        this.xpReward = XP_REWARD_HUGE;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
    }

    @Override
    public boolean isHostile() {
        return true;
    }

    @Override
    public boolean wouldHaveOwner() {
        return false;
    }
}
