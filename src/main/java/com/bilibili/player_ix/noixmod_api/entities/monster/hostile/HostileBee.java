
package com.bilibili.player_ix.noixmod_api.entities.monster.hostile;

import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.AbstractBee;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;

public class HostileBee
extends AbstractBee
implements Enemy {
    public HostileBee(EntityType<? extends AbstractBee> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
        this.xpReward = 2;
    }

    public boolean isHostile() {
        return true;
    }
}
