
package com.bilibili.player_ix.noixmod_api.entities.monster.hostile;

import com.bilibili.player_ix.noixmod_api.entities.servant.DrownedServant;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;

public class HostileDrowned
extends DrownedServant
implements Enemy {
    public HostileDrowned(EntityType<HostileDrowned> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
        this.xpReward = 3;
    }

    public boolean isHostile() {
        return true;
    }

    public boolean wouldHaveOwner() {
        return false;
    }
}
