
package com.bilibili.player_ix.noixmod_api.entities.servant.end;

import com.github.NineAbyss9.ix_api.ix_api.api.mobs.OwnableMob;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public abstract class AbstractEndServant
extends OwnableMob {
    public AbstractEndServant(EntityType<? extends AbstractEndServant> entityType, Level level) {
        super(entityType, level);
    }

    @Nullable
    public ParticleOptions getAmbientParticle() {
        return null;
    }
}
