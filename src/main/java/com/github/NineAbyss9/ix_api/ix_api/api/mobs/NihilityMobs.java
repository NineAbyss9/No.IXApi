
package com.github.NineAbyss9.ix_api.ix_api.api.mobs;

import com.bilibili.player_ix.noixmod_api.api.entity.IX;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIItems;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public abstract class NihilityMobs
extends OwnableMob
implements Nihilistic {
    protected NihilityMobs(EntityType<? extends NihilityMobs> e, Level l) {
        super(e, l);
    }

    public boolean removeWhenFarAway(double d) {
        return d > 50 && this.getOwner() == null;
    }

    public boolean canAttack(LivingEntity $$0) {
        if ($$0 instanceof IX) {
            return false;
        }
        if ($$0 instanceof Nihilistic) {
            return false;
        }
        return super.canAttack($$0);
    }

    public boolean addEffect(MobEffectInstance p_147208_, @Nullable Entity p_147209_) {
        return false;
    }

    public boolean fireImmune() {
        return true;
    }

    public MobType getMobType() {
        return ApiMobType.NIHILISTIC;
    }

    protected void dropCustomDeathLoot(DamageSource pSource, int pLooting, boolean pRecentlyHit) {
        if (this.isHostile()) {
            this.spawnAtLocation(NoixmodAPIItems.NIHILISTIC_ASH, this.randomUtil.nextInt(2));
        }
        super.dropCustomDeathLoot(pSource, pLooting, pRecentlyHit);
    }
}
