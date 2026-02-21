
package com.bilibili.player_ix.noixmod_api.entities.servant.end;

import com.github.NineAbyss9.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiMeleeAttackGoal;
import com.bilibili.player_ix.noixmod_api.util.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.EntityTeleportEvent;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;

public class EnderManServant
extends AbstractEndServant {
    @Nullable
    protected Creeper heldCreeper;
    @Nullable
    protected LivingEntity heldMob;
    protected static final EntityDataAccessor<Boolean> DATA_IS_CREEPY;
    protected static final EntityDataAccessor<Boolean> DATA_HOLDING_STATE;
    public EnderManServant(EntityType<? extends EnderManServant> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1F);
    }

    protected void defineSynchedData() {
        this.entityData.define(DATA_IS_CREEPY, false);
        this.entityData.define(DATA_HOLDING_STATE, false);
        super.defineSynchedData();
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new EndermanFreezeWhenLookedAt(this));
        this.goalSelector.addGoal(1, new TeleportAndIgniteGoal(this));
        this.goalSelector.addGoal(1, new EndermanHoldMobGoal(this));
        this.goalSelector.addGoal(2, new EndermanAttackGoal(this, 1));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this,
                1.0, 0.0F));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, LivingEntity.class, 10F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new OwnerHurtTargetGoal<>(this));
        this.targetSelector.addGoal(1, new OwnableTargetGoal<>(this, false));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this,
                Endermite.class, false));
    }

    public void aiStep() {
        if (this.level().isClientSide) {
            for(int i = 0; i < 2; ++i) {
                if (this.getAmbientParticle() != null) {
                    this.level().addParticle(this.getAmbientParticle(), this.getRandomX(0.5),
                            this.getRandomY() - 0.25, this.getRandomZ(0.5),
                            (this.random.nextDouble() - 0.5) * 2.0,
                            -this.random.nextDouble(), (this.random.nextDouble() - 0.5) * 2.0);
                }
            }
        }
        if (this.isInWaterRainOrBubble()) {
            this.hurt(this.damageSources().drown(), 1F);
        }
        this.jumping = false;
        super.aiStep();
    }

    @Nullable
    public ParticleOptions getAmbientParticle() {
        return ParticleTypes.PORTAL;
    }

    protected float getStandingEyeHeight(Pose p_32517_, EntityDimensions p_32518_) {
        return 2.55F;
    }

    public boolean isSensitiveToWater() {
        return true;
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return this.isCreepy() ? SoundEvents.ENDERMAN_SCREAM : SoundEvents.ENDERMAN_AMBIENT;
    }

    public void rideTick() {
        this.stopRiding();
    }

    protected boolean canRide(Entity p_20339_) {
        return false;
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource p_21239_) {
        return SoundEvents.ENDERMAN_HURT;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENDERMAN_DEATH;
    }

    public MoveControl getMoveControl() {
        return this.moveControl;
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (this.isInvulnerableTo(pSource)) {
            return false;
        } else {
            boolean flag = pSource.getDirectEntity() instanceof ThrownPotion;
            boolean flag1;
            if (!pSource.is(DamageTypeTags.IS_PROJECTILE) && !flag) {
                flag1 = super.hurt(pSource, pAmount);
                if (!this.level().isClientSide() && !(pSource.getEntity() instanceof LivingEntity) && this.random.nextInt(10) != 0) {
                    this.teleport();
                }
            } else {
                flag1 = flag && this.hurtWithCleanWater(pSource, (ThrownPotion)pSource.getDirectEntity(), pAmount);
                for(int i = 0; i < 64; ++i) {
                    if (this.teleport()) {
                        return true;
                    }
                }
            }
            return flag1;
        }
    }

    public boolean hurtWithCleanWater(DamageSource p_186273_, ThrownPotion p_186274_, float p_186275_) {
        ItemStack itemstack = p_186274_.getItem();
        Potion potion = PotionUtils.getPotion(itemstack);
        List<MobEffectInstance> list = PotionUtils.getMobEffects(itemstack);
        boolean flag = potion == Potions.WATER && list.isEmpty();
        return flag && super.hurt(p_186273_, p_186275_);
    }

    public void setTarget(@Nullable LivingEntity p_21544_) {
        if (p_21544_ instanceof Creeper) {
            return;
        }
        this.setCreepy(p_21544_ != null);
        super.setTarget(p_21544_);
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.getHeldMob() != null;
    }

    @Nullable
    public LivingEntity getHeldMob() {
        return this.heldMob;
    }

    public void setHeldMob(@Nullable LivingEntity holding) {
        this.heldMob = holding;
    }

    public void setHoldingState(boolean flag) {
        this.entityData.set(DATA_HOLDING_STATE, flag);
    }

    public boolean isHolding() {
        return this.getHeldMob() != null || this.entityData.get(DATA_HOLDING_STATE);
    }

    public boolean isCreepy() {
        return this.entityData.get(DATA_IS_CREEPY);
    }

    public void setCreepy(boolean b) {
        this.entityData.set(DATA_IS_CREEPY, b);
    }

    public boolean isLookingAtMe(Player p_32535_) {
        ItemStack itemstack = p_32535_.getInventory().armor.get(3);
        if (itemstack.getItem() == Blocks.CARVED_PUMPKIN.asItem() || this.getOwner() instanceof Player) {
            return false;
        } else {
            Vec3 vec3 = p_32535_.getViewVector(1.0F).normalize();
            Vec3 vec31 = new Vec3(this.getX() - p_32535_.getX(), this.getEyeY() - p_32535_.getEyeY(),
                    this.getZ() - p_32535_.getZ());
            double d0 = vec31.length();
            vec31 = vec31.normalize();
            double d1 = vec3.dot(vec31);
            return d1 > 1.0 - 0.025 / d0 && p_32535_.hasLineOfSight(this);
        }
    }

    public boolean teleportTowards(Entity p_32501_) {
        Vec3 vec3 = new Vec3(this.getX() - p_32501_.getX(), this.getY(0.5) -
                p_32501_.getEyeY(), this.getZ() - p_32501_.getZ());
        vec3 = vec3.normalize();
        double d1 = this.getX() + (this.random.nextDouble()) - 0.5 * 8.0 - vec3.x * 16.0;
        double d2 = this.getY() + this.random.nextInt(16) - 8 - vec3.y * 16.0;
        double d3 = this.getZ() + (this.random.nextDouble() - 0.5) * 8.0 - vec3.z * 16.0;
        return this.teleport(d1, d2, d3);
    }

    public boolean teleport() {
        if (!this.level().isClientSide() && this.isAlive()) {
            double d0 = this.getX() + (this.random.nextDouble() - 0.5) * 64.0;
            double d1 = this.getY() + (double)(this.random.nextInt(64) - 32);
            double d2 = this.getZ() + (this.random.nextDouble() - 0.5) * 64.0;
            return this.teleport(d0, d1, d2);
        } else {
            return false;
        }
    }

    public boolean teleport(double p_32544_, double p_32545_, double p_32546_) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(p_32544_,
                p_32545_, p_32546_);
        while (blockpos$mutableblockpos.getY() > this.level().getMinBuildHeight() && !BlockUtil
                .isMotion(this.level().getBlockState(blockpos$mutableblockpos))) {
            blockpos$mutableblockpos.move(Direction.DOWN);
        }
        BlockState blockstate = this.level().getBlockState(blockpos$mutableblockpos);
        boolean flag = BlockUtil.isMotion(blockstate);
        boolean flag1 = blockstate.getFluidState().is(FluidTags.WATER);
        if (flag && !flag1) {
            EntityTeleportEvent.EnderEntity event = ForgeEventFactory.onEnderTeleport(
                    this, p_32544_, p_32545_, p_32546_);
            if (event.isCanceled()) {
                return false;
            } else {
                Vec3 vec3 = this.position();
                boolean flag2 = this.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(),
                        true);
                if (flag2) {
                    this.level().gameEvent(GameEvent.TELEPORT, vec3, GameEvent.Context.of(this));
                    if (!this.isSilent()) {
                        this.level().playSound(null, this.xo, this.yo, this.zo, SoundEvents.ENDERMAN_TELEPORT,
                                this.getSoundSource(), 1.0F, 1.0F);
                        this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                    }
                }
                return flag2;
            }
        } else {
            return false;
        }
    }

    static {
        DATA_IS_CREEPY = SynchedEntityData.defineId(EnderManServant.class, EntityDataSerializers.BOOLEAN);
        DATA_HOLDING_STATE = SynchedEntityData.defineId(EnderManServant.class, EntityDataSerializers.BOOLEAN);
    }

    protected static class EndermanAttackGoal
    extends ApiMeleeAttackGoal {
        protected final EnderManServant servant;
        public EndermanAttackGoal(EnderManServant finder, double speed) {
            super(finder, speed);
            this.servant = finder;
        }

        public void start() {
            this.servant.setCreepy(true);
            super.start();
        }

        public void stop() {
            this.servant.setCreepy(false);
            super.stop();
        }

        protected double getAttackReachSqr(LivingEntity p_25556_) {
            return Maths.square(2.75);
        }

        public boolean canUse() {
            if (this.servant.isHolding()) {
                return false;
            }
            return super.canUse();
        }

        public boolean canContinueToUse() {
            if (this.servant.isHolding()) {
                return false;
            }
            return super.canContinueToUse();
        }
    }

    protected static class EndermanFreezeWhenLookedAt extends Goal {
        private final EnderManServant enderman;
        @Nullable
        protected LivingEntity target;

        public EndermanFreezeWhenLookedAt(EnderManServant p_32550_) {
            this.enderman = p_32550_;
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
        }

        public boolean canUse() {
            this.target = this.enderman.getTarget();
            if (!(this.target instanceof Player)) {
                return false;
            } else {
                double d0 = this.target.distanceToSqr(this.enderman);
                return !(d0 > 256.0) && this.enderman.isLookingAtMe((Player) this.target);
            }
        }

        public void start() {
            this.enderman.getNavigation().stop();
        }

        public void tick() {
            if (this.target != null) {
                this.enderman.getLookControl().setLookAt(this.target.getX(), this.target.getEyeY(), this.target.getZ());
            }
        }
    }

    protected static class EndermanHoldMobGoal extends Goal {
        protected final EnderManServant enderman;
        public EndermanHoldMobGoal(EnderManServant p_32585_) {
            this.enderman = p_32585_;
        }

        public boolean canUse() {
            if (this.enderman.isHolding()) {
                return false;
            } else if (!this.enderman.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                return false;
            } else {
                return this.enderman.getRandom().nextInt(10) == 0;
            }
        }

        public void tick() {
            Level level = this.enderman.level();
            List<LivingEntity> livingEntities = level.getEntitiesOfClass(LivingEntity.class, this.enderman.getBoundingBox()
                    .inflate(4));
            if (!livingEntities.isEmpty()) {
                for (LivingEntity living : livingEntities) {
                    if (living instanceof Creeper creeper) {
                        this.enderman.setHeldMob(creeper);
                        this.enderman.heldCreeper = creeper;
                    } else if (!(living instanceof EnderManServant)) {
                        this.enderman.setHeldMob(living);
                    }
                    this.enderman.setHoldingState(true);
                    living.startRiding(this.enderman);
                    this.stop();
                    break;
                }
            }
        }
    }

    protected static class TeleportAndIgniteGoal
    extends Goal {
        protected final EnderManServant servant;
        public TeleportAndIgniteGoal(EnderManServant enderManServant) {
            this.servant = enderManServant;
        }

        public void start() {
            LivingEntity target = this.servant.getTarget();
            Creeper creeper = this.servant.heldCreeper;
            if (target != null && creeper != null) {
                if (this.servant.teleportTowards(target)) {
                    creeper.ignite();
                    creeper.stopRiding();
                    this.servant.heldCreeper = null;
                    this.servant.setHeldMob(null);
                    this.servant.setHoldingState(false);
                    this.servant.teleport();
                }
            }
        }

        public boolean canUse() {
            if (this.servant.getTarget() == null) {
                return false;
            }
            if (this.servant.heldCreeper == null) {
                return false;
            }
            return this.servant.isHolding();
        }

        public boolean canContinueToUse() {
            return false;
        }
    }
}
