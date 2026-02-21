
package com.bilibili.player_ix.noixmod_api.entities.boss;

import com.github.NineAbyss9.ix_api.ix_api.api.ApiSpells;
import com.github.NineAbyss9.ix_api.ix_api.api.item.ItemStacks;
import com.github.NineAbyss9.ix_api.ix_api.api.mobs.ApiBoss;
import com.github.NineAbyss9.ix_api.ix_api.api.mobs.ApiPathfinderMob;
import com.github.NineAbyss9.ix_api.ix_api.api.mobs.OwnableMob;
import com.github.NineAbyss9.ix_api.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.AbstractUseSpellGoal;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiMeleeAttackGoal;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.NormalCastingSpellGoal;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.EnemyWormIllager;
import com.bilibili.player_ix.noixmod_api.entities.servant.worm.Worm;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class WormMaster
extends EnemyWormIllager
implements ApiBoss {
    private final ServerBossEvent bossInfo;
    private static final EntityDataAccessor<Integer> DATA_SUMMON_COUNT;
    public WormMaster(EntityType<WormMaster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.bossInfo=new ServerBossEvent(getDisplayName(), BossEvent.BossBarColor.YELLOW,
                BossEvent.BossBarOverlay.PROGRESS);
        this.setItemInHand(InteractionHand.MAIN_HAND, ItemStacks.of(Items.IRON_AXE));
        this.xpReward = 100;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SUMMON_COUNT, 0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new NormalCastingSpellGoal(this));
        this.goalSelector.addGoal(1, new SummonSpellGoal(this));
        this.goalSelector.addGoal(2, new ApiMeleeAttackGoal(this, 1,
                Maths.square(2)));
        OwnableMob.addBehaviorGoals(this, 5, 1, 10F, true, true);
        this.targetSelector.addGoal(1, new MobUtils.HostileNearestAttackableTargetGoal(this, false));
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.bossInfo.setProgress(this.getHealth()/this.getMaxHealth());
    }

    @Override
    public void startSeenByPlayer(ServerPlayer p_20119_) {
        super.startSeenByPlayer(p_20119_);
        this.bossInfo.addPlayer(p_20119_);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer p_20174_) {
        super.stopSeenByPlayer(p_20174_);
        this.bossInfo.removePlayer(p_20174_);
    }

    public void summonCountPlus() {
        this.setSummonCount(getSummonCount() +1);
    }

    public boolean spawnWorm() {
        return this.getSummonCount() < 10;
    }

    public int getSummonCount() {
        return this.entityData.get(DATA_SUMMON_COUNT);
    }

    public void setSummonCount(int count) {
        this.entityData.set(DATA_SUMMON_COUNT, count);
    }

    @Override
    public WormIllagerArmPose getArmPose() {
        if (this.isCastingSpell()) {
            return WormIllagerArmPose.SPELL_CASTING;
        }
        if (this.isAggressive()) {
            return WormIllagerArmPose.ATTACKING;
        }
        return WormIllagerArmPose.CROSSED;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.EVOKER_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource p_21239_) {
        return SoundEvents.EVOKER_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.EVOKER_DEATH;
    }

    @Override
    public float getVoicePitch() {
        return super.getVoicePitch();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return ApiPathfinderMob.createPathAttributes().add(Attributes.MAX_HEALTH, 300)
                .add(Attributes.ARMOR, 4).add(Attributes.FOLLOW_RANGE, 120)
                .add(Attributes.ATTACK_DAMAGE, 5).add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    static {
        DATA_SUMMON_COUNT = SynchedEntityData.defineId(WormMaster.class, EntityDataSerializers.INT);
    }

    private abstract static class WormMasterSpellGoal extends AbstractUseSpellGoal {
        protected final WormMaster master;
        public WormMasterSpellGoal(WormMaster wormMaster) {
            super(wormMaster);
            this.master = wormMaster;
        }
    }

    private static class SummonSpellGoal extends WormMasterSpellGoal {
        public SummonSpellGoal(WormMaster finder) {
            super(finder);
        }

        @Override
        protected void castSpell() {
            if (!this.master.level().isClientSide) {
                if (this.master.spawnWorm()) {
                    Worm worm = new Worm(NoixmodAPIEntities.WORM.get(), this.master.level());
                    worm.setOwner(this.master);
                    if (this.master.level() instanceof ServerLevel level) {
                        worm.finalizeSpawn(level, level.getCurrentDifficultyAt(this.master.blockPosition()),
                                MobSpawnType.MOB_SUMMONED, null, null);
                    }
                    worm.moveTo(this.master.blockPosition(), 0, 0);
                    this.master.level().addFreshEntity(worm);
                }
                this.master.summonCountPlus();
            }
        }

        @Override
        protected int getCastingTime() {
            return 30;
        }

        @Override
        protected int getCastingInterval() {
            return 120;
        }

        @Nullable
        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EMPTY;
        }

        @Override
        protected ApiSpells.ApiSpell getSpell() {
            return ApiSpells.ApiSpell.FIRE;
        }
    }
}
