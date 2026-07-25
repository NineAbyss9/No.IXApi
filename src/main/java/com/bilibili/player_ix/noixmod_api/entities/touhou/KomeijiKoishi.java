
package com.bilibili.player_ix.noixmod_api.entities.touhou;

import com.bilibili.player_ix.noixmod_api.world.HorrorModeManager;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class KomeijiKoishi
extends AbstractTouhouEntity
{
    public KomeijiKoishi(EntityType<? extends KomeijiKoishi> type, Level level)
    {
        super(type, level);
    }

    public boolean isHorror() {
        return HorrorModeManager.horrorModeEnabled();
    }

    /// Except
    public boolean canBeSeenByAnyone() {return false;}
}
