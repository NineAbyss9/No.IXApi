
package com.bilibili.player_ix.noixmod_api.entities.projectile;

import com.github.NineAbyss9.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import com.bilibili.player_ix.noixmod_api.util.WorldUtil;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class LittleFireball
extends AbstractHurtingProjectile {
    public LittleFireball(EntityType<? extends LittleFireball> p_37006_, Level p_37007_) {
        super(p_37006_, p_37007_);
    }

    public LittleFireball(Level level, LivingEntity living, double d, double dou, double o) {
        super(NoixmodAPIEntities.LITTLE_FIREBALL.get(), living, d, dou, o, level);
    }

    public EntityType<?> getType() {
        return NoixmodAPIEntities.LITTLE_FIREBALL.get();
    }

    public void tick() {
        super.tick();
        if (this.tickCount % 200 == 0) {
            this.discard();
        }
    }

    protected void onHit(HitResult p_37260_) {
        super.onHit(p_37260_);
        if (this.level() instanceof ServerLevel level) {
            WorldUtil.sendParticles(ParticleTypes.FALLING_LAVA, this, 10, 0.15, 1, 0.15, 0, level);
        }
        MobUtils.rangeHurtAndFire(3, 3, 3, this, this.damageSources().indirectMagic(this,
                this.getOwner()), 3f, Maths.randomBetweenInclusive(this.random, 8, 12));
        this.discard();
    }

    protected void onHitEntity(EntityHitResult p_37259_) {
        super.onHitEntity(p_37259_);
        Entity entity = p_37259_.getEntity();
        Entity e = this.getOwner();
        if (entity instanceof LivingEntity lie && MobUtils.canHurt(lie, e)) {
            lie.hurt(this.damageSources().indirectMagic(this, e), 3f);
            if (e instanceof LivingEntity living) {
                this.doEnchantDamageEffects(living, lie);
            }
        }
    }

    protected boolean canHitEntity(Entity p_36842_) {
        if (p_36842_ instanceof LivingEntity entity && !MobUtils.canHurt(entity, this)) {
            return false;
        }
        return super.canHitEntity(p_36842_);
    }

    protected ParticleOptions getTrailParticle() {
        return ParticleTypes.FALLING_LAVA;
    }
}
