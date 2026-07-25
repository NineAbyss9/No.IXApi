
package com.bilibili.player_ix.noixmod_api.entities.boss.abyss;

import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;
import java.util.List;

/**Data for {@linkplain Abyss}, this may be useless now*/
class AbyssData {
    final Abyss abyss;
    AbyssData(Abyss pAbyss) {
        abyss = pAbyss;
    }

    @Nullable
    private LivingEntity getTarget() {
        return abyss.getTarget();
    }

    boolean canAttack() {
        LivingEntity target = this.getTarget();
        if (target == null) {
            return false;
        } else {
            return abyss.closerThan(target, 4.0D);
        }
    }

    boolean needToChangePhase() {
        if (abyss.isSecondPhase()) {
            return false;
        }
        return MobUtils.isHalfHealth(abyss);
    }

    void tickPhase() {
        if (this.needToChangePhase()) {
            abyss.setHealth(abyss.getMaxHealth());
            abyss.setBossPhase(2);
            abyss.setFlag(99);
        }
    }

    List<LivingEntity> entities(double... range) {
        int size = range.length;
        if (size == 1) {
            return abyss.level().getEntitiesOfClass(LivingEntity.class, abyss.getBoundingBox().inflate(range[0]), abyss.predicate);
        } else {
            return abyss.level().getEntitiesOfClass(LivingEntity.class, abyss.getBoundingBox().inflate(range[0], range[1], range[2]),
                    abyss.predicate);
        }
    }
}
