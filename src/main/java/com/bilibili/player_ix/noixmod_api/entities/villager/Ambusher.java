
package com.bilibili.player_ix.noixmod_api.entities.villager;

import com.github.NineAbyss9.ix_api.ix_api.api.item.ItemStacks;
import com.github.NineAbyss9.ix_api.ix_api.api.mobs.ApiRangedAttackMob;
import com.github.NineAbyss9.ix_api.ix_api.api.mobs.OwnableMob;
import com.github.NineAbyss9.ix_api.ix_api.util.Maths;
import com.github.NineAbyss9.ix_api.ix_api.util.Option;
import com.github.NineAbyss9.ix_api.ix_api.util.ParticleUtil;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiMeleeAttackGoal;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiRangedBowAttackGoal;
import com.bilibili.player_ix.noixmod_api.entities.villager.trades.ApiVillagerTrades;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPITags;
import com.bilibili.player_ix.noixmod_api.util.OwnerSummon;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class Ambusher extends VillagerFighter implements ApiRangedAttackMob {
    private static final EntityDataAccessor<Integer> DATA_EAT_TIME;
    private final OwnerSummon ownerSummon;
    public Ambusher(EntityType<Ambusher> type, Level level) {
        super(type, level);
        this.ownerSummon = new OwnerSummon(this);
        ItemStack stack = Option.of(ItemStacks.of(Items.BOW)).ifOrElse(level.random.nextBoolean(),
                ItemStacks.of(Items.IRON_SWORD));
        this.setItemInHand(InteractionHand.MAIN_HAND, stack);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_EAT_TIME, 0);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new ApiRangedBowAttackGoal(this,
                1, 10, 20F, stack -> (stack.is(Items.BOW))));
        this.goalSelector.addGoal(1, new AmbusherMeleeGoal(this));
        OwnableMob.addBehaviorGoals(this, 4, 0.8, 10F, true, true);
        this.targetSelector.addGoal(1,
                new HurtByTargetGoal(this, VillagerFighter.class).setAlertOthers());
    }

    public void tick() {
        super.tick();
        if (this.level().isClientSide && isEating()) {
            Vec3 eyePosition = this.getEyePosition();
            this.level().addParticle(ParticleUtil.getItemParticleOption(getOffhandItem()), this.getRandomX(0.8),
                    eyePosition.y - 0.1, getRandomZ(0.8), 0, 0.2, 0);
        }
    }

    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            if (isBread()) {
                this.increaseEatTime();
                if (this.tickCount % 5 == 0) {
                    this.playSound(SoundEvents.GENERIC_EAT);
                }
                if (this.getEatTime() >= 50) {
                    this.getOffhandItem().shrink(1);
                    heal(3f);
                    resetEatTime();
                }
            }
            if (this.getHealth() < this.getMaxHealth()) {
                if (!isBread()) {
                    this.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(Items.BREAD));
                }
            }
            if (this.tickCount % 80 == 0 && this.isInvisible()) {
                heal(1f);
            }
        }
    }

    public boolean isInvisible() {
        if (this.getFeetBlockState().is(NoixmodAPITags.AMBUSHER_CAN_HIDE)) {
            return true;
        }
        return super.isInvisible();
    }

    private boolean isBread() {
        return this.getOffhandItem().is(Items.BREAD);
    }

    public boolean isEating() {
        return this.getEatTime() > 0;
    }

    private int getEatTime() {
        return this.entityData.get(DATA_EAT_TIME);
    }

    private void resetEatTime() {
        this.setEatTime(0);
    }

    private void setEatTime(int time) {
        this.entityData.set(DATA_EAT_TIME, time);
    }

    private void increaseEatTime() {
        this.setEatTime(this.getEatTime() + 1);
    }

    protected SoundEvent getCastingSoundEvent() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    @Nullable
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return NoixmodAPIEntities.AMBUSHER.get().create(serverLevel);
    }

    public VillagerFighterArmPose getArmPose() {
        if (this.isAggressive()) {
            if (this.getMainHandItem().is(Items.BOW)) {
                return VillagerFighterArmPose.BOW_AND_ARROW;
            }
            if (this.getMainHandItem().getItem() instanceof SwordItem) {
                return VillagerFighterArmPose.ATTACKING;
            }
        } else if (this.isEating()) {
            return VillagerFighterArmPose.NATURAL;
        }
        return VillagerFighterArmPose.CROSSED;
    }

    protected VillagerTrades.ItemListing[] getTradeLists() {
        return ApiVillagerTrades.AMBUSHER_TRADES;
    }

    private boolean checkBow() {
        return this.getMainHandItem().is(Items.BOW);
    }

    public AbstractArrow getArrow(ItemStack stack, float pDistanceFactor) {
        Arrow arrow = new Arrow(this.level(), this);
        arrow.setCritArrow(this.random.nextFloat() <= 0.05f);
        arrow.setEffectsFromItem(stack);
        arrow.setEnchantmentEffectsFromEntity(this, pDistanceFactor);
        arrow.setOwner(this);
        if (this.isOnFire()) {
            arrow.setSecondsOnFire(40);
        }
        return arrow;
    }

    public void performRangedAttack(LivingEntity livingEntity, float v) {
        double x = this.ownerSummon.projectileDouble(livingEntity)[0];
        double y = this.ownerSummon.projectileDouble(livingEntity)[1];
        double z = this.ownerSummon.projectileDouble(livingEntity)[2];
        ItemStack stack = this.getProjectile(this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this,
                item -> item instanceof BowItem)));
        AbstractArrow arrow = this.getArrow(stack, v * 4);
        if (this.getMainHandItem().getItem() instanceof BowItem bow) {
            arrow = bow.customArrow(arrow);
        }
        arrow.shoot(x, y, z, 1.6f, 14 - this.level().getDifficulty().getId() * 4);
        this.playSound(SoundEvents.SKELETON_SHOOT);
        this.level().addFreshEntity(arrow);
    }

    static {
        DATA_EAT_TIME = SynchedEntityData.defineId(Ambusher.class, EntityDataSerializers.INT);
    }

    private static class AmbusherMeleeGoal extends ApiMeleeAttackGoal {
        final Ambusher ambusher;
        public AmbusherMeleeGoal(Ambusher finder) {
            super(finder, 1.2, Maths.square(2));
            this.ambusher = finder;
        }

        public boolean canUse() {
            if (this.ambusher.checkBow()){
                return false;
            }
            return super.canUse();
        }

        public boolean canContinueToUse() {
            if (this.ambusher.checkBow()) {
                return false;
            }
            return super.canContinueToUse();
        }
    }
}
