
package com.bilibili.player_ix.noixmod_api.entities.monster.hostile;

import com.bilibili.player_ix.noixmod_api.entities.servant.ice.Yeti;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;

public class HostileYeti
extends Yeti
implements Enemy {
    public HostileYeti(EntityType<? extends Yeti> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 10;
        this.setHostile(true);
    }

    public Component getName() {
        return Component.translatable("entity.noixmodapi.yeti");
    }
}
