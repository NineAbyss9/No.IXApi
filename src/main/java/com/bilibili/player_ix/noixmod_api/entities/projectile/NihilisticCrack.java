
package com.bilibili.player_ix.noixmod_api.entities.projectile;

import com.github.NineAbyss9.ix_api.ix_api.api.mobs.Ownable;
import com.github.NineAbyss9.ix_api.ix_api.util.ParticleUtil;
import com.bilibili.player_ix.noixmod_api.entities.servant.OwnedEntity;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIParticleTypes;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;

public class NihilisticCrack
extends OwnedEntity {
    public NihilisticCrack(EntityType<? extends NihilisticCrack> type, Level level) {
        super(type, level);
    }

    public void baseTick() {
        if (!level().isClientSide && firstTick) {
            ParticleUtil.sendParticles((ServerLevel)level(), NoixmodAPIParticleTypes.CRACK.get(), position(),
                    1, 0, 0, 0, 0);
        }
        super.baseTick();
    }

    public void tick() {
        super.tick();
        if (tickCount % 20 == 0) {
            List<LivingEntity> entities = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(6, 0.3, 6),
                    e-> MobUtils.canHurt(e, this));
            if (!entities.isEmpty()) {
                entities.forEach(e->e.hurt(damageSources().indirectMagic(ownerOrThis(this)
                        , ownerOrThis(this)), 12.0F));
            }
        }
        if (level().isClientSide && level().random.nextBoolean()) {
            level().addParticle(NoixmodAPIParticleTypes.SUMMON_PARTICLE.get(), getRandomX(0.8),
                    getRandomY(), getRandomZ(0.8), 0, 0.1, 0);
        }
    }

    public boolean hasLife() {
        return true;
    }

    private static Entity ownerOrThis(Ownable ownable) {
        return ownable.getOwner() == null ? (Entity)ownable : ownable.getOwner();
    }

    protected void handleDeath() {
        this.playSound(SoundEvents.RESPAWN_ANCHOR_DEPLETE.get());
    }
}
