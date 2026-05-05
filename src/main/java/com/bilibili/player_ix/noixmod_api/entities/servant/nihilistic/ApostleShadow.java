
package com.bilibili.player_ix.noixmod_api.entities.servant.nihilistic;

import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import com.github.NineAbyss9.ix_api.api.mobs.OwnableMob;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiOwnerTargetGoal;
import com.bilibili.player_ix.noixmod_api.entities.boss.Apostle;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIParticleTypes;
import com.bilibili.player_ix.noixmod_api.util.WorldUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ApostleShadow
extends Apostle {
    private long teleportCooldown = 60L;
    public ApostleShadow(EntityType<? extends Apostle> apostle, Level world) {
        super(apostle, world);
    }

    public ApostleShadow(PlayMessages.SpawnEntity entity, Level world) {
        this(NoixmodAPIEntities.APOSTLE_SHADOW.get(), world);
    }

    public EntityType<?> getType() {
        return NoixmodAPIEntities.APOSTLE_SHADOW.get();
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

    public boolean isShadow()
    {
        return true;
    }

    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.clientLevel().addParticle(this.getParticleType(), this.getRandomX(0.5), this.getRandomY(),
                    this.getRandomZ(0.5), 0, 0.015, 0);
        }
    }

    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.teleportCooldown > 0) {
            --this.teleportCooldown;
        } else {
            this.teleportCooldown = 60L;
            this.teleport();
        }
    }

    public void die(DamageSource p_21014_) {
        this.makeParticle();
        this.discard();
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new LookAtLordGoal(this));
        this.goalSelector.addGoal(1, new ApostleBowAttackGoal(this));
        this.goalSelector.addGoal(2, new CastingSpellGoal());
        this.goalSelector.addGoal(3, new ShootFireballGoal(this));
        this.goalSelector.addGoal(3, new TrialSpellGoal(this));
        this.goalSelector.addGoal(3, new SummonStaySoulGoal(this));
        this.goalSelector.addGoal(4, new FloatGoal(this));
        this.goalSelector.addGoal(4, new ApostleLookAtEntityGoal(this));
        this.goalSelector.addGoal(4, new ApostleRandomLookGoal(this));
        this.goalSelector.addGoal(4, new ApostleRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(4, new OwnableMob.FollowOwnerGoal<>(this, 1,
                40f, 10f, false));
        this.targetSelector.addGoal(0, new ApiOwnerTargetGoal(this));
        this.targetSelector.addGoal(1, new OwnableMob.OwnableTargetGoal<>(this, false));
        this.targetSelector.addGoal(1, new OwnableMob.OwnerHurtTargetGoal<>(this));
    }

    public void makeParticle() {
        if (!this.level().isClientSide) {
            WorldUtil.sendParticles(ParticleTypes.LARGE_SMOKE, this, 30, this.random.nextGaussian()
                    * 0.2);
        }
    }

    public SimpleParticleType getParticleType() {
        if (NoixmodAPIMainConfig.HorrorMode.get()) {
            return NoixmodAPIParticleTypes.BLOOD_SPELL.get();
        }
        return NoixmodAPIParticleTypes.NIHILISTIC_FIRE.get();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Apostle.createBaseAttributes().add(Attributes.MAX_HEALTH, 30);
    }
}
