
package com.bilibili.player_ix.noixmod_api.entities.monster;

import com.github.NineAbyss9.ix_api.api.ApiPose;
import com.github.NineAbyss9.ix_api.api.item.ItemStacks;
import com.github.NineAbyss9.ix_api.api.mobs.ApiBoss;
import com.github.NineAbyss9.ix_api.api.mobs.ApiPoseMob;
import com.github.NineAbyss9.ix_api.api.mobs.ApiRangedAttackMob;
import com.github.NineAbyss9.ix_api.api.mobs.OwnableMob;
import com.github.NineAbyss9.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiRangedBowAttackGoal;
import com.bilibili.player_ix.noixmod_api.entities.boss.EvokerIllager;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.APISpellcaster;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import com.bilibili.player_ix.noixmod_api.util.OwnerSummon;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class MoonKiller
extends APISpellcaster
implements ApiBoss, ApiRangedAttackMob, ApiPoseMob {
    private final OwnerSummon ownerSummon;
    private static final String aya = "aya";
    private int buffCooldown;
    private int teleportCooldown;
    public MoonKiller(EntityType<? extends MoonKiller> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.xpReward = 20;
        this.setItemInHand(InteractionHand.MAIN_HAND, ItemStacks.of(Items.BOW));
        this.ownerSummon = new OwnerSummon(this);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new CastingSpellGoal());
        this.goalSelector.addGoal(1, new ApiRangedBowAttackGoal(this,
                1, 10, 30f));
        this.goalSelector.addGoal(2, new VindicatorSpellGoal());
        OwnableMob.addBehaviorGoals(this, 5, 0.8, 12f, true, true);
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this, EvokerIllager.class).setAlertOthers());
        this.targetSelector.addGoal(1, new MobUtils.HostileNearestAttackableTargetGoal(this, false));
    }

    public void aiStep() {
        super.aiStep();
    }

    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.buffCooldown > 0) {
            --this.buffCooldown;
        }
        if (this.timeToTeleport()) {
            this.teleport();
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createMonsterAttributes().add(Attributes.MAX_HEALTH, 318).add(Attributes.ARMOR, 7)
                .add(Attributes.FOLLOW_RANGE, 72).add(Attributes.MOVEMENT_SPEED, 0.4)
                .add(Attributes.ATTACK_DAMAGE, 1).add(Attributes.KNOCKBACK_RESISTANCE, 0);
    }

    public ApiPose getPoses() {
        if (this.isCastingSpell()) {
            return ApiPose.SPELL_CASTING;
        }
        if (this.isAggressive()) {
            return ApiPose.BOW_AND_ARROW;
        }
        return ApiPose.NATURAL;
    }

    private boolean timeToTeleport() {
        return this.teleportCooldown <= 0 && this.level().getRandom().nextBoolean()
                && this.isAggressive();
    }

    public void addAdditionalSaveData(CompoundTag p_37870_) {
        super.addAdditionalSaveData(p_37870_);
    }

    public void readAdditionalSaveData(CompoundTag p_37862_) {
        super.readAdditionalSaveData(p_37862_);
    }

    public boolean isAya() {
        if (this.getCustomName() == null) {
            return false;
        }
        return this.getCustomName().getString().equals(aya);
    }

    public void teleport() {
        double x = Maths.randomBetween(this.random, 15f, -15f);
        double z = Maths.randomBetween(this.random, 15f, -15f);
        PrimedTnt tnt = new PrimedTnt(EntityType.TNT, this.level());
        tnt.moveTo(this.blockPosition(), 0, 0);
        this.level().addFreshEntity(tnt);
        this.randomTeleport(this.getX() + x, this.getY(), this.getZ() + z, false);
        this.handleTeleportCooldown();
    }

    private void handleTeleportCooldown() {
        this.teleportCooldown = 400;
    }

    public boolean hurt(DamageSource damageSource, float amount) {
        amount = Math.min(14f, amount);
        Entity entity = damageSource.getEntity();
        Entity directEntity = damageSource.getDirectEntity();
        if (entity instanceof PrimedTnt) {
            return false;
        }
        if (entity instanceof EvokerIllager || directEntity instanceof EvokerIllager) {
            return false;
        }
        return super.hurt(damageSource, amount);
    }

    public SoundEvent getCelebrateSound() {
        return SoundEvents.EMPTY;
    }

    public AbstractArrow getArrow(ItemStack stack, float pDistanceFactor) {
        Arrow arrow = new Arrow(this.level(), this);
        arrow.setEffectsFromItem(stack);
        arrow.setEnchantmentEffectsFromEntity(this, pDistanceFactor);
        arrow.setOwner(this);
        arrow.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 0));
        return arrow;
    }

    public void performRangedAttack(LivingEntity livingEntity, float v) {
        ItemStack itemstack = this.getProjectile(this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this,
                item -> item instanceof BowItem)));
        AbstractArrow arrow = this.getArrow(itemstack, v * 3);
        double[] d = this.ownerSummon.projectileDouble(livingEntity);
        arrow.shoot(d[0], d[1], d[2], 2.2f, 0.8f);
        this.playSound(SoundEvents.SKELETON_SHOOT);
        this.level().addFreshEntity(arrow);
    }

    private class VindicatorSpellGoal extends UseSpellGoal {

        protected void castSpell() {
            if (!level().isClientSide) {
                for (int i = 0;i < 2;i++) {
                    ServerLevel level = (ServerLevel) level();
                    var vindicator = EntityType.VINDICATOR.create(level);
                    if (vindicator==null) continue;
                    vindicator.moveTo(blockPosition().offset(Maths.randomInteger(3), 0, Maths.randomInteger(3)),
                            0, 0);
                    vindicator.finalizeSpawn(level, level.getCurrentDifficultyAt(blockPosition()), MobSpawnType.MOB_SUMMONED,
                            null, null);
                    vindicator.setTarget(getTarget());
                    level.addFreshEntityWithPassengers(vindicator);
                }
            }
        }

        protected int getCastingTime() {
            return 80;
        }

        protected int getCastingInterval() {
            return 480;
        }

        @Nullable
        protected SoundEvent getPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_SUMMON;
        }

        protected IllagerSpellType getSpellType() {
            return IllagerSpellType.RANGE;
        }
    }
}
