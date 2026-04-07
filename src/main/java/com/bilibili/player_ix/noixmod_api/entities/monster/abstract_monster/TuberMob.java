
package com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster;

import com.github.NineAbyss9.ix_api.api.mobs.OwnableMob;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public abstract class TuberMob
extends OwnableMob {
    public TuberMob(EntityType<? extends TuberMob> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
    }

    public boolean canAttack(@NotNull LivingEntity p_21171_) {
        if (p_21171_ instanceof TuberMob) {
            return false;
        }
        return super.canAttack(p_21171_);
    }

    @Override
    public boolean hurt(@NotNull DamageSource pSource, float pAmount) {
        Entity entity = pSource.getEntity();
        Entity in = pSource.getDirectEntity();
        if (entity instanceof TuberMob || in instanceof TuberMob) {
            return false;
        }
        return super.hurt(pSource, pAmount);
    }
}
