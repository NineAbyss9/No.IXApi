
package com.github.NineAbyss9.ix_api.ix_api.api.mobs;

import com.github.NineAbyss9.ix_api.ix_api.util.Colors;
import com.github.NineAbyss9.ix_api.ix_api.util.Maths;
import com.github.NineAbyss9.ix_api.ix_api.util.ParticleUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;

public interface IConversion {
    boolean isConverting();

    int getConversionTick();

    void setConversionTick(int tick);

    void onConvert();

    default float[] getConversionColor() {
        return Colors.GREEN;
    }

    default void convertTick() {
        if (this.isConverting()) {
            if (this instanceof Entity entity) {
                if (entity.level().isClientSide) {
                    AABB aabb = entity.getBoundingBox();
                    ParticleUtil.addParticle(entity.level(), ParticleTypes.ENTITY_EFFECT,
                            entity.position().add(Maths.randomInt((int)aabb.getXsize()),
                                    Maths.randomInt((int)aabb.getYsize()), Maths.randomInt((int)aabb.getZsize())),
                            this.getConversionColor());
                }
            }
            this.setConversionTick(this.getConversionTick() - 1);
        }
        if (this.getConversionTick() == 0) {
            this.onConvert();
        }
    }
}
