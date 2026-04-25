
package com.bilibili.player_ix.noixmod_api.entities.projectile;

import com.github.NineAbyss9.ix_api.api.mobs.Ownable;
import com.bilibili.player_ix.noixmod_api.entities.servant.core.OwnedEntity;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class NihilisticLightningBlot
extends OwnedEntity
implements Ownable {
    private int life;
    public long seed;
    private int flashes;
    private final Set<Entity> hitEntities = Sets.newHashSet();
    LightningBolt bolt;
    private int blocksSetOnFire;
    private float damage = 5.0F;

    public NihilisticLightningBlot(EntityType<? extends OwnedEntity> type, Level level) {
        super(type, level);
        this.life = 2;
        this.seed = this.random.nextLong();
        this.flashes = this.random.nextInt(3) + 1;
    }

    public SoundSource getSoundSource() {
        return SoundSource.WEATHER;
    }

    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
    }

    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
    }

    private BlockPos getStrikePosition() {
        Vec3 vec3 = this.position();
        return BlockPos.containing(vec3.x, vec3.y - 1.0E-6, vec3.z);
    }

    private void powerLightningRod() {
        BlockPos blockpos = this.getStrikePosition();
        BlockState blockstate = this.level().getBlockState(blockpos);
        if (blockstate.is(Blocks.LIGHTNING_ROD)) {
            ((LightningRodBlock)blockstate.getBlock()).onLightningStrike(blockstate, this.level(), blockpos);
        }
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getDamage() {
        return this.damage;
    }

    public void tick() {
        if (this.life == 2) {
            if (this.level().isClientSide()) {
                this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.WEATHER, 10000.0F, 0.8F + this.random.nextFloat() * 0.2F, false);
                this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.WEATHER, 2.0F, 0.5F + this.random.nextFloat() * 0.2F, false);
            } else {
                Difficulty difficulty = this.level().getDifficulty();
                if (difficulty == Difficulty.NORMAL || difficulty == Difficulty.HARD) {
                    this.spawnFire(4);
                }
                this.powerLightningRod();
                NihilisticLightningBlot.clearCopperOnLightningStrike(this.level(), this.getStrikePosition());
                this.gameEvent(GameEvent.LIGHTNING_STRIKE);
            }
        }
        --this.life;
        List<Entity> list1;
        if (this.life < 0) {
            if (this.flashes == 0) {
                this.discard();
            } else if (this.life < -this.random.nextInt(10)) {
                --this.flashes;
                this.life = 1;
                this.seed = this.random.nextLong();
                this.spawnFire(0);
            }
        }
        if (this.life >= 0) {
            if (!(this.level() instanceof ServerLevel)) {
                this.level().setSkyFlashTime(3);
            } else {
                list1 = this.level().getEntities(this, new AABB(this.getX() - 3.0, this.getY() - 3.0, this.getZ() - 3.0, this.getX() + 3.0, this.getY() + 6.0 + 3.0, this.getZ() + 3.0), Entity::isAlive);
                for (Entity entity : list1) {
                    if (entity instanceof LivingEntity lie) {
                        if (MobUtils.canHurt(lie, this)) {
                            lie.hurt(this.damageSources().wither(), this.getDamage());
                        }
                    } else {
                        entity.hurt(this.damageSources().wither(), this.getDamage());
                    }
                }
                this.hitEntities.addAll(list1);
            }
        }
        this.baseTick();
    }

    private void spawnFire(int p_20871_) {
        this.hitEntities.iterator().next();
        if (!this.level().isClientSide && this.level().getGameRules().getBoolean(GameRules.RULE_DOFIRETICK)) {
            BlockPos blockpos = this.blockPosition();
            BlockState blockstate = BaseFireBlock.getState(this.level(), blockpos);
            if (this.level().getBlockState(blockpos).isAir() && blockstate.canSurvive(this.level(), blockpos)) {
                this.level().setBlockAndUpdate(blockpos, blockstate);
                ++this.blocksSetOnFire;
            }
            for(int i = 0; i < p_20871_; ++i) {
                BlockPos blockpos1 = blockpos.offset(this.random.nextInt(3) - 1, this.random.nextInt(3) - 1, this.random.nextInt(3) - 1);
                blockstate = BaseFireBlock.getState(this.level(), blockpos1);
                if (this.level().getBlockState(blockpos1).isAir() && blockstate.canSurvive(this.level(), blockpos1)) {
                    this.level().setBlockAndUpdate(blockpos1, blockstate);
                    ++this.blocksSetOnFire;
                }
            }
        }
    }

    private static void clearCopperOnLightningStrike(Level p_147151_, BlockPos p_147152_) {
        BlockState blockstate = p_147151_.getBlockState(p_147152_);
        BlockPos blockpos;
        BlockState state;
        if (blockstate.is(Blocks.LIGHTNING_ROD)) {
            blockpos = p_147152_.relative(blockstate.getValue(LightningRodBlock.FACING).getOpposite());
            state = p_147151_.getBlockState(blockpos);
        } else {
            blockpos = p_147152_;
            state = blockstate;
        }
        if (state.getBlock() instanceof WeatheringCopper) {
            p_147151_.setBlockAndUpdate(blockpos, WeatheringCopper.getFirst(p_147151_.getBlockState(blockpos)));
            BlockPos.MutableBlockPos blockpos$mutableblockpos = p_147152_.mutable();
            int i = p_147151_.random.nextInt(3) + 3;
            for(int j = 0; j < i; ++j) {
                int k = p_147151_.random.nextInt(8) + 1;
                NihilisticLightningBlot.randomWalkCleaningCopper(p_147151_, blockpos, blockpos$mutableblockpos, k);
            }
        }
    }

    private static void randomWalkCleaningCopper(Level p_147146_, BlockPos p_147147_, BlockPos.MutableBlockPos p_147148_, int p_147149_) {
        p_147148_.set(p_147147_);
        for(int i = 0; i < p_147149_; ++i) {
            Optional<BlockPos> optional = randomStepCleaningCopper(p_147146_, p_147148_);
            if (optional.isEmpty()) {
                break;
            }
            p_147148_.set(optional.get());
        }
    }

    private static Optional<BlockPos> randomStepCleaningCopper(Level p_147154_, BlockPos p_147155_) {
        Iterator<BlockPos> var2 = BlockPos.randomInCube(p_147154_.random, 10, p_147155_, 1).iterator();
        BlockPos blockpos;
        BlockState blockstate;
        do {
            if (!var2.hasNext()) {
                return Optional.empty();
            }
            blockpos = var2.next();
            blockstate = p_147154_.getBlockState(blockpos);
        } while (!(blockstate.getBlock() instanceof WeatheringCopper));
        BlockPos finalBlockpos = blockpos;
        WeatheringCopper.getPrevious(blockstate).ifPresent((p_147144_) -> p_147154_.setBlockAndUpdate(finalBlockpos, p_147144_));
        p_147154_.levelEvent(3002, blockpos, -1);
        return Optional.of(blockpos);
    }

    public boolean shouldRenderAtSqrDistance(double p_20869_) {
        double d0 = 64.0 * getViewScale();
        return p_20869_ < d0 * d0;
    }
}
