
package com.github.NineAbyss9.ix_api.api.mobs;

import com.github.NineAbyss9.ix_api.api.ISync;
import com.github.NineAbyss9.ix_api.api.Synchronizer;
import com.bilibili.player_ix.noixmod_api.util.EntitiesFinder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.function.Predicate;

public interface Ownable
extends TraceableEntity, OwnableEntity, ISync {
    default boolean isHostile() {
        return this.getOwner() instanceof Enemy;
    }

    default void setHostile(boolean hostile) {
    }

    @Nullable
    default LivingEntity getMasterOwner() {
        if (this.getOwner() instanceof Ownable ownable) {
            return ownable.getOwner();
        } else {
            return this.getOwner();
        }
    }

    default boolean isOwnedBy(LivingEntity living) {
        return this.getOwner() == living;
    }

    default OwnableData getOwnableData() {
        return new OwnableData(this);
    }

    default boolean canAccept(ItemStack stack) {
        return true;
    }

    default void onSync(Synchronizer synchronizer) {
    }

    @Nullable
    default LivingEntity getOwner() {
        if (this.ownableGetLevel().isClientSide) {
            int id = this.getOwnerId();
            Entity entity = this.ownableGetLevel().getEntity(this.getOwnerId());
            return (id <= -1 || !(entity instanceof LivingEntity) || entity == this) ? null : (LivingEntity)entity;
        } else {
            UUID uuid = this.getOwnerUUID();
            return uuid == null ? null : EntitiesFinder.getLivingEntity(this.ownableGetLevel(), uuid);
        }
    }

    @Nullable
    UUID getOwnerUUID();

    /**Sets the owner of a {@linkplain Ownable}*/
    default void setOwner(@Nullable LivingEntity lie) {
        if (lie != null) {
            this.setOwnerUUID(lie.getUUID());
            this.setOwnerId(lie.getId());
        } else {
            this.setOwnerUUID(null);
            this.setOwnerId(-1);
        }
    }

    /**Sets the owner uuid*/
    void setOwnerUUID(@Nullable UUID ownerUUID);

    default int getOwnerId() {
        return -1;
    }

    default void setOwnerId(int id) {
    }

    default void setTargetByOwner() {
        if (this instanceof Mob && this.getOwner() instanceof Mob) {
            OwnableMob.setTargetByOwner(this);
        }
    }

    default void setLifeTick(int tick) {
    }

    default int getLifeTick() {
        return 0;
    }

    default boolean hasLife() {
        return false;
    }

    default boolean wouldHaveOwner() {
        return true;
    }

    default boolean isUnowned() {
        return this.getOwner() == null;
    }

    default boolean isOwned() {
        return this.getOwner() != null;
    }

    default boolean areBothOwner(LivingEntity living) {
        if (living instanceof Ownable ownable) {
            return ownable.getOwner() == this && this.getOwner() == living;
        } else {
            return false;
        }
    }

    default void addOwnableAdditionalSaveData(CompoundTag tag) {
        if (this.getOwnerUUID() != null) {
            tag.putUUID("OwnerUUID", this.getOwnerUUID());
        }
        if (this.getOwner() != null) {
            tag.putString("Owner", this.getOwner().getDisplayName().getString());
        }
    }

    default void readOwnableAdditionalSaveData(CompoundTag tag) {
        if (tag.hasUUID("OwnerUUID")) {
            this.setOwnerUUID(tag.getUUID("OwnerUUID"));
        }
    }

    default void ownableTick() {
        if (this.getOwner() != null) {
            this.checkOwner();
            if (this.getOwner() instanceof Ownable ownable) {
                if (this.getOwner().isDeadOrDying() || !this.getOwner().isAlive()) {
                    if (ownable.getOwner() != null) {
                        this.setOwner(ownable.getOwner());
                    }
                }
            }
        }
    }

    default void checkOwner() {
        if (!this.ownableGetLevel().isClientSide && this.getOwner() != null) {
            if (this.getOwner().tickCount < 20) {
                Entity entity = this.ownableGetLevel().getEntity(this.getOwnerId());
                if (entity instanceof LivingEntity livingEntity) {
                    if (livingEntity != this.getOwner()) {
                        this.setOwnerId(this.getOwner().getId());
                    }
                } else {
                    this.setOwnerId(this.getOwner().getId());
                }
            }
        }
    }

    default Predicate<Entity> summonPredicate(){
        if (this instanceof Mob mob){
            return entity -> mob.getClass().isAssignableFrom(entity.getClass());
        }
        return entity -> entity instanceof Ownable;
    }

    private Level ownableGetLevel() {
        return ((Entity)this).level();
    }

    @Nullable
    static LivingEntity getOwner(Entity pOwnable) {
        if (pOwnable instanceof Ownable ownable) {
            return ownable.getOwner();
        } else if (pOwnable instanceof OwnableEntity entity) {
            return entity.getOwner();
        }
        return pOwnable instanceof TraceableEntity traceable && traceable.getOwner() instanceof LivingEntity living ? living : null;
    }
}
