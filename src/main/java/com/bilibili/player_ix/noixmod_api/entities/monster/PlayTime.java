
package com.bilibili.player_ix.noixmod_api.entities.monster;

import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.BasicMob;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class PlayTime extends BasicMob {
    public PlayTime(EntityType<? extends PlayTime> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
        this.xpReward = 10;
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        return false;
    }

    protected void actuallyHurt(DamageSource p_21240_, float p_21241_) {
    }
}
