
package com.bilibili.player_ix.noixmod_api.entities.npc.DanDing;

import com.github.NineAbyss9.ix_api.api.ApiPose;
import com.github.NineAbyss9.ix_api.api.mobs.ApiPoseMob;
import com.github.NineAbyss9.ix_api.api.mobs.OwnableMob;
import com.github.NineAbyss9.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiMeleeAttackGoal;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIAttributes;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.BackUpIfTooClose;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.npc.Npc;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@SuppressWarnings("ALL")
public class DanDa extends PathfinderMob implements Merchant, Npc, ApiPoseMob, NeutralMob {
    private int angryTick;
    private int hurtCount;
    @Nullable
    private Player tradingPlayer;
    private MerchantOffers offers;
    private int hurtCooldown;
    public boolean isArmored;
    @Nullable
    private DamageType source = null;
    @Nullable
    private DamageType source1 = null;
    @Nullable
    private DamageType source2 = null;
    protected static final ImmutableList<SensorType<? extends Sensor<? super DanDa>>> SENSOR_TYPES;
    private static final ImmutableList<MemoryModuleType<?>> MEMORY_MODULE_TYPES;
    public DanDa(EntityType<? extends DanDa> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
        this.restockAll();
        this.setPersistenceRequired();
        this.offers = new MerchantOffers();
        this.addTrades();
        this.armor();
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.NETHERITE_SWORD));
        this.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(Items.SHIELD));
        this.setMaxUpStep(1.375f);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new DanDaMeleeGoal(this));
        OwnableMob.addBehaviorGoals(this, 5, 0.7, 12F, true, true);
    }

    public double getAttributeValue(Attribute p_21173_) {
        if (p_21173_.equals(Attributes.ATTACK_DAMAGE) && !this.onGround()) {
            return super.getAttributeValue(p_21173_) * 1.5f;
        }
        return super.getAttributeValue(p_21173_);
    }

    protected Brain.Provider<DanDa> brainProvider() {
        return Brain.provider(MEMORY_MODULE_TYPES, SENSOR_TYPES);
    }

    protected Brain<?> makeBrain(Dynamic<?> p_21069_) {
        return makeBrain(this, this.brainProvider().makeBrain(p_21069_));
    }

    private static Brain<?> makeBrain(DanDa da, Brain<DanDa> daBrain) {
        initHideActivity(da, daBrain);
        return daBrain;
    }

    private static void initHideActivity(DanDa da, Brain<DanDa> daBrain) {
        daBrain.addActivityAndRemoveMemoryWhenStopped(Activity.HIDE, 10, ImmutableList.of(
                BehaviorBuilder.triggerIf(w -> true, BackUpIfTooClose.create(3, 0.75F))), MemoryModuleType.AVOID_TARGET);
    }

    @Override
    public void aiStep() {
        this.updateSwingTime();
        super.aiStep();
        LivingEntity target = this.getTarget();
        if (this.angryTick > 0) {
            this.angryTick--;
        } else {
            this.setTarget(null);
        }
        if (this.hurtCooldown > 0) {
            this.hurtCooldown--;
        }
        if (!this.level().isClientSide) {
            if (this.isTrading()) {
                if (this.isAggressive()) {
                    this.setTradingPlayer(null);
                } else if (this.getTradingPlayer() != null && this.getTradingPlayer().distanceTo(this) > 15) {
                    this.getTradingPlayer().closeContainer();
                }
            }
            if (this.isLeftHanded()) {
                this.setLeftHanded(false);
            }
            if (this.isAlive()) {
                if (target == null) {
                    if (this.getHealth() < this.getMaxHealth()) {
                        this.setHealth(this.getMaxHealth());
                    }
                } else {
                    if (this.onGround() && target.getY() > this.getY() + 0.5 && this.distanceToSqr(target) <= Maths.square(2.2)) {
                        this.jumpFromGround();
                    }
                    /*for (int i = 0; i < 2;++i){
                        this.level().destroyBlock(this.blockPosition().offset(0, i, 0), true);
                        this.level().destroyBlock(this.blockPosition().offset(1, i, 0), true);
                        this.level().destroyBlock(this.blockPosition().offset(1, i, 1), true);
                        this.level().destroyBlock(this.blockPosition().offset(-1, i, 0), true);
                        this.level().destroyBlock(this.blockPosition().offset(0, i, -1), true);
                        this.level().destroyBlock(this.blockPosition().offset(-1, i, -1), true);
                    }*/
                }
                int i = this.getTicksUsingItem();
                if (this.isUsingItem()) {
                    ItemStack stack = this.getMainHandItem();
                    Item item = stack.getItem();
                    if (i >= item.getUseDuration(stack)) {
                        item.finishUsingItem(stack, this.level(), this);
                    }
                    this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.NETHERITE_SWORD));
                    Difficulty difficulty = this.level().getCurrentDifficultyAt(this.blockPosition()).getDifficulty();
                    int a = 0;
                    if (difficulty.equals(Difficulty.NORMAL)) {
                        a = 3;
                    } else if (difficulty.equals(Difficulty.HARD)) {
                        a = 5;
                    }
                    this.getMainHandItem().enchant(Enchantments.SHARPNESS, a);
                } else {
                    if (!this.getMainHandItem().is(Items.NETHERITE_SWORD) && !this.getMainHandItem().is(Items.ENCHANTED_GOLDEN_APPLE)
                            && !this.getMainHandItem().is(Items.GOLDEN_APPLE)) {
                        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.NETHERITE_SWORD));
                        Difficulty difficulty = this.level().getCurrentDifficultyAt(this.blockPosition()).getDifficulty();
                        int a = 0;
                        if (difficulty.equals(Difficulty.NORMAL)) {
                            a = 3;
                        } else if (difficulty.equals(Difficulty.HARD)) {
                            a = 5;
                        }
                        this.getMainHandItem().enchant(Enchantments.SHARPNESS, a);
                    }
                }
                if (!this.getMainHandItem().isEnchanted() && this.getMainHandItem().is(Items.NETHERITE_SWORD)) {
                    Difficulty difficulty = this.level().getCurrentDifficultyAt(this.blockPosition()).getDifficulty();
                    int a = 0;
                    if (difficulty.equals(Difficulty.NORMAL)) {
                        a = 3;
                    } else if (difficulty.equals(Difficulty.HARD)) {
                        a = 5;
                    }
                    this.getMainHandItem().enchant(Enchantments.SHARPNESS, a);
                }
            }
        }
    }


    public int getRemainingPersistentAngerTime() {
        return 0;
    }

    public void setRemainingPersistentAngerTime(int pRemainingPersistentAngerTime) {
    }

    @Nullable
    public UUID getPersistentAngerTarget() {
        return null;
    }

    public void setPersistentAngerTarget(@Nullable UUID uuid) {
    }

    public void startPersistentAngerTimer() {
    }

    @Override
    protected void dropAllDeathLoot(DamageSource p_21192_) {}

    @Override
    public boolean isImmobile() {
        return this.isTrading();
    }

    @Override
    public void setTarget(@Nullable LivingEntity p_21544_) {
        this.angryTick = Maths.toTick(120);
        super.setTarget(p_21544_);
    }

    private void armor() {
        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.NETHERITE_HELMET));
        this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.NETHERITE_CHESTPLATE));
        this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.NETHERITE_LEGGINGS));
        this.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.NETHERITE_BOOTS));
        this.playSound(SoundEvents.ARMOR_EQUIP_NETHERITE);
        Difficulty difficulty = this.level().getCurrentDifficultyAt(this.blockPosition()).getDifficulty();
        int i = 0;
        if (difficulty.equals(Difficulty.NORMAL)) {
            i = 3;
        } else if (difficulty.equals(Difficulty.HARD)) {
            i = 5;
        }
        ItemStack stack = new ItemStack(Items.NETHERITE_SWORD);
        stack.enchant(Enchantments.SHARPNESS, i);
        this.setItemInHand(InteractionHand.MAIN_HAND, stack);
        this.getArmorSlots().forEach(this::enchant);
        this.isArmored = true;
    }

    private void enchant(ItemStack stack) {
        Difficulty difficulty = this.level().getCurrentDifficultyAt(this.blockPosition()).getDifficulty();
        if (difficulty.equals(Difficulty.NORMAL)) {
            stack.enchant(Enchantments.ALL_DAMAGE_PROTECTION, 2);
        } else if (difficulty.equals(Difficulty.HARD)) {
            stack.enchant(Enchantments.ALL_DAMAGE_PROTECTION, 4);
        }
    }

    @Override
    public ApiPose getPoses() {
        return ApiPose.NATURAL;
    }

    @Override
    public boolean doHurtTarget(Entity p_21372_) {
        boolean flag = super.doHurtTarget(p_21372_);
        MobUtils.rangeHurt(5, 5, 5, this, this.damageSources().mobAttack(this),
                (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
        if (flag) {
            if (p_21372_ instanceof LivingEntity living && living.isAlive()) {
                float danDa$doHurtTarget5Float;
                if (this.onGround()) {
                    danDa$doHurtTarget5Float = 20;
                } else {
                    danDa$doHurtTarget5Float = 10;
                }
                living.setHealth(living.getHealth() - living.getMaxHealth() / danDa$doHurtTarget5Float);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected InteractionResult mobInteract(Player p_35856_, InteractionHand p_35857_) {
        //Need to change
        if (this.isAlive() && !this.isTrading()) {
            if (p_35857_ == InteractionHand.MAIN_HAND) {
                p_35856_.awardStat(Stats.TALKED_TO_VILLAGER);
            }
            if (!this.getOffers().isEmpty()) {
                if (!this.level().isClientSide && !this.isAggressive()) {
                    this.setTradingPlayer(p_35856_);
                    this.openTradingScreen(p_35856_, this.getDisplayName(), 1);
                }
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        } else {
            return super.mobInteract(p_35856_, p_35857_);
        }
    }

    public float getDamageCape() {
        Difficulty difficulty = this.level().getCurrentDifficultyAt(this.blockPosition()).getDifficulty();
        if (difficulty.equals(Difficulty.PEACEFUL)) {
            return 20;
        } else if (difficulty.equals(Difficulty.EASY)) {
            return 8;
        } else if (difficulty.equals(Difficulty.NORMAL)) {
            return 5;
        } else {
            return 2;
        }
    }

    @Override
    public boolean hurt(DamageSource p_21016_, float p_21017_) {
        Entity entity = p_21016_.getEntity();
        if (!p_21016_.is(DamageTypeTags.BYPASSES_INVULNERABILITY)
                && p_21016_.getEntity() == null) {
            return false;
        }
        Vec3 p_21016_sourcePosition = p_21016_.getSourcePosition();
        if (p_21016_sourcePosition != null) {
            if (this.distanceToSqr(p_21016_.getSourcePosition()) > Maths.square(2.6)) {
                if (!this.isUsingItem()) {
                    this.startUsingItem(InteractionHand.OFF_HAND);
                    this.playSound(SoundEvents.SHIELD_BLOCK);
                }
                return false;
            }
        }
        if (this.hurtCooldown <= 0) {
            this.hurtCooldown = 10;
            if (source == null) {
                this.source = p_21016_.type();
            } else if (source1 == null) {
                this.source1 = p_21016_.type();
                if (this.source == p_21016_.type()) {
                    resetSouses();
                }
            } else if (source2 == null) {
                this.source2 = p_21016_.type();
                if (source != source1 && source1 != source2 && source != source2) {
                    this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.ENCHANTED_GOLDEN_APPLE));
                    this.startUsingItem(InteractionHand.MAIN_HAND);
                }
                resetSouses();
            }
            return super.hurt(p_21016_, p_21017_);
        } else return false;
    }

    @Override
    protected void actuallyHurt(DamageSource p_21240_, float p_21241_) {
        if (p_21241_ <= 0) {
            return;
        }
        if (!this.isAggressive()) {
            ++this.hurtCount;
        }
        if (this.hurtCount >= 3) {
            Entity entity = p_21240_.getEntity();
            if (entity instanceof LivingEntity living) {
                this.setTarget(living);
            }
        }
        super.actuallyHurt(p_21240_, p_21241_);
    }

    @Override
    public void setHealth(float p_21154_) {
        float health = this.getHealth();
        float delta = health - p_21154_;
        if (delta > this.getDamageCape()) {
            p_21154_ = health - this.getDamageCape();
        }
        super.setHealth(p_21154_);
    }

    protected void restockAll() {
        for (MerchantOffer merchantoffer : this.getOffers()) {
            merchantoffer.resetUses();
        }
    }

    private void resetSouses() {
        this.source = this.source1 = this.source2 = null;
    }

    @Override
    public void setTradingPlayer(@Nullable Player player) {
        this.tradingPlayer = player;
    }

    @Nullable
    @Override
    public Player getTradingPlayer() {
        return this.tradingPlayer;
    }

    @Override
    public MerchantOffers getOffers() {
        if (this.offers == null) {
            this.offers = new MerchantOffers();
            this.addTrades();
        }
        return this.offers;
    }

    private void addTrades() {
        Set<Integer> integers = new HashSet<>();
        VillagerTrades.ItemListing[] listings = DanDingTrades.DAN_DING_TRADES;
        if (listings.length > 50) {
            while (integers.size() < 50) {
                integers.add(this.random.nextInt(listings.length));
            }
        } else {
            for (int i = 0; i < listings.length; i++) {
                integers.add(i);
            }
        }
        for (int i : integers) {
            VillagerTrades.ItemListing listing = listings[i];
            MerchantOffer offer = listing.getOffer(this, this.getRandom());
            if (this.offers != null) {
                this.offers.add(offer);
            }
        }
    }

    public boolean isTrading() {
        return this.tradingPlayer != null;
    }

    @Override
    public void overrideOffers(MerchantOffers merchantOffers) {}

    @Override
    public void notifyTrade(MerchantOffer merchantOffer) {
        this.playSound(SoundEvents.PLAYER_LEVELUP);
    }

    @Override
    public void notifyTradeUpdated(ItemStack itemStack) {}

    @Override
    public int getVillagerXp() {
        return 0;
    }

    @Override
    public void overrideXp(int i) {
    }

    @Override
    public boolean showProgressBar() {
        return false;
    }

    @Override
    public SoundEvent getNotifyTradeSound() {
        return SoundEvents.VILLAGER_TRADE;
    }

    @Override
    public boolean isClientSide() {
        return this.level().isClientSide;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return NoixmodAPIAttributes.baseAttributes(1, 0.27, 0)
                .add(Attributes.MAX_HEALTH, 20).add(Attributes.FOLLOW_RANGE, Double.MAX_VALUE/2);
    }

    static {
        SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS,
                SensorType.NEAREST_ITEMS, SensorType.HURT_BY);
        MEMORY_MODULE_TYPES = ImmutableList.<MemoryModuleType<?>>of(MemoryModuleType.LOOK_TARGET,
                MemoryModuleType.DOORS_TO_CLOSE, MemoryModuleType.NEAREST_LIVING_ENTITIES,
                MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_PLAYER,
                MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER,
                MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS,
                MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM,
                MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS, MemoryModuleType.HURT_BY,
                MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.WALK_TARGET,
                        MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.ATTACK_TARGET,
                        MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.INTERACTION_TARGET,
                        MemoryModuleType.PATH, MemoryModuleType.ANGRY_AT, MemoryModuleType.UNIVERSAL_ANGER,
                        MemoryModuleType.AVOID_TARGET, MemoryModuleType.ADMIRING_ITEM, MemoryModuleType.
                        TIME_TRYING_TO_REACH_ADMIRE_ITEM, MemoryModuleType.ADMIRING_DISABLED,
                        MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM, MemoryModuleType.HUNTED_RECENTLY,
                        MemoryModuleType.NEAREST_VISIBLE_BABY_HOGLIN,
                        MemoryModuleType.NEAREST_VISIBLE_NEMESIS,
                        MemoryModuleType.NEAREST_PLAYERS, MemoryModuleType.NEAREST_REPELLENT);
    }

    private static class DanDaMeleeGoal extends ApiMeleeAttackGoal {
        final DanDa danDa;
        public DanDaMeleeGoal(DanDa finder) {
            super(finder, 1.5, Maths.square(2.6));
            this.danDa = finder;
        }

        @Override
        public void tick() {
            super.tick();
        }

        @Override
        public boolean canUse() {
            if (this.danDa.angryTick <= 0) {
                return false;
            }
            return super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            if (this.danDa.angryTick <= 0) {
                return false;
            }
            return super.canContinueToUse();
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity p_25557_, double p_25558_) {
            super.checkAndPerformAttack(p_25557_, p_25558_);
        }
    }
}
