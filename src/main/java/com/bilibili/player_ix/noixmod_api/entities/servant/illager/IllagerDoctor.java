
package com.bilibili.player_ix.noixmod_api.entities.servant.illager;

import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class IllagerDoctor
extends OwnableIllager {
    public IllagerDoctor(EntityType<? extends IllagerDoctor> entityType, Level level) {
        super(entityType, level);
    }

    protected void registerGoals() {
        super.registerGoals();
    }

    public void aiStep() {
        super.aiStep();
        if (this.level().isClientSide) {
            return;
        }
        if (this.tickCount % 600 == 0) {
            this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(16),
                            e -> MobUtils.areAllies(e, this))
                    .forEach(this::healOther);
        }
    }

    private void healOther(LivingEntity illager) {
        illager.heal(5.0F);
    }
}
