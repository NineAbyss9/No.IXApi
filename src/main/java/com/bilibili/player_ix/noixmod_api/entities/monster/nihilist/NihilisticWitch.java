
package com.bilibili.player_ix.noixmod_api.entities.monster.nihilist;

import com.github.NineAbyss9.ix_api.api.mobs.ApiPathfinderMob;
import com.github.NineAbyss9.ix_api.api.mobs.OwnableMob;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.Nihilist;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class NihilisticWitch
extends Nihilist
implements RangedAttackMob, Enemy {
    private static final UUID SPEED_MODIFIER_DRINKING_UUID = UUID.fromString(
            "5CD17E52-A79A-43D3-A529-90FDE04B181B");
    private static final AttributeModifier SPEED_MODIFIER_DRINKING;
    private static final EntityDataAccessor<Boolean> DATA_USING_ITEM;
    private int usingTime;
    private NearestAttackableWitchTargetGoal<LivingEntity> attackPlayersGoal;
    private NearestHealableRaiderTargetGoal<Nihilist> healRaidersGoal;
    public NihilisticWitch(EntityType<? extends NihilisticWitch> entityType, Level level) {
        super(entityType, level);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_USING_ITEM, false);
    }

    protected void registerGoals() {
        this.healRaidersGoal = new NearestHealableRaiderTargetGoal<>(this, Nihilist.class,
                false, (entity) -> true);
        this.attackPlayersGoal = new NearestAttackableWitchTargetGoal<>(this, LivingEntity.class,
                500, false, false, (living) -> MobUtils.canHurt(living, this));
        super.registerGoals();
        OwnableMob.addBehaviorGoals(this, 3, 0.8F, 15F, true, false);
        this.targetSelector.addGoal(2, healRaidersGoal);
        this.targetSelector.addGoal(2, attackPlayersGoal);
    }

    public void aiStep() {
        if (!this.level().isClientSide && this.isAlive()) {
            this.healRaidersGoal.decrementCooldown();
            this.attackPlayersGoal.setCanAttack(this.healRaidersGoal.getCooldown() <= 0);
            if (this.isDrinkingPotion()) {
                if (this.usingTime-- <= 0) {
                    this.setUsingItem(false);
                    ItemStack $$0 = this.getMainHandItem();
                    this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                    if ($$0.is(Items.POTION)) {
                        List<MobEffectInstance> $$1 = PotionUtils.getMobEffects($$0);
                        for (MobEffectInstance $$2 : $$1) {
                            this.addEffect(new MobEffectInstance($$2));
                        }
                    }
                    this.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(SPEED_MODIFIER_DRINKING);
                }
            } else {
                Potion $$3 = null;
                if (this.random.nextFloat() < 0.15F && this.isInWater() && !this.hasEffect(MobEffects.WATER_BREATHING)) {
                    $$3 = Potions.WATER_BREATHING;
                } else if (this.random.nextFloat() < 0.15F && (this.isOnFire() || this.getLastDamageSource() != null
                        && this.getLastDamageSource().is(DamageTypeTags.IS_FIRE)) &&
                        !this.hasEffect(MobEffects.FIRE_RESISTANCE)) {
                    $$3 = Potions.FIRE_RESISTANCE;
                } else if (this.random.nextFloat() < 0.05F && this.getHealth() < this.getMaxHealth()) {
                    $$3 = Potions.HEALING;
                } else if (this.random.nextFloat() < 0.5F && this.getTarget() != null && !this.hasEffect(
                        MobEffects.MOVEMENT_SPEED) && this.getTarget().distanceToSqr(this) > 121.0) {
                    $$3 = Potions.SWIFTNESS;
                }
                if ($$3 != null) {
                    this.setItemSlot(EquipmentSlot.MAINHAND, PotionUtils.setPotion(new ItemStack(Items.POTION), $$3));
                    this.usingTime = this.getMainHandItem().getUseDuration();
                    this.setUsingItem(true);
                    if (!this.isSilent()) {
                        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.WITCH_DRINK,
                                this.getSoundSource(), 1.0F, 0.8F + this.random.nextFloat() * 0.4F);
                    }
                    AttributeInstance $$4 = this.getAttribute(Attributes.MOVEMENT_SPEED);
                    if ($$4 != null) {
                        $$4.removeModifier(SPEED_MODIFIER_DRINKING);
                        $$4.addTransientModifier(SPEED_MODIFIER_DRINKING);
                    }
                }
            }
            if (this.random.nextFloat() < 7.5E-4F) {
                this.level().broadcastEntityEvent(this, (byte)15);
            }
        }
        super.aiStep();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return ApiPathfinderMob.createPathAttributes().add(Attributes.MAX_HEALTH, 30)
                .add(Attributes.ARMOR, 2)
                .add(Attributes.MOVEMENT_SPEED, 0.3).add(Attributes.FOLLOW_RANGE, 120);
    }

    protected float getStandingEyeHeight(Pose p_34146_, EntityDimensions p_34147_) {
        return 1.62F;
    }

    public boolean isDrinkingPotion() {
        return this.entityData.get(DATA_USING_ITEM);
    }

    public void setUsingItem(boolean b) {
        this.entityData.set(DATA_USING_ITEM, b);
    }

    public void performRangedAttack(LivingEntity p_34143_, float v) {
        if (this.isDrinkingPotion()) {
            return;
        }
        Vec3 $$2 = p_34143_.getDeltaMovement();
        double $$3 = p_34143_.getX() + $$2.x - this.getX();
        double $$4 = p_34143_.getEyeY() - 1.100000023841858 - this.getY();
        double $$5 = p_34143_.getZ() + $$2.z - this.getZ();
        double $$6 = Math.sqrt($$3 * $$3 + $$5 * $$5);
        Potion $$7 = Potions.HARMING;
        if (p_34143_ instanceof Raider) {
            if (p_34143_.getHealth() <= 4.0F) {
                $$7 = Potions.HEALING;
            } else {
                $$7 = Potions.REGENERATION;
            }
            this.setTarget(null);
        } else if ($$6 >= 8.0 && !p_34143_.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
            $$7 = Potions.SLOWNESS;
        } else if (p_34143_.getHealth() >= 8.0F && !p_34143_.hasEffect(MobEffects.POISON)) {
            $$7 = Potions.POISON;
        } else if ($$6 <= 3.0 && !p_34143_.hasEffect(MobEffects.WEAKNESS) && this.random.nextFloat() < 0.25F) {
            $$7 = Potions.WEAKNESS;
        }
        ThrownPotion $$8 = new ThrownPotion(this.level(), this);
        $$8.setItem(PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), $$7));
        $$8.setXRot($$8.getXRot() - -20.0F);
        $$8.shoot($$3, $$4 + $$6 * 0.2, $$5, 0.75F, 8.0F);
        if (!this.isSilent()) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.WITCH_THROW, this.getSoundSource(), 1.0F, 0.8F + this.random.nextFloat() * 0.4F);
        }
        this.level().addFreshEntity($$8);
    }

    static {
        SPEED_MODIFIER_DRINKING = new AttributeModifier(SPEED_MODIFIER_DRINKING_UUID, "Drinking speed penalty", -0.25, AttributeModifier.Operation.ADDITION);
        DATA_USING_ITEM = SynchedEntityData.defineId(NihilisticWitch.class, EntityDataSerializers.BOOLEAN);
    }

    public static class NearestAttackableWitchTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
        private boolean canAttack = true;

        public NearestAttackableWitchTargetGoal(Mob p_26076_, Class<T> p_26077_, int p_26078_, boolean p_26079_, boolean p_26080_, @Nullable Predicate<LivingEntity> p_26081_) {
            super(p_26076_, p_26077_, p_26078_, p_26079_, p_26080_, p_26081_);
        }

        public void setCanAttack(boolean p_26084_) {
            this.canAttack = p_26084_;
        }

        public boolean canUse() {
            return this.canAttack && super.canUse();
        }
    }

    public static class NearestHealableRaiderTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
        private static final int DEFAULT_COOLDOWN = 200;
        private int cooldown = 0;

        public NearestHealableRaiderTargetGoal(Mob p_26087_, Class<T> p_26088_, boolean p_26089_,
                                               @Nullable Predicate<LivingEntity> p_26090_) {
            super(p_26087_, p_26088_, 500, p_26089_, false, p_26090_);
        }

        public int getCooldown() {
            return this.cooldown;
        }

        public void decrementCooldown() {
            --this.cooldown;
        }

        public boolean canUse() {
            if (this.cooldown <= 0 && this.mob.getRandom().nextBoolean()) {
                this.findTarget();
                return this.target != null;
            } else {
                return false;
            }
        }

        public void start() {
            this.cooldown = reducedTickDelay(200);
            super.start();
        }
    }
}
