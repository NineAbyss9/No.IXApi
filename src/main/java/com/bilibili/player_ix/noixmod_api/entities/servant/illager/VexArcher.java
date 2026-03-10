
package com.bilibili.player_ix.noixmod_api.entities.servant.illager;

import com.github.NineAbyss9.ix_api.api.item.ItemStacks;
import com.github.NineAbyss9.ix_api.api.mobs.ApiRangedAttackMob;
import com.github.NineAbyss9.ix_api.api.mobs.Ownable;
import com.github.NineAbyss9.ix_api.api.mobs.OwnableMob;
import com.github.NineAbyss9.ix_api.util.Vec9;
import com.bilibili.player_ix.noixmod_api.compat.goety.GoetyCompat;
import com.bilibili.player_ix.noixmod_api.entities.ai.control.FlyingVexMoveControl;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiRangedBowAttackGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.EnumSet;

public class VexArcher extends OwnableMob implements ApiRangedAttackMob {
    private static final int TICKS_PER_FLAP = Mth.ceil(3.9269907F);
    public VexArcher(EntityType<? extends VexArcher> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
        this.moveControl = new FlyingVexMoveControl(this);
        ItemStack stack = ItemStacks.of(Items.BOW);
        this.setItemInHand(InteractionHand.MAIN_HAND, stack);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(4, new ApiRangedBowAttackGoal(this, 1.5, 14,
                20f) {
            public boolean checkSee() {
                return true;
            }
        });
        this.goalSelector.addGoal(8, new VexRandomMoveGoal(this));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, LivingEntity.class, 20f));
        this.targetSelector.addGoal(0, new OwnerHurtTargetGoal<>(this));
        this.targetSelector.addGoal(0, new OwnableTargetGoal<>(this, false));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    public void tick() {
        this.noPhysics = true;
        super.tick();
        this.setNoGravity(true);
        if (this.hasLife() && this.getLifeTick() <= 0) {
            this.hurt(this.damageSources().starve(), 1.0f);
        }
    }

    public void aiStep() {
        super.aiStep();
    }

    public void move(MoverType p_19973_, Vec3 p_19974_) {
        super.move(p_19973_, p_19974_);
        this.checkInsideBlocks();
    }

    protected boolean isFlapping() {
        return this.tickCount % TICKS_PER_FLAP ==0;
    }

    protected float getStandingEyeHeight(Pose p_260180_, EntityDimensions p_260049_) {
        return p_260049_.height - 0.28125F;
    }

    protected void dropCustomDeathLoot(DamageSource p_21385_, int p_21386_, boolean p_21387_) {
        if (GoetyCompat.goetyLoaded()) {
            if (this.level().getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
                this.spawnAtLocation(GoetyCompat.getItemStack("raging_matter"), 2);
            }
        }
        super.dropCustomDeathLoot(p_21385_, p_21386_, p_21387_);
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return SoundEvents.VEX_AMBIENT;
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource p_21239_) {
        return SoundEvents.VEX_HURT;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return SoundEvents.VEX_DEATH;
    }

    public AbstractArrow getArrow(ItemStack stack, float pDistanceFactor) {
        Arrow arrow = new Arrow(this.level(), this);
        Collection<MobEffectInstance> instances = this.getActiveEffects();
        if (!instances.isEmpty()) {
            for (MobEffectInstance instance : instances) {
                arrow.addEffect(instance);
            }
        }
        arrow.setOwner(ownerOrThis(this, this));
        arrow.setEnchantmentEffectsFromEntity(this, pDistanceFactor);
        arrow.setEffectsFromItem(stack);
        return arrow;
    }

    public void performRangedAttack(LivingEntity livingEntity, float v) {
        ItemStack itemstack = this.getProjectile(this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this,
                item -> item instanceof BowItem)));
        AbstractArrow arrow = this.getArrow(itemstack, v * 3);
        if (this.getMainHandItem().getItem() instanceof BowItem bow) {
            arrow = bow.customArrow(arrow);
        }
        double $$4 = livingEntity.getX() - this.getX();
        double $$5 = livingEntity.getY(0.5) - this.getY(0.5);
        double $$6 = livingEntity.getZ() - this.getZ();
        float speed = 2.2f;
        arrow.shoot($$4, $$5, $$6, speed, 0.8f);
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0f, 1.0f /
                (this.getRandom().nextFloat() * 0.4f + 0.8f));
        this.level().addFreshEntity(arrow);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createPathAttributes().add(Attributes.FOLLOW_RANGE, 64).add(Attributes.MAX_HEALTH,
                        14).add(Attributes.ATTACK_DAMAGE, 2)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0).add(Attributes.FLYING_SPEED, 1);
    }

    public static class VexRandomMoveGoal extends Goal {
        protected final Mob mob;
        protected Vec9 last = Vec9.of();
        public VexRandomMoveGoal(Mob pmob) {
            this.mob = pmob;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        public boolean canUse() {
            if (!mob.getNavigation().isDone()) {
                return false;
            }
            return checkRandom();
        }

        protected boolean checkRandom() {
            return mob.getRandom().nextInt(reducedTickDelay(7)) == 0;
        }

        public boolean canContinueToUse() {
            return false;
        }

        public void tick() {
            BlockPos pos = this.mob.blockPosition();
            if (this.mob instanceof Ownable ownable && ownable.getOwner() != null) {
                pos = ownable.getOwner().blockPosition();
            }
            for (int $$1 = 0; $$1 < 3; ++$$1) {
                BlockPos $$2 = pos.offset(this.mob.getRandom().nextInt(15) - 7, this.mob
                        .getRandom().nextInt(11) - 5, this.mob.getRandom().nextInt(15) - 7);
                if (this.mob.level().isEmptyBlock($$2)) {
                    this.mob.getMoveControl().setWantedPosition($$2.getX() + 0.5, $$2.getY() + 0.5,
                            $$2.getZ() + 0.5, 0.3);
                    last = Vec9.of($$2);
                    if (this.mob.getTarget() == null) {
                        this.mob.getLookControl().setLookAt($$2.getX() + 0.5, $$2.getY() + 0.5,
                                $$2.getZ() + 0.5, 180.0F, 20.0F);
                    }
                    break;
                }
            }
        }

        //public void tick() {

        //}
    }
}
