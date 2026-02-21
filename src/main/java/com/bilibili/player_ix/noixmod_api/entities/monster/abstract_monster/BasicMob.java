
package com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster;

import com.github.NineAbyss9.ix_api.api.mobs.ApiPathfinderMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class BasicMob
extends ApiPathfinderMob {
    public BasicMob(EntityType<? extends BasicMob> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
    }

    @Override
    public boolean canAttack(LivingEntity p_21171_) {
        if (p_21171_ instanceof BasicMob) {
            return false;
        }
        return super.canAttack(p_21171_);
    }

    @Nullable
    @Override
    public LivingEntity getTarget() {
        if (super.getTarget() instanceof BasicMob) {
            return null;
        }
        return super.getTarget();
    }
}
