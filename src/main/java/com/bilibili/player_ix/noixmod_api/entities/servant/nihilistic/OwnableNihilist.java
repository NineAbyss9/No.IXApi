
package com.bilibili.player_ix.noixmod_api.entities.servant.nihilistic;

import com.github.NineAbyss9.ix_api.api.mobs.Ownable;
import com.github.NineAbyss9.ix_api.api.mobs.OwnableData;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.SpellcasterNihilist;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.scores.Team;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public abstract class OwnableNihilist
extends SpellcasterNihilist
implements Ownable {
    @Nullable
    protected UUID ownerUUID;
    private static final EntityDataAccessor<Optional<UUID>> DATA_OWNER_UUID;
    private static final EntityDataAccessor<Integer> DATA_OWNER_ID;
    protected OwnableData ownableData = new OwnableData(this);
    protected OwnableNihilist(EntityType<? extends OwnableNihilist> entityType, Level level) {
        super(entityType, level);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_OWNER_UUID, Optional.empty());
        this.entityData.define(DATA_OWNER_ID, -1);
    }

    public boolean canAttack(LivingEntity lie) {
        if (!MobUtils.canHurt(lie, this)) {
            return false;
        }
        return super.canAttack(lie);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        addOwnableAdditionalSaveData (tag);
        this.ownableData.addOwnableAdditionalSaveData(tag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        readOwnableAdditionalSaveData(tag);
        this.ownableData.readOwnableAdditionalSaveData(tag);
    }

    public OwnableData getOwnableData() {
        return ownableData;
    }

    @Nullable
    @Override
    public UUID getOwnerUUID() {
        return this.entityData.get(DATA_OWNER_UUID).orElse(null);
    }

    @Nullable
    @Override
    public Team getTeam() {
        LivingEntity entity = this.getOwner();
        if (entity != null) {
            if (this.areBothOwner(entity)) {
                return super.getTeam();
            }
            return entity.getTeam();
        }
        return super.getTeam();
    }

    public void setOwnerUUID(@Nullable UUID ownerUUID) {
        this.entityData.set(DATA_OWNER_UUID, Optional.ofNullable(ownerUUID));
    }

    public int getOwnerId() {
        return this.entityData.get(DATA_OWNER_ID);
    }

    public void setOwnerId(int id) {
        this.entityData.set(DATA_OWNER_ID, id);
    }

    protected SoundEvent getCastingSoundEvent() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    static {
        DATA_OWNER_UUID = SynchedEntityData.defineId(OwnableNihilist.class, EntityDataSerializers.OPTIONAL_UUID);
        DATA_OWNER_ID = SynchedEntityData.defineId(OwnableNihilist.class, EntityDataSerializers.INT);
    }
}
