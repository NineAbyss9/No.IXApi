
package com.bilibili.player_ix.noixmod_api.entities.servant;

import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.AbstractGhost;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraftforge.common.Tags;

public class GhostMiner
extends AbstractGhost {
    public GhostMiner(EntityType<? extends GhostMiner> entityType, Level level) {
        super(entityType, level);
        this.setItemInHand(InteractionHand.MAIN_HAND, Items.IRON_AXE.getDefaultInstance());
    }



    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MineBlockGoal(this));
        this.addBehaviorGoal(3, 0.8D, 10.0F);
    }

    public static class MineBlockGoal extends Goal {
        protected final Mob mob;
        protected boolean hasBlock;
        protected BlockPos blockPos = BlockPos.ZERO;
        private static final int DEFAULT_DOOR_BREAK_TIME = 240;
        protected int breakTime;
        protected int lastBreakProgress = -1;
        protected int blockBreakTime = -1;
        public MineBlockGoal(Mob pMob) {
            super();
            this.mob = pMob;
        }

        public MineBlockGoal(Mob pMob, int pDoorBreakTime) {
            this(pMob);
            this.blockBreakTime = pDoorBreakTime;
        }

        protected int getBlockBreakTime() {
            return this.blockBreakTime;
        }

        public boolean canUse() {
            if (!this.mob.horizontalCollision) {
                return false;
            } else {
                GroundPathNavigation groundpathnavigation = (GroundPathNavigation)this.mob.getNavigation();
                Path path = groundpathnavigation.getPath();
                if (path != null && !path.isDone()) {
                    for(int i = 0; i < Math.min(path.getNextNodeIndex() + 2, path.getNodeCount()); ++i) {
                        Node node = path.getNode(i);
                        this.blockPos = new BlockPos(node.x, node.y + 1, node.z);
                        if (!(this.mob.distanceToSqr((double)this.blockPos.getX(), this.mob.getY(), (double)this.blockPos.getZ()) > 6D)) {
                            this.hasBlock = this.mob.level().getBlockState(this.blockPos).is(Tags.Blocks.ORES);
                            if (this.hasBlock) {
                                return true;
                            }
                        }
                    }
                    this.blockPos = this.mob.blockPosition().above();
                    this.hasBlock = DoorBlock.isWoodenDoor(this.mob.level(), this.blockPos);
                    return this.hasBlock;
                } else {
                    return false;
                }
            }
            //return net.minecraftforge.common.ForgeHooks.canEntityDestroy(this.mob.level(), this.blockPos, this.mob);
        }

        public void start() {
            this.breakTime = 0;
        }

        public boolean canContinueToUse() {
            return this.breakTime <= this.getBlockBreakTime() && this.blockPos
                    .closerToCenterThan(this.mob.position(), 2.0D);
        }

        public void stop() {
            this.mob.level().destroyBlockProgress(this.mob.getId(), this.blockPos, -1);
        }

        public void tick() {
            super.tick();
            if (this.mob.getRandom().nextInt(20) == 0) {
                this.mob.level().levelEvent(1019, this.blockPos, 0);
                if (!this.mob.swinging) {
                    this.mob.swing(this.mob.getUsedItemHand());
                }
            }
            ++this.breakTime;
            int i = (int)((float)this.breakTime / (float)this.getBlockBreakTime() * 10.0F);
            if (i != this.lastBreakProgress) {
                this.mob.level().destroyBlockProgress(this.mob.getId(), this.blockPos, i);
                this.lastBreakProgress = i;
            }
            if (this.breakTime == this.getBlockBreakTime()) {
                this.mob.level().destroyBlock(this.blockPos, false);
            }
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }
    }
}
