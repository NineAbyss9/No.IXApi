
package com.bilibili.player_ix.noixmod_api.entities.projectile;

import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ThrownAxe
extends AbstractHurtingProjectile
implements ItemSupplier {
    private static final EntityDataAccessor<ItemStack> DATA_ID_FIREWORKS_ITEM =
            SynchedEntityData.defineId(ThrownAxe.class, EntityDataSerializers.ITEM_STACK);

    public ThrownAxe(Level level, double x, double y, double z) {
        super(NoixmodAPIEntities.THROWN_AXE.get(), level);
    }

    protected void onHitBlock(BlockHitResult p_36755_) {
        super.onHitBlock(p_36755_);
        if (this.level().isClientSide) {
            this.level().addParticle(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 0.1, 0.1, 0.1);
        }
        this.level().playSound(null, this, SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.HOSTILE, 2f, 1f);
        List<LivingEntity> $$0 = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(2.0));
        for (LivingEntity livingEntity : $$0) {
            if (this.getOwner() == null) {
                return;
            } else if (livingEntity != this.getOwner() && this.getOwner() != null) {
                livingEntity.hurt(this.damageSources().thrown(this, this.getOwner()), 8.0f);
            }
        }
        this.discard();
    }

    public ItemStack getItem() {
        ItemStack itemstack = this.entityData.get(DATA_ID_FIREWORKS_ITEM);
        return itemstack.isEmpty() ? new ItemStack(Items.IRON_AXE) : itemstack;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_FIREWORKS_ITEM, ItemStack.EMPTY);
    }

    public boolean isOnFire() {
        return false;
    }

    protected boolean shouldBurn() {
        return false;
    }

    public ThrownAxe(Level level, LivingEntity lie, double d, double d1, double d2) {
        super(NoixmodAPIEntities.THROWN_AXE.get(), lie, d, d1, d2, level);
        this.setOwner(lie);
    }

    public ThrownAxe(EntityType<? extends ThrownAxe> p_36721_, Level level) {
        super(p_36721_, level);
    }

    private boolean isAcceptibleReturnOwner() {
        Entity $$0 = this.getOwner();
        if ($$0 != null && $$0.isAlive()) {
            return !($$0 instanceof ServerPlayer) || !$$0.isSpectator();
        } else {
            return false;
        }
    }

    public void tick() {
        Entity $$0 = this.getOwner();
        if ($$0 != null) {
            if (!this.isAcceptibleReturnOwner()) {
                this.discard();
            } else {
                Vec3 $$2 = $$0.getEyePosition().subtract(this.position());
                this.setPosRaw(this.getX(), this.getY() + $$2.y * 0.015 * (double) 2, this.getZ());
                if (this.level().isClientSide) {
                    this.yOld = this.getY();
                }
                double $$3 = 0.05 * (double) 2;
                this.setDeltaMovement(this.getDeltaMovement().scale(0.95).add($$2.normalize().scale($$3)));
            }
        }
        super.tick();
    }

    public boolean isPickable() {
        return false;
    }

    protected void onHit(HitResult p_37260_) {
        super.onHit(p_37260_);
        if (this.level().isClientSide) {
            this.level().addParticle(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
        }
        List<LivingEntity> $$0 = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(2.0));
        for (LivingEntity livingEntity : $$0) {
            if (this.getOwner() == null) {
                return;
            } else if (livingEntity != this.getOwner() && this.getOwner() != null) {
                livingEntity.hurt(this.damageSources().thrown(this, this.getOwner()), 8.0f);
                if (livingEntity instanceof Player player) {
                    player.disableShield(player.canDisableShield());
                }
            }
        }
        this.discard();
    }

    protected void onHitEntity(EntityHitResult p_37216_) {
        super.onHitEntity(p_37216_);
        if (!this.level().isClientSide) {
            this.level().addParticle(ParticleTypes.EXPLOSION, this.getX(),this.getY(),this.getZ(), 0.1, 0.1, 0.1);
            Entity entity = p_37216_.getEntity();
            Entity entity1 = this.getOwner();
            entity.hurt(this.damageSources().thrown(this, entity1), 8.0F);
            if (entity1 instanceof LivingEntity) {
                this.doEnchantDamageEffects((LivingEntity) entity1, entity);
            }
        }
        this.discard();
    }
}
