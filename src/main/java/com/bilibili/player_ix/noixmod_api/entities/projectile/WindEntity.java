
package com.bilibili.player_ix.noixmod_api.entities.projectile;

import com.github.NineAbyss9.ix_api.util.Vec9;
import com.bilibili.player_ix.noixmod_api.entities.servant.core.OwnedEntity;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIParticleTypes;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPITags;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.NineAbyss9.math.AbyssMath;

import java.util.List;
import java.util.function.Predicate;

public class WindEntity
extends OwnedEntity {
    private final Predicate<LivingEntity> NO_WIND_AND_CAN_HURT;
    public WindEntity(EntityType<? extends WindEntity> type, Level level) {
        super(type, level);
        NO_WIND_AND_CAN_HURT = entity -> MobUtils.canHurt(entity, this)
            && !entity.getType().is(NoixmodAPITags.NOT_AFFECT_BY_WIND);
    }

    public void tick() {
        super.tick();
        if (this.tickCount % 10 != 0) return;
        if (this.level().isClientSide) {
            this.level().addParticle(NoixmodAPIParticleTypes.WIND.get(), this.getRandomX(0.6), this.getY(),
                    this.getRandomZ(0.6), AbyssMath.trueOrFalse(0.3D), 0,
                    AbyssMath.trueOrFalse(0.3D));
        } else {
            List<LivingEntity> entities = this.level().getEntitiesOfClass(LivingEntity.class,
                    this.getBoundingBox().inflate(4),
                    NO_WIND_AND_CAN_HURT);
            if (entities.isEmpty()) {
                return;
            }
            for (LivingEntity entity : entities) {
                entity.setDeltaMovement(entity.getDeltaMovement().add(Vec9.moveToVec(entity, this,
                        0.005D)));
            }
        }
    }

    public boolean hasLife() {
        return true;
    }
}
