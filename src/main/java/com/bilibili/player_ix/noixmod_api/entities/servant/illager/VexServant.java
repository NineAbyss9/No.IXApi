
package com.bilibili.player_ix.noixmod_api.entities.servant.illager;

import com.bilibili.player_ix.noixmod_api.api.entity.IVex;
import com.bilibili.player_ix.noixmod_api.entities.servant.nether.NetherSoul;
import com.github.NineAbyss9.ix_api.api.item.ItemStacks;
import com.github.NineAbyss9.ix_api.api.mobs.IFlagMob;
import com.github.NineAbyss9.ix_api.api.mobs.OwnableMob;
import com.bilibili.player_ix.noixmod_api.entities.ai.control.FlyingVexMoveControl;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class VexServant
extends OwnableMob
implements IFlagMob, IVex {
    private static final EntityDataAccessor<Integer> DATA_FLAGS;
    private static final int TICKS_PER_FLAP = Mth.ceil(3.9269907F);
    public VexServant(EntityType<? extends VexServant> entityType, Level level) {
        super(entityType, level);
        this.setItemInHand(InteractionHand.MAIN_HAND, ItemStacks.of(Items.IRON_SWORD));
        this.enchantSpawnedWeapon(level.random, 1.0F);
        this.moveControl = new FlyingVexMoveControl(this);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS, 0);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(6, new VexChargeAttackGoal<>(this));
        this.goalSelector.addGoal(8, new VexArcher.VexRandomMoveGoal(this));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, LivingEntity.class,
                20f));
        addTargetGoal();
    }

    public void tick() {
        this.noPhysics = true;
        super.tick();
        this.noPhysics = false;
        this.setNoGravity(true);
        if (this.hasLife() && this.getLifeTick() <= 0) {
            this.hurt(this.damageSources().starve(), 1.0f);
        }
    }

    public void move(MoverType pType, Vec3 pPos) {
        super.move(pType, pPos);
        this.checkInsideBlocks();
    }

    public int getFlag() {
        return this.entityData.get(DATA_FLAGS);
    }

    public void setFlag(int flag) {
        this.entityData.set(DATA_FLAGS, flag);
    }

    protected boolean isFlapping() {
        return this.tickCount % TICKS_PER_FLAP == 0;
    }

    protected float getStandingEyeHeight(Pose p_260180_, EntityDimensions p_260049_) {
        return p_260049_.height - 0.28125F;
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

    public static AttributeSupplier.Builder createAttributes() {
        return createPathAttributes().add(Attributes.FOLLOW_RANGE, 64).add(Attributes.MAX_HEALTH,
                        14).add(Attributes.ATTACK_DAMAGE, 7)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0);
    }

    static {
        DATA_FLAGS = SynchedEntityData.defineId(VexServant.class, EntityDataSerializers.INT);
    }

    public static class VexChargeAttackGoal<T extends Mob & IFlagMob & IVex> extends Goal {
        protected final T mob;
        public VexChargeAttackGoal(T pMob) {
            mob = pMob;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            LivingEntity target = mob.getTarget();
            if (target != null && target.isAlive() && !mob.getMoveControl().hasWanted() &&
                    mob.getRandom().nextInt(reducedTickDelay(7)) == 0) {
                return mob.distanceToSqr(target) > 4.0D;
            } else {
                return false;
            }
        }

        public boolean canContinueToUse() {
            return mob.getMoveControl().hasWanted() && mob.getFlag() == 1 &&
                    mob.getTarget() != null && mob.getTarget().isAlive();
        }

        public void start() {
            LivingEntity $$0 = mob.getTarget();
            if ($$0 != null) {
                Vec3 $$1 = $$0.getEyePosition();
                mob.getMoveControl().setWantedPosition($$1.x, $$1.y, $$1.z, 1.0D);
            }
            mob.setFlag(1);
            mob.playSound(this.mob.getChargeSound(), 1.0F, 1.0F);
        }

        public void stop() {
            this.mob.resetFlag();
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity target = mob.getTarget();
            if (target == null) {
                return;
            }
            if (this.mob.getBoundingBox().intersects(target.getBoundingBox())) {
                this.mob.doHurtTarget(target);
                if (!(this.mob instanceof NetherSoul)) {
                    this.mob.setFlag(0);
                }
            } else {
                double sqr = mob.distanceToSqr(target);
                if (sqr < 9.0D) {
                    Vec3 $$2 = target.getEyePosition();
                    mob.getMoveControl().setWantedPosition($$2.x, $$2.y - 1.0D, $$2.z, 1.0D);
                }
            }
        }
    }
}
