
package com.bilibili.player_ix.noixmod_api.entities.friendly;

import com.github.NineAbyss9.ix_api.ix_api.api.mobs.ApiPathfinderMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgePlayer;

public abstract class AbstractFriendlyMob
extends ApiPathfinderMob {
    protected AbstractFriendlyMob(EntityType<? extends AbstractFriendlyMob> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
    }

    public boolean canAttack(LivingEntity p_21171_) {
        if (p_21171_ instanceof IForgePlayer) {
            return false;
        }
        if (p_21171_ instanceof AbstractFriendlyMob) {
            return false;
        }
        return super.canAttack(p_21171_);
    }
}
