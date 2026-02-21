
package com.bilibili.player_ix.noixmod_api.entities.projectile;

import com.github.NineAbyss9.ix_api.ix_api.api.mobs.Ownable;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class HeadHunterSword extends Entity implements Ownable {
    @Nullable
    private LivingEntity owner;
    @Nullable
    private UUID ownerUUID;
    private static final EntityDataAccessor<Integer> DATA_LIFE;
    public HeadHunterSword(EntityType<?extends HeadHunterSword> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Override
    public void tick() {
        super.tick();
        this.setLife(this.getLife() + 1);
        if (this.getLife() > 60) {
            if (this.tickCount % 5 == 0) {
                if (this.getOwner() != null) {
                    List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(1,
                                    3, 1), living -> MobUtils.canHurt(living,this));
                    for (LivingEntity living : list) {
                        living.setHealth(living.getHealth() - living.getMaxHealth() / 30);
                    }
                    MobUtils.rangeHurt(1, 3, 1, this, this.damageSources().mobAttack(
                            this.getOwner()), 5);
                }
            }
        }
        if (this.getLife() > 110) {
            this.remove(RemovalReason.KILLED);
        }
    }

    @Nullable
    public LivingEntity getOwner() {
        return this.owner;
    }

    @Nullable
    @Override
    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    @Override
    public void setOwner(@Nullable LivingEntity owner) {
        this.owner = owner;
    }

    public void setOwnerUUID(@Nullable UUID ownerUUID) {
        this.ownerUUID = ownerUUID;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_LIFE, 0);
    }

    public int getLife() {
        return this.entityData.get(DATA_LIFE);
    }

    public void setLife(int life) {
        this.entityData.set(DATA_LIFE, life);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        readOwnableAdditionalSaveData(compoundTag);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        addOwnableAdditionalSaveData(compoundTag);
    }

    static {
        DATA_LIFE = SynchedEntityData.defineId(HeadHunterSword.class, EntityDataSerializers.INT);
    }
}
