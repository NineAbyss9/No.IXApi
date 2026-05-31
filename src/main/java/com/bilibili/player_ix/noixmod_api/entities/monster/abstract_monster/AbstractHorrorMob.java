
package com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster;

import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiMeleeAttackGoal;
import com.github.NineAbyss9.ix_api.api.mobs.ApiPathfinderMob;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIDamageSource;
import com.github.NineAbyss9.ix_api.util.ParticleUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractHorrorMob extends ApiPathfinderMob implements Enemy, IHorror {
    public AbstractHorrorMob(EntityType<? extends AbstractHorrorMob> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    protected void addAttackGoal() {
        this.goalSelector.addGoal(0, new ApiMeleeAttackGoal(this, 1.0D));
    }

    protected void addGoals(int i) {
        this.goalSelector.addGoal(i, new FloatGoal(this));
        this.goalSelector.addGoal(i, new RandomStrollGoal(this, 0.8D));
    }

    protected void targetGoals() {
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this,
                Player.class, false));
        this.targetSelector.addGoal(2, new HorrorHurtByTargetGoal(this, AbstractHorrorMob.class));
    }

    public int getLevel() {
        return 0;
    }

    public boolean canAttack(LivingEntity p_21171_) {
        if (p_21171_ instanceof AbstractHorrorMob) {
            return false;
        }
        return super.canAttack(p_21171_);
    }

    public boolean isInvulnerableTo(DamageSource pSource) {
        if (pSource.is(DamageTypeTags.IS_FALL)) {
            return true;
        }
        return super.isInvulnerableTo(pSource);
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (NoixmodAPIDamageSource.sourceEntity(pSource) instanceof AbstractHorrorMob) {
            return false;
        }
        return super.hurt(pSource, pAmount);
    }

    protected void actuallyHurt(DamageSource pSource, float pAmount) {
        if (NoixmodAPIDamageSource.sourceEntity(pSource) instanceof AbstractHorrorMob) {
            return;
        }
        super.actuallyHurt(pSource, pAmount);
    }

    public boolean startRiding(Entity pEntity, boolean pForce) {
        return false;
    }

    protected boolean canRide(Entity pVehicle) {
        return false;
    }

    public void spawnAnim()
    {
        ParticleUtil.sendParticles(this.serverLevel(), ParticleTypes.LARGE_SMOKE, this.position(), 5,
                0.15, 0.5, 0.15, 0.05);
    }

    public void die() {
        if (this.isServerSide()) {
            this.spawnAnim();
        }
    }

    protected static class HorrorOpenDoorGoal extends Goal
    {
        protected Mob mob;
        protected BlockPos doorPos = BlockPos.ZERO;
        protected boolean hasDoor;
        private boolean passed;
        private float doorOpenDirX;
        private float doorOpenDirZ;
        private int forgetTime;
        public HorrorOpenDoorGoal(Mob pMob) {
            this.mob = pMob;
        }

        protected boolean isOpen() {
            if (!this.hasDoor) {
                return false;
            } else {
                BlockState blockstate = this.mob.level().getBlockState(this.doorPos);
                if (!(blockstate.getBlock() instanceof DoorBlock) && !(blockstate.getBlock() instanceof TrapDoorBlock)) {
                    this.hasDoor = false;
                    return false;
                } else {
                    return blockstate.getValue(BlockStateProperties.OPEN);
                }
            }
        }

        @SuppressWarnings("all")
        protected void setOpen(boolean pOpen) {
            if (this.hasDoor) {
                BlockState blockstate = this.mob.level().getBlockState(this.doorPos);
                if (blockstate.getBlock() instanceof DoorBlock) {
                    ((DoorBlock)blockstate.getBlock()).setOpen(this.mob, this.mob.level(), blockstate, this.doorPos, pOpen);
                } else if (blockstate.is(BlockTags.WOODEN_TRAPDOORS)) {
                    ((TrapDoorBlock)blockstate.getBlock()).use(blockstate, this.mob.level(), this.doorPos, null, InteractionHand.MAIN_HAND,
                            null);
                }
            }
        }

        public boolean canUse() {
            if (!this.mob.horizontalCollision) {
                return false;
            } else {
                GroundPathNavigation groundpathnavigation = (GroundPathNavigation)this.mob.getNavigation();
                Path path = groundpathnavigation.getPath();
                if (path == null || path.isDone() || !groundpathnavigation.canOpenDoors()) {
                    return false;
                } else {
                    for (int i = 0; i < Math.min(path.getNextNodeIndex() + 2, path.getNodeCount()); ++i) {
                        Node node = path.getNode(i);
                        this.doorPos = new BlockPos(node.x, node.y + 1, node.z);
                        if (!(this.mob.distanceToSqr((double)this.doorPos.getX(), this.mob.getY(), (double)this.doorPos.getZ()) > 2.25D)) {
                            this.hasDoor = DoorBlock.isWoodenDoor(this.mob.level(), this.doorPos);
                            if (this.hasDoor) {
                                return true;
                            }
                        }
                    }
                    this.doorPos = this.mob.blockPosition().above();
                    this.hasDoor = DoorBlock.isWoodenDoor(this.mob.level(), this.doorPos);
                    return this.hasDoor;
                }
            }
        }

        public boolean canContinueToUse() {
            return false;
        }

        public void start() {
            /*this.passed = false;
            this.doorOpenDirX = (float)((double)this.doorPos.getX() + 0.5D - this.mob.getX());
            this.doorOpenDirZ = (float)((double)this.doorPos.getZ() + 0.5D - this.mob.getZ());*/
            this.forgetTime = 20;
            this.setOpen(true);
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            --this.forgetTime;
            float f = (float)((double)this.doorPos.getX() + 0.5D - this.mob.getX());
            float f1 = (float)((double)this.doorPos.getZ() + 0.5D - this.mob.getZ());
            float f2 = this.doorOpenDirX * f + this.doorOpenDirZ * f1;
            if (f2 < 0.0F) {
                this.passed = true;
            }
        }

        public void stop()
        {
            this.setOpen(false);
        }
    }

    protected static class HorrorHurtByTargetGoal extends HurtByTargetGoal {
        public HorrorHurtByTargetGoal(PathfinderMob p_26039_, Class<?>... toIgnore) {
            super(p_26039_, toIgnore);
        }

        protected boolean canAttack(@Nullable LivingEntity p_26151_, TargetingConditions p_26152_) {
            if (p_26151_ instanceof AbstractHorrorMob) {
                return false;
            }
            return super.canAttack(p_26151_, p_26152_);
        }
    }
}
