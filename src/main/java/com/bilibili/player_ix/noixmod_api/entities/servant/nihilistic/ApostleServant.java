
package com.bilibili.player_ix.noixmod_api.entities.servant.nihilistic;

import com.github.NineAbyss9.ix_api.api.mobs.OwnableMob;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiOwnerTargetGoal;
import com.bilibili.player_ix.noixmod_api.entities.boss.Apostle;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ApostleServant
extends Apostle {
    public ApostleServant(EntityType<? extends ApostleServant> $$0, Level $$1) {
        super($$0, $$1);
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
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Apostle.createAttributes();
    }

    public boolean wouldHaveOwner() {
        return true;
    }

    @Nullable
    public LivingEntity getOwner() {
        return this.owner;
    }

    @Nullable
    public UUID getOwnerUUID() {
        if (this.owner == null && this.ownerUUID != null && this.level() instanceof ServerLevel) {
            Entity $$0 = ((ServerLevel)this.level()).getEntity(this.ownerUUID);
            if ($$0 instanceof LivingEntity lie) {
                this.setOwner(lie);
            }
        }
        return this.ownerUUID;
    }

    public void setOwner(@Nullable LivingEntity lie) {
        this.owner = lie;
        if (lie != null) {
            this.setOwnerUUID(lie.getUUID());
        }
    }

    public void setOwnerUUID(@Nullable UUID uuid) {
        this.ownerUUID = uuid;
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
            if (this.isInEnd()) {
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
}
