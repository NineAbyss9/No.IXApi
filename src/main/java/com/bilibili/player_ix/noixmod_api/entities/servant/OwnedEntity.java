
package com.bilibili.player_ix.noixmod_api.entities.servant;

import com.github.NineAbyss9.ix_api.ix_api.api.mobs.Ownable;
import com.github.NineAbyss9.ix_api.ix_api.util.Maths;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.scores.Team;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public abstract class OwnedEntity
extends Entity
implements Ownable {
    protected static final EntityDataAccessor<Integer> DATA_LIFE_TIME;
    protected static final EntityDataAccessor<Float> DATA_HEALTH;
    protected static final EntityDataAccessor<Integer> DATA_OWNER_ID;
    protected static final EntityDataAccessor<Optional<UUID>> DATA_OWNER_UUID;
    public OwnedEntity(EntityType<? extends OwnedEntity> type, Level level) {
        super(type, level);
        handleLife(level);
    }

    protected void defineSynchedData() {
        this.entityData.define(DATA_LIFE_TIME, 200);
        this.entityData.define(DATA_HEALTH, 20F);
        this.entityData.define(DATA_OWNER_ID, -1);
        this.entityData.define(DATA_OWNER_UUID, Optional.empty());
    }

    public float getHealth() {
        return this.entityData.get(DATA_HEALTH);
    }

    public void setHealth(float health) {
        this.entityData.set(DATA_HEALTH, health);
    }

    protected float getMaxHealth() {
        return 20f;
    }

    protected void handleLife(Level pLevel) {
        this.setLifeTick(this.getDefaultLifeTime());
    }

    public int getDefaultLifeTime() {
        return Maths.toTick(10);
    }

    public int getLifeTick() {
        return this.entityData.get(DATA_LIFE_TIME);
    }

    public void setLifeTick(int i) {
        this.entityData.set(DATA_LIFE_TIME, i);
    }

    public void tick() {
        super.tick();
        if (this.hasLife()) {
            this.setLifeTick(this.getLifeTick() - 1);
            if (this.getLifeTick() < 0) {
                this.handleDeath();
                this.remove(RemovalReason.KILLED);
            }
        }
    }

    protected void handleDeath() {
    }

    @Nullable
    public Team getTeam() {
        LivingEntity living = this.getOwner();
        if (living != null) {
            return living.getTeam();
        }
        return super.getTeam();
    }

    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        this.addOwnableAdditionalSaveData(compoundTag);
    }

    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        this.readOwnableAdditionalSaveData(compoundTag);
    }

    public int getOwnerId() {
        return this.entityData.get(DATA_OWNER_ID);
    }

    public void setOwnerId(int id) {
        this.entityData.set(DATA_OWNER_ID, id);
    }

    @Nullable
    public UUID getOwnerUUID() {
        return this.entityData.get(DATA_OWNER_UUID).orElse(null);
    }

    public void setOwnerUUID(@Nullable UUID ownerUUID) {
        this.entityData.set(DATA_OWNER_UUID, Optional.ofNullable(ownerUUID));
    }

    static {
        DATA_LIFE_TIME = SynchedEntityData.defineId(OwnedEntity.class, EntityDataSerializers.INT);
        DATA_HEALTH = SynchedEntityData.defineId(OwnedEntity.class, EntityDataSerializers.FLOAT);
        DATA_OWNER_ID = SynchedEntityData.defineId(OwnedEntity.class, EntityDataSerializers.INT);
        DATA_OWNER_UUID = SynchedEntityData.defineId(OwnedEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    }
}
