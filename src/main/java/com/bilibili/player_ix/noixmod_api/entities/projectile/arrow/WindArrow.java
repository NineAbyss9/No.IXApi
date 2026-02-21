
package com.bilibili.player_ix.noixmod_api.entities.projectile.arrow;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class WindArrow
extends Arrow {
    @SuppressWarnings("all")
    public WindArrow(Level pLevel, @Nullable LivingEntity pShooter) {
        super(pLevel, pShooter);
    }

    public WindArrow(Level pLevel, double pX, double pY, double pZ) {
        super(pLevel, pX, pY, pZ);
    }

    public WindArrow(EntityType<? extends Arrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    protected void doPostHurtEffects(LivingEntity pLiving) {
        super.doPostHurtEffects(pLiving);
    }
}
