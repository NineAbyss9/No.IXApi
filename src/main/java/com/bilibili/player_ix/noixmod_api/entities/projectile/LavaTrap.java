
package com.bilibili.player_ix.noixmod_api.entities.projectile;

import com.bilibili.player_ix.noixmod_api.entities.servant.OwnedEntity;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIParticleTypes;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class LavaTrap extends OwnedEntity {
    public LavaTrap(EntityType<? extends LavaTrap> type, Level level) {
        super(type, level);
    }

    public LavaTrap(Level level) {
        this(NoixmodAPIEntities.LAVA_TRAP.get(), level);
    }

    public void tick() {
        if (this.getLifeTick() == 2) {
            this.damage();
            this.discard();
        }
        super.tick();
        if (this.level().isClientSide()) {
            this.level().addParticle(NoixmodAPIParticleTypes.API_LAVA.get(), this.getRandomX(0.5),
                    this.getY() + 0.1, this.getRandomZ(0.5), 0, 0, 0);
        }
    }

    public void damage() {
        this.playSound(SoundEvents.GENERIC_EXPLODE, 2, 0.5F);
        if (this.level() instanceof ServerLevel level) {
            level.sendParticles(ParticleTypes.LAVA, this.getRandomX(0.5), this.getRandomY(),
                    this.getRandomZ(0.5), 12, 0, 0, 0, 1);
        }
        MobUtils.rangeHurtAndFire(3, 1, 3, this.getOwner() == null ? this : this.getOwner(),
                this.damageSources().inFire(), 8, 4);
    }

    public boolean hasLife() {
        return true;
    }

    public int getDefaultLifeTime() {
        return 30;
    }
}
