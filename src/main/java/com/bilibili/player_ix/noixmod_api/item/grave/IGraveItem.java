
package com.bilibili.player_ix.noixmod_api.item.grave;

import com.bilibili.player_ix.noixmod_api.entities.monster.silent.SilentGhost;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public interface IGraveItem
{
    default void spawnSilentGhost(Level pLevel, Vec3 pPos) {
        var ghost = new SilentGhost(NoixmodAPIEntities.SILENT_GHOST.get(), pLevel);
        ghost.moveTo(pPos);
        if (!pLevel.addFreshEntity(ghost))
            ghost.discard();
    }
}