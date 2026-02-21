
package com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;

public abstract class EnemyWormIllager
extends AbstractWormIllager
implements Enemy {
    public EnemyWormIllager(EntityType<? extends EnemyWormIllager> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }
}
