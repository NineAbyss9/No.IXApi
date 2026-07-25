
package com.bilibili.player_ix.noixmod_api.entities.servant.nihilistic;

import com.github.NineAbyss9.ix_api.api.mobs.OwnableMob;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiOwnerTargetGoal;
import com.bilibili.player_ix.noixmod_api.entities.boss.Apostle;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class ApostleServant
extends Apostle {
    private static final EntityDataAccessor<Optional<UUID>> DATA_OWNER_UNIQUE_ID;
    private static final EntityDataAccessor<Integer> DATA_OWNER_ID;
    public ApostleServant(EntityType<? extends ApostleServant> type, Level level) {
        super(type, level);
    }

    protected void defineSynchedData()
    {
        super.defineSynchedData();
        this.entityData.define(DATA_OWNER_ID, -1);
        this.entityData.define(DATA_OWNER_UNIQUE_ID, Optional.empty());
    }

    public EntityType<?> getType() {
        return NoixmodAPIEntities.APOSTLE_SERVANT.get();
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new LookAtLordGoal(this));
        this.goalSelector.addGoal(1, new CloneSettingSecondPhaseGoal(this));
        this.goalSelector.addGoal(2, new ApostleBowAttackGoal(this));
        this.goalSelector.addGoal(3, new CastingSpellGoal());
        this.goalSelector.addGoal(3, new TrialSpellGoal(this));
        this.goalSelector.addGoal(3, new ShootFireballGoal(this));
        this.goalSelector.addGoal(3, new SummonStatueSpellGoal(this));
        this.goalSelector.addGoal(3, new SummonSoulGoal(this));
        this.goalSelector.addGoal(3, new RoarSpellGoal(this));
        this.goalSelector.addGoal(3, new SummonPowerEntitySpellGoal(this));
        this.goalSelector.addGoal(3, new SpreadFireballGoal(this));
        this.goalSelector.addGoal(3, new SummonStaySoulGoal(this));
        this.goalSelector.addGoal(3, new SummonArrowRainSpellGoal(this));
        this.goalSelector.addGoal(3, new RangedSummonSpellGoal(this));
        //this.goalSelector.addGoal(3, new ArmoredZombieSpellGoal(this));
        this.goalSelector.addGoal(3, new SummonServantsSpellGoal(this));
        this.goalSelector.addGoal(5, new FloatGoal(this));
        this.goalSelector.addGoal(5, new ApostleLookAtEntityGoal(this));
        this.goalSelector.addGoal(5, new ApostleRandomLookGoal(this));
        this.goalSelector.addGoal(5, new ApostleRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(5, new OwnableMob.FollowOwnerGoal<>(this, 1,
                40f, 10f, false, 900, true));
        this.targetSelector.addGoal(0, new ApiOwnerTargetGoal(this));
        this.targetSelector.addGoal(1, new OwnableMob.OwnableTargetGoal<>(this, false));
        this.targetSelector.addGoal(2, new OwnableMob.OwnerHurtTargetGoal<>(this));
        this.targetSelector.addGoal(2, new OwnableMob.OwnableHurtByTargetGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Apostle.createAttributes();
    }

    public boolean isClone()
    {
        return true;
    }

    public boolean wouldHaveOwner() {
        return true;
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        this.addOwnableAdditionalSaveData(tag);
        super.addAdditionalSaveData(tag);
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        this.readOwnableAdditionalSaveData(tag);
        super.readAdditionalSaveData(tag);
    }

    public int healTick() {
        if (this.getOwner() != null) {
            if (this.inEnd) {
                return this.healTicker() / 2;
            } else {
                return this.healTicker();
            }
        } else {
            return 30;
        }
    }

    public int healTicker() {
        if (this.getOwner() == null) {
            return 30;
        } else {
            float health = (this.getOwner().getHealth() / this.getMaxHealth());
            if (health < 0.5F) {
                return 15;
            } else {
                return (int)(30 * health);
            }
        }
    }

    @Nullable
    public UUID getOwnerUUID() {
        return this.entityData.get(DATA_OWNER_UNIQUE_ID).orElse(null);
    }

    public void setOwnerUUID(@Nullable UUID uuid) {
        this.entityData.set(DATA_OWNER_UNIQUE_ID, Optional.ofNullable(uuid));
    }

    public int getOwnerId() {
        return this.entityData.get(DATA_OWNER_ID);
    }

    public void setOwnerId(int id) {
        this.entityData.set(DATA_OWNER_ID, id);
    }

    public boolean isHostile() {
        return this.getOwner() instanceof Enemy;
    }

    static {
        DATA_OWNER_ID = SynchedEntityData.defineId(ApostleServant.class, EntityDataSerializers.INT);
        DATA_OWNER_UNIQUE_ID = SynchedEntityData.defineId(ApostleServant.class,
                EntityDataSerializers.OPTIONAL_UUID);
    }
}
