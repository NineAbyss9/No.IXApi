
package com.bilibili.player_ix.noixmod_api.entities.projectile.summon;

import com.github.NineAbyss9.ix_api.util.ParticleUtil;
import com.bilibili.player_ix.noixmod_api.entities.boss.star_guardian.StarGuardian;
import com.bilibili.player_ix.noixmod_api.entities.servant.core.OwnedEntity;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIParticleTypes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class SummonZhuan
extends OwnedEntity {
    public SummonZhuan(EntityType<? extends OwnedEntity> type, Level level) {
        super(type, level);
    }

    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            ParticleUtil.addFlatParticle(ParticleTypes.SMOKE, this, 1, 1);
            ParticleUtil.addFlatParticle(NoixmodAPIParticleTypes.NIHILISTIC_FIRE.get(), this, 1, 1);
        }
    }

    public boolean hasLife() {
        return true;
    }

    protected void handleDeath() {
        if (!this.level().isClientSide) {
            ServerLevel serverLevel = (ServerLevel)this.level();
            StarGuardian guardian = NoixmodAPIEntities.STAR_GUARDIAN.get().create(serverLevel);
            if (guardian != null) {
                guardian.moveTo(position().add(0, 0.3, 0));
                if (serverLevel.addFreshEntity(guardian)) {
                    ParticleUtil.sendParticles(serverLevel, ParticleTypes.WITCH, position(), 20, 1, 1, 1, 0);
                }
            }
        }
    }
}
