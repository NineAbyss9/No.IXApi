
package com.bilibili.player_ix.noixmod_api.entities.monster.hostile;

import com.bilibili.player_ix.noixmod_api.entities.servant.nihilistic.NihilisticBlaze;
import com.github.NineAbyss9.ix_api.api.mobs.MobUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;

public class HostileNihilisticBlaze
extends NihilisticBlaze
implements Enemy {
    public HostileNihilisticBlaze(EntityType<? extends NihilisticBlaze> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    protected void targetGoal() {
        this.targetSelector.addGoal(1, new MobUtils.HostileNearestAttackableTargetGoal(this, true));
    }

    public Component getName()
    {
        return Component.translatable("entity.noixmodapi.nihilistic_blaze");
    }

    public boolean isHostile() {
        return true;
    }
}
