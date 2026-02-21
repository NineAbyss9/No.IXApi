
package com.bilibili.player_ix.noixmod_api.entities.projectile;

import com.github.NineAbyss9.ix_api.api.mobs.IProjectile;
import com.github.NineAbyss9.ix_api.api.mobs.Nihilistic;
import com.github.NineAbyss9.ix_api.api.mobs.Ownable;
import com.bilibili.player_ix.noixmod_api.entities.servant.OwnedEntity;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class NihilisticFire
extends OwnedEntity
implements Ownable, Nihilistic, IProjectile {
    int lifeTicks = 3;
    public NihilisticFire(EntityType<? extends NihilisticFire> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
        this.setNoGravity(true);
        this.noPhysics = true;
    }

    public boolean hurt(DamageSource p_21016_, float p_21017_) {
        if (p_21016_.is(DamageTypeTags.IS_FALL)) {
            return false;
        }
        return super.hurt(p_21016_, p_21017_);
    }

    public boolean fireImmune() {
        return true;
    }

    public boolean isHostile() {
        return true;
    }

    public void tick() {
        super.tick();
        --this.lifeTicks;
        double d = this.random.nextGaussian() * 0.1;
        double d1 = this.random.nextGaussian() * 0.1;
        this.level().addParticle(ParticleTypes.SOUL_FIRE_FLAME, this.getX(), this.getY(), this.getZ(), d, 0.1, d1);
        if (this.lifeTicks <= 0) {
            this.playSound(SoundEvents.FIRE_EXTINGUISH);
            this.damage();
        }
    }

    public void onHit(HitResult pResult) {
    }

    public void onHitEntity(EntityHitResult pResult) {
    }

    public void onHitBlock(BlockHitResult pResult) {
    }

    public boolean canHitEntity(Entity pEntity) {
        return !(pEntity instanceof Nihilistic) && pEntity != this.getOwner();
    }

    protected void damage() {
        if (this.getOwner() != null) {
            MobUtils.rangeHurt(2, 2, 2, this, this.damageSources().indirectMagic(this.getOwner(),
                    this.getOwner()), 2f);
        }
        this.discard();
    }
}
