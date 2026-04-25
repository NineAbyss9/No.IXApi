
package com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster;

import com.github.NineAbyss9.ix_api.api.ApiPose;
import com.github.NineAbyss9.ix_api.api.annotation.ClientOnly;
import com.github.NineAbyss9.ix_api.api.mobs.ApiPoseMob;
import com.github.NineAbyss9.ix_api.api.mobs.OwnableMob;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public abstract class AbstractGhost
extends OwnableMob
implements ApiPoseMob {
    public AbstractGhost(EntityType<? extends AbstractGhost> entityType, Level levelIn) {
        super(entityType, levelIn);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.SOUL_ESCAPE;
    }

    protected SoundEvent getHurtSound(DamageSource p_33034_) {
        return SoundEvents.SOUL_ESCAPE;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.SOUL_ESCAPE;
    }

    @ClientOnly
    protected void soulParticle() {
        this.level().addParticle(ParticleTypes.SOUL, this.getRandomX(0.8), this.getRandomY(), this.getRandomZ(0.8),
                0, 0, 0);
    }

    public ApiPose getPoses() {
        return ApiPose.NATURAL;
    }
}
