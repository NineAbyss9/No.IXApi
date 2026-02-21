
package com.bilibili.player_ix.noixmod_api.entities.servant.nihilistic;

import com.github.NineAbyss9.ix_api.api.mobs.NihilitySummonedMobs;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class NihilisticEntity
extends NihilitySummonedMobs {
    public NihilisticEntity(EntityType<? extends NihilitySummonedMobs> e, Level l) {
        super(e, l);
    }

    public void aiStep() {
        super.aiStep();
        if (this.level().isClientSide) {
            clientLevel().addParticle(NoixmodAPIParticleTypes.NORMAL_SPELL.get(), getRandomX(0.8),
                    getRandomY(), getRandomZ(0.8), 0, 0, 0);
        }
    }
}
