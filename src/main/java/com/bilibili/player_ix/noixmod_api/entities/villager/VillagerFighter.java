
package com.bilibili.player_ix.noixmod_api.entities.villager;

import com.github.NineAbyss9.ix_api.api.APISpells;
import com.github.NineAbyss9.ix_api.api.mobs.*;
import com.github.NineAbyss9.ix_api.util.ItemUtil;
import com.github.NineAbyss9.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.compat.goety.GoetyCompat;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiTradeWithPlayerGoal;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Npc;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Optional;
import java.util.UUID;

public abstract class VillagerFighter
extends AbstractVillager
implements ApiVillager, Merchant, Npc, Ownable {
    @Nullable
    protected Player tradingPlayer;
    private static final EntityDataAccessor<Optional<UUID>> DATA_OWNER_UUID;
    private static final EntityDataAccessor<Integer> DATA_OWNER_ID;
    protected static final EntityDataAccessor<Integer> DATA_OWNED_TIME;
    protected static final EntityDataAccessor<Byte> SPELL = SynchedEntityData.defineId(
            VillagerFighter.class, EntityDataSerializers.BYTE);
    protected int spellCastingTickCount;
    protected APISpells.APISpell currentSpell = APISpells.APISpell.NONE;
    protected MerchantOffers offers = new MerchantOffers();
    private final OwnableData ownableData;
    public VillagerFighter(EntityType<? extends VillagerFighter> type, Level level) {
        super(type, level);
        this.ownableData = new OwnableData(this);
        this.restockAll();
        this.updateTrades();
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_OWNER_UUID, Optional.empty());
        this.entityData.define(DATA_OWNER_ID, -1);
        this.entityData.define(DATA_OWNED_TIME, 0);
        this.entityData.define(SPELL, Maths.ZERO_BYTE);
    }

    protected void registerGoals() {
        super.registerGoals();
        if (this.getNavigation() instanceof GroundPathNavigation groundPathNavigation) {
            groundPathNavigation.setCanOpenDoors(true);
        }
        this.goalSelector.addGoal(3, new OwnableMob.FollowOwnerGoal<>(this, 1.0,
                20, 5, false));
        this.goalSelector.addGoal(3, new ApiTradeWithPlayerGoal(this));
        this.goalSelector.addGoal(4, new OpenDoorGoal(this, true));
        this.addTargetGoal();
    }

    protected void addTargetGoal() {
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this,
                LivingEntity.class, false, living -> MobUtils.ownableCanHurt(living, this)));
        this.targetSelector.addGoal(1, new OwnableMob.OwnerHurtTargetGoal<>(this));
    }

    public void aiStep() {
        this.updateSwingTime();
        super.aiStep();
        if (!this.isOwnedForever()) {
            this.reduceOwnedTime();
        }
    }

    public OwnableData getOwnableData() {
        return ownableData;
    }

    @Nullable
    public UUID getOwnerUUID() {
        return this.entityData.get(DATA_OWNER_UUID).orElse(null);
    }

    public void setOwnerUUID(@Nullable UUID ownerUUID) {
        this.entityData.set(DATA_OWNER_UUID, Optional.ofNullable(ownerUUID));
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.spellCastingTickCount = tag.getInt("SpellTicks");
        this.ownableData.readOwnableAdditionalSaveData(tag);
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("SpellTicks", this.getSpellCastingTime());
        this.ownableData.addOwnableAdditionalSaveData(tag);
    }

    protected APISpells.APISpell getSpellId() {
        if (!this.level().isClientSide) {
            return this.currentSpell;
        }
        return APISpells.APISpell.getById(this.entityData.get(SPELL));
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance instance, MobSpawnType type) {
        return this.finalizeSpawn(accessor, instance, type, null, null);
    }

    public void updateTrades() {
        VillagerTrades.ItemListing[] listings = this.getTradeLists();
        if (listings != null) {
            this.addOffersFromItemListings(this.getOffers(), listings, 10);
        }
    }

    public void rewardTradeXp(MerchantOffer merchantOffer) {
        ApiVillager.super.rewardTradeXp(merchantOffer);
    }

    @Nullable
    protected VillagerTrades.ItemListing[] getTradeLists() {
        return null;
    }

    public void restockAll() {
        for (MerchantOffer merchantOffers : this.getOffers()) {
            merchantOffers.resetUses();
        }
    }

    public void startTrading(Player player) {
        player.awardStat(Stats.TALKED_TO_VILLAGER);
        this.openTradingScreen(player, this.getDisplayName(), 1);
        this.setTradingPlayer(player);
    }

    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (this.isAlive() && !this.isTrading() && !this.isBaby() && this.isEffectiveAi()) {
            ItemStack itemStack = player.getItemInHand(hand);
            boolean flag = GoetyCompat.goetyLoaded() && itemStack.is(GoetyCompat.getItem(
                    "magic_emerald"));
            if ((itemStack.is(Items.EMERALD) || flag) && itemStack.getCount() > 12) {
                int baseTime = itemStack.getCount() * 600 * 20;
                int time;
                if (player instanceof ServerPlayer serverPlayer) {
                    ResourceLocation pLocation = new ResourceLocation("minecraft", "nether/root");
                    Advancement advancement = serverPlayer.server.getAdvancements().getAdvancement(pLocation);
                    if (advancement != null &&
                            serverPlayer.getAdvancements().getOrStartProgress(advancement).isDone()) {
                        time = -1;
                    } else {
                        time = flag ? baseTime * 2 : baseTime;
                    }
                    serverPlayer.sendSystemMessage(Component.translatable("info.noixmodapi.villager_fighter.set_owner",
                            this.getDisplayName(), player.getDisplayName(), time), false);
                } else {
                    time = flag ? baseTime * 2 : baseTime;
                }
                this.setOwnedTime(time);
                if (time != -1) {
                    ItemUtil.shrink(itemStack, player, itemStack.getCount());
                }
                this.setOwner(player);
                return InteractionResult.SUCCESS;
            } else {
                if (player.isShiftKeyDown()) {
                    if (this.ownableData.nextFlag(player, hand)) {
                        this.ownableData.nextFlag();
                        return InteractionResult.SUCCESS;
                    }
                } else if (!this.getOffers().isEmpty()) {
                    if (!this.level().isClientSide && !this.isAggressive()) {
                        this.startTrading(player);
                        return InteractionResult.sidedSuccess(this.level().isClientSide);
                    }
                }
            }
            return InteractionResult.PASS;
        } else {
            return super.mobInteract(player, hand);
        }
    }

    public boolean isOwned() {
        return this.getOwner() != null && this.getOwnedTime() > 0;
    }

    public boolean isUnowned() {
        return !this.isOwned();
    }

    public boolean isOwnedForever() {
        return this.getOwnedTime() == -1;
    }

    public int getOwnedTime() {
        return this.entityData.get(DATA_OWNED_TIME);
    }

    public void setOwnedTime(int time) {
        this.entityData.set(DATA_OWNED_TIME, time);
    }

    protected void reduceOwnedTime() {
        this.setOwnedTime(this.getOwnedTime() - 1);
    }

    public boolean isTrading() {
        return this.tradingPlayer != null;
    }

    @Nullable
    public Player getTradingPlayer() {
        return tradingPlayer;
    }

    public void setTradingPlayer(@Nullable Player tradingPlayer) {
        this.tradingPlayer = tradingPlayer;
    }

    public void setTarget(@Nullable LivingEntity pTarget) {
        if (pTarget instanceof ApiVillager) {
            return;
        }
        super.setTarget(pTarget);
    }

    public MerchantOffers getOffers() {
        if (this.offers == null) {
            this.offers = new MerchantOffers();
            this.updateTrades();
        }
        return this.offers;
    }

    public void notifyTradeUpdated(ItemStack itemStack) {
        if (!this.level().isClientSide && this.ambientSoundTime > -this.getAmbientSoundInterval() + 20) {
            this.ambientSoundTime = -this.getAmbientSoundInterval();
            this.playSound(this.getTradeUpdatedSound(!itemStack.isEmpty()), this.getSoundVolume(), this.getVoicePitch());
        }
    }

    public int getVillagerXp() {
        return 1;
    }

    public boolean canRestock() {
        return true;
    }

    public void notifyTrade(MerchantOffer merchantOffer) {
        merchantOffer.increaseUses();
        this.ambientSoundTime = -this.getAmbientSoundInterval();
        this.rewardTradeXp(merchantOffer);
    }

    public void overrideOffers(MerchantOffers merchantOffers) {
    }

    public void overrideXp(int i) {
    }

    public SoundEvent getTradeUpdatedSound(boolean p_35323_) {
        return ApiVillager.super.getTradeUpdatedSound(p_35323_);
    }

    public boolean showProgressBar() {
        return false;
    }

    public SoundEvent getNotifyTradeSound() {
        return SoundEvents.VILLAGER_TRADE;
    }

    public boolean isClientSide() {
        return this.level().isClientSide;
    }

    public void checkDespawn() {
        if (this.getSpawnType() != MobSpawnType.STRUCTURE && this.getSpawnType()
                != MobSpawnType.BREEDING) {
            super.checkDespawn();
        }
    }

    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.getSpellCastingTime() > 0) {
            --this.spellCastingTickCount;
        }
    }

    public void tick() {
        super.tick();
        if (this.level().isClientSide && this.isCastingSpell()) {
            APISpells.APISpell spellId = this.getSpellId();
            double $$1 = spellId.spellColor[0];
            double $$2 = spellId.spellColor[1];
            double $$3 = spellId.spellColor[2];
            float $$4 = this.yBodyRot * (Mth.PI / 180) + Mth.cos(this.tickCount * 0.6662f) * 0.25f;
            float $$5 = Mth.cos($$4);
            float $$6 = Mth.sin($$4);
            this.level().addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() + $$5 * 0.6, this.getY()
                    + 1.8, this.getZ() + $$6 * 0.6, $$1, $$2, $$3);
            this.level().addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() - $$5 * 0.6, this.getY()
                    + 1.8, this.getZ() - $$6 * 0.6, $$1, $$2, $$3);
        }
    }

    public boolean isCastingSpell() {
        if (this.level().isClientSide) {
            return this.entityData.get(SPELL) > 0;
        }
        return this.spellCastingTickCount > 0;
    }

    protected void setSpell(APISpells.APISpell spell) {
        this.currentSpell = spell;
        this.entityData.set(SPELL, (byte)spell.id);
    }

    public int getOwnerId() {
        return this.entityData.get(DATA_OWNER_ID);
    }

    public void setOwnerId(int id) {
        this.entityData.set(DATA_OWNER_ID, id);
    }

    public MobType getMobType() {
        return ApiMobType.VILLAGER;
    }

    public VillagerFighterArmPose getArmPose() {
        if (this.isCastingSpell()) {
            return VillagerFighter.VillagerFighterArmPose.SPELL_CASTING;
        }
        if (this.isAggressive()) {
            return VillagerFighter.VillagerFighterArmPose.BOW_AND_ARROW;
        }
        return VillagerFighter.VillagerFighterArmPose.CROSSED;
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        Entity entity = pSource.getEntity();
        Entity directEntity = pSource.getDirectEntity();
        if (!MobUtils.canHurt(this, entity) || !MobUtils.canHurt(this, directEntity)) {
            return false;
        }
        return super.hurt(pSource, pAmount);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.VILLAGER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_33034_) {
        return SoundEvents.VILLAGER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.VILLAGER_DEATH;
    }

    protected int getSpellCastingTime() {
        return this.spellCastingTickCount;
    }

    protected abstract SoundEvent getCastingSoundEvent();

    static {
        DATA_OWNER_UUID = SynchedEntityData.defineId(VillagerFighter.class, EntityDataSerializers.OPTIONAL_UUID);
        DATA_OWNER_ID = SynchedEntityData.defineId(VillagerFighter.class, EntityDataSerializers.INT);
        DATA_OWNED_TIME = SynchedEntityData.defineId(VillagerFighter.class, EntityDataSerializers.INT);
    }

    protected abstract class UseSpellGoal
    extends Goal {
        protected int attackWarmupDelay;
        protected int nextAttackTickCount;

        protected UseSpellGoal() {
        }

        public boolean canUse() {
            LivingEntity $$0 = VillagerFighter.this.getTarget();
            if (this.needTarget() && ($$0 == null || !$$0.isAlive())) {
                return false;
            }
            if (VillagerFighter.this.isCastingSpell()) {
                return false;
            }
            return VillagerFighter.this.tickCount >= this.nextAttackTickCount;
        }

        public boolean canContinueToUse() {
            if (this.needTarget() && VillagerFighter.this.getTarget() == null) {
                return false;
            }
            return this.attackWarmupDelay > 0;
        }

        public void start() {
            this.attackWarmupDelay = this.adjustedTickDelay(this.getCastWarmupTime());
            VillagerFighter.this.spellCastingTickCount = this.getCastingTime();
            this.nextAttackTickCount = VillagerFighter.this.tickCount + this.getCastingInterval();
            SoundEvent $$0 = this.getSpellPrepareSound();
            if ($$0 != null) {
                VillagerFighter.this.playSound($$0, 1.0f, 1.0f);
            }
            VillagerFighter.this.setSpell(this.getSpell());
        }

        public void tick() {
            --this.attackWarmupDelay;
            if (this.attackWarmupDelay == 0) {
                this.performSpellCasting();
                VillagerFighter.this.playSound(VillagerFighter.this.getCastingSoundEvent(), 1.0f, 1.0f);
            }
        }

        protected boolean needTarget() {
            return true;
        }

        protected abstract void performSpellCasting();

        protected int getCastWarmupTime() {
            return 20;
        }

        protected abstract int getCastingTime();

        protected abstract int getCastingInterval();

        @Nullable
        protected abstract SoundEvent getSpellPrepareSound();

        protected abstract APISpells.APISpell getSpell();
    }

    protected class CastingSpellGoal
    extends Goal {
        public CastingSpellGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        public boolean canUse() {
            return VillagerFighter.this.getSpellCastingTime() > 0;
        }

        public void start() {
            VillagerFighter.this.getNavigation().stop();
        }

        public void stop() {
            VillagerFighter.this.setSpell(APISpells.APISpell.NONE);
        }

        public void tick() {
            if (VillagerFighter.this.getTarget() != null) {
                VillagerFighter.this.getLookControl().setLookAt(VillagerFighter.this.getTarget(),
                        getMaxHeadYRot(), VillagerFighter.this.getMaxHeadXRot());
            }
        }
    }

    public enum VillagerFighterArmPose {
        CROSSED,
        ATTACKING,
        BOW_AND_ARROW,
        CROSSBOW_HOLD,
        CROSSBOW_CHARGE,
        CELEBRATING,
        NATURAL,
        SPELL_CASTING,
    }

    protected static class VillagerFighterHurtByTargetGoal extends HurtByTargetGoal {
        public VillagerFighterHurtByTargetGoal(PathfinderMob p_26039_, Class<?>... p_26040_) {
            super(p_26039_, p_26040_);
        }

        public boolean canUse() {
            if (!super.canUse()) {
                return false;
            }
            return !(this.mob.getLastHurtByMob() instanceof AbstractVillager)
                    && !(this.mob.getLastHurtByMob() instanceof ApiVillager);
        }
    }

    protected class FighterHealSpellGoal
    extends UseSpellGoal {
        private final float healAmount;
        public FighterHealSpellGoal(float amount) {
            super();
            this.healAmount = amount;
        }

        public boolean canUse() {
            if (VillagerFighter.this.getHealth() >= (VillagerFighter.this.getMaxHealth() - this.healAmount)) {
                return false;
            }
            if (isBaby()) {
                return false;
            }
            return super.canUse();
        }

        protected void performSpellCasting() {
            VillagerFighter.this.heal(this.healAmount);
        }

        protected int getCastingTime() {
            return 20;
        }

        protected int getCastingInterval() {
            return 500;
        }

        @Nullable
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.BELL_RESONATE;
        }

        protected APISpells.APISpell getSpell() {
            return APISpells.APISpell.REGEN;
        }

        protected boolean needTarget() {
            return false;
        }
    }
}
