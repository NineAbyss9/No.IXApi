
package com.bilibili.player_ix.noixmod_api.entities.projectile;

import com.github.NineAbyss9.ix_api.ix_api.api.mobs.ApiTargeting;
import com.github.NineAbyss9.ix_api.ix_api.api.mobs.Nihilistic;
import com.github.NineAbyss9.ix_api.ix_api.util.Maths;
import com.github.NineAbyss9.ix_api.ix_api.util.Vec9;
import com.bilibili.player_ix.noixmod_api.entities.servant.OwnedEntity;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.UUID;

public class BlackHole extends OwnedEntity implements ApiTargeting, Nihilistic {
    @Nullable
    private LivingEntity target;
    public BlackHole(EntityType<? extends BlackHole> type, Level level) {
        super(type, level);
        this.setLifeTick(this.getDefaultLifeTime());
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.level().addParticle(NoixmodAPIParticleTypes.DARK_SPELL.get(), this.getRandomX(0.5),
                    this.getRandomY(), this.getRandomZ(0.5), 0, 0, 0);
        }
        LivingEntity living = this.getTarget();
        if (living != null) {
            this.setDeltaMovement(Vec9.moveToVec(this, living, 0.1));
            if (this.distanceToSqr(living) <= 6) {
                if (living.isAlive()) {
                    living.setHealth(living.getHealth() - 9);
                    living.addDeltaMovement(Vec9.moveToVec(living, this, 0.15));
                }
            }
        }
        if (this.getOwner() instanceof Mob mob) {
            this.setTarget(mob.getTarget());
        }
    }

    @Override
    public boolean hasLife() {
        return true;
    }

    @Override
    public int getDefaultLifeTime() {
        return Maths.toTick(60);
    }

    @Nullable
    @Override
    public LivingEntity getTarget() {
        return this.target;
    }

    @Override
    public void setTarget(@Nullable LivingEntity living) {
        this.target = living;
    }

    @Override
    public UUID getTargetUuid() {
        return null;
    }
}
