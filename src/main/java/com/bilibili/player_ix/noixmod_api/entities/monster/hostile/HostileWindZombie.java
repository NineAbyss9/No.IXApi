
package com.bilibili.player_ix.noixmod_api.entities.monster.hostile;

import com.bilibili.player_ix.noixmod_api.entities.servant.WindZombie;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class HostileWindZombie
extends WindZombie {
    public HostileWindZombie(EntityType<? extends WindZombie> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
    }

    public boolean isHostile() {
        return true;
    }
}
