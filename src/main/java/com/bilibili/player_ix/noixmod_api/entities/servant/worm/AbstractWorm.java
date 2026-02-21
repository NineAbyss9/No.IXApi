
package com.bilibili.player_ix.noixmod_api.entities.servant.worm;

import com.github.NineAbyss9.ix_api.ix_api.api.mobs.IWormMob;
import com.github.NineAbyss9.ix_api.ix_api.api.mobs.OwnableMob;
import com.github.NineAbyss9.ix_api.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIParticleTypes;
import com.bilibili.player_ix.noixmod_api.util.WorldUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.scores.Team;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractWorm
extends OwnableMob
implements IWormMob {
    private int breedCooldown = 60;
    protected AbstractWorm(EntityType<? extends AbstractWorm> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty,
                                        MobSpawnType pReason, @Nullable SpawnGroupData p_21437_,
                                        @Nullable CompoundTag pDataTag) {
        if (pReason == MobSpawnType.SPAWN_EGG || pReason == MobSpawnType.NATURAL) {
            this.setHostile(true);
        }
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, p_21437_, pDataTag);
    }

    public int getBreedCooldown() {
        return Maths.toTick(NoixmodAPIMainConfig.WormBreedCooldown.get());
    }

    public void setBreedCooldown(int i) {
        this.breedCooldown = i;
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("BreedCooldown", this.breedCooldown);
        super.addAdditionalSaveData(tag);
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("BreedCooldown")) {
            this.setBreedCooldown(tag.getInt("BreedCooldown"));
        }
        super.readAdditionalSaveData(tag);
    }

    public boolean isHostile() {
        return super.isHostile() || this.getSpawnType() == MobSpawnType.SPAWN_EGG ||
                this.getSpawnType() == MobSpawnType.NATURAL;
    }

    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.breedCooldown > 0) {
            --this.breedCooldown;
        } else {
            this.summonBreedMob();
            this.setBreedCooldown(this.getBreedCooldown());
        }
    }

    public boolean canAttack(LivingEntity p_21171_) {
        if (WORM_PREDICATE.test(p_21171_)) {
            return false;
        }
        return super.canAttack(p_21171_);
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (WORM_PREDICATE.test(pSource.getEntity())) {
            return false;
        }
        if (pSource.is(DamageTypeTags.IS_FALL)) {
            return false;
        }
        return super.hurt(pSource, pAmount);
    }

    @Override
    public boolean killedEntity(ServerLevel level, LivingEntity p_216989_) {
        this.summonBreedMob();
        return super.killedEntity(level, p_216989_);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SILVERFISH_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource p_21239_) {
        return SoundEvents.SILVERFISH_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SILVERFISH_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos p_20135_, BlockState p_20136_) {
        this.playSound(SoundEvents.SILVERFISH_STEP, 1f, 0.5f);
    }

    @Nullable
    @Override
    public Team getTeam() {
        LivingEntity entity = this.getOwner();
        if (entity != null && !this.areBothOwner(entity)) {
            return entity.getTeam();
        }
        return super.getTeam();
    }

    @Nullable
    public AbstractWorm getBreedMob() {
        return null;
    }

    public void makeWormParticle() {
        if (this.level() instanceof ServerLevel) {
            WorldUtil.sendParticles(NoixmodAPIParticleTypes.WORM_PARTICLE.get(), this, 15,
                    this.random.nextGaussian() * 0.2);
        }
    }

    public void summonBreedMob() {
        if (this.level() instanceof ServerLevel level) {
            AbstractWorm worm = this.getBreedMob();
            if (worm != null) {
                worm.moveTo(this.blockPosition(), 0, 0);
                WorldUtil.nullableFinalizeSpawn(worm, level, level.getCurrentDifficultyAt(this.blockPosition()),
                        MobSpawnType.BREEDING);
                if (this.getOwner() != null) {
                    worm.setOwner(this.getOwner());
                }
                worm.setHostile(this.isHostile());
                worm.setBreedCooldown(this.getBreedCooldown());
                level.addFreshEntity(worm);
                this.makeWormParticle();
            }
            this.playSound(SoundEvents.SLIME_SQUISH);
        }
        if (this.isAlive()) {
            this.setHealth(this.getMaxHealth());
        }
    }
}
