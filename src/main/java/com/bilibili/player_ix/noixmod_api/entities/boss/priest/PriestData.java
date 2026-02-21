
package com.bilibili.player_ix.noixmod_api.entities.boss.priest;

import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;

class PriestData {
    private final Priest priest;
    PriestData(Priest pPriest) {
        priest = pPriest;
    }

    @Nullable
    LivingEntity getTarget() {
        return priest.getTarget();
    }

    public boolean closeThan(Entity living, double range)  {
        return priest.closerThan(living, range);
    }

    boolean canAttack() {
        if (this.getTarget() == null)
            return false;
        else
            return this.closeThan(this.getTarget(), 4);
    }

    boolean isHalfHealth() {
        return MobUtils.isHalfHealth(priest);
    }
}
