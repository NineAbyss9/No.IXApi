
package com.bilibili.player_ix.noixmod_api.blocks;

import com.bilibili.player_ix.noixmod_api.entities.servant.nihilistic.NihilisticWither;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;

import javax.annotation.Nullable;

public class NihilisticStarBlock
extends Block {
    @Nullable
    private static BlockPattern witherPatternFull;
    public NihilisticStarBlock() {
        super(Properties.of());
    }

    @Override
    public void setPlacedBy(Level p_49847_, BlockPos p_49848_, BlockState p_49849_, @Nullable LivingEntity p_49850_,
                            ItemStack p_49851_) {
        super.setPlacedBy(p_49847_, p_49848_, p_49849_, p_49850_, p_49851_);
    }

    public static void checkSpawn(Level p_58256_, BlockPos p_58257_, SkullBlockEntity p_58258_) {
        if (!p_58256_.isClientSide) {
            BlockState $$3 = p_58258_.getBlockState();
            boolean $$4 = $$3.is(new NihilisticStarBlock());
            if ($$4 && p_58257_.getY() >= p_58256_.getMinBuildHeight() && p_58256_.getDifficulty()
                    != Difficulty.PEACEFUL) {
                BlockPattern.BlockPatternMatch $$5 = getOrCreateWitherFull().find(p_58256_, p_58257_);
                if ($$5 != null) {
                    NihilisticWither wither = NoixmodAPIEntities.NIHILISTIC_WITHER.get().create(p_58256_);
                    if (wither != null) {
                        CarvedPumpkinBlock.clearPatternBlocks(p_58256_, $$5);
                        BlockPos $$7 = $$5.getBlock(1, 2, 0).getPos();
                        wither.moveTo((double)$$7.getX() + 0.5, $$7.getY() + 0.55, $$7.getZ() + 0.5,
                                $$5.getForwards().getAxis() == Direction.Axis.X ? 0.0F : 90.0F, 0.0F);
                        wither.yBodyRot = $$5.getForwards().getAxis() == Direction.Axis.X ? 0.0F : 90.0F;
                        wither.handleSpawnEvent();
                        for (ServerPlayer $$8 : p_58256_.getEntitiesOfClass(ServerPlayer.class, wither.getBoundingBox()
                                .inflate(50.0))) {
                            CriteriaTriggers.SUMMONED_ENTITY.trigger($$8, wither);
                        }
                        p_58256_.addFreshEntity(wither);
                        CarvedPumpkinBlock.updatePatternBlocks(p_58256_, $$5);
                    }
                }
            }
        }
    }

    private static BlockPattern getOrCreateWitherFull() {
        if (witherPatternFull == null) {
            witherPatternFull = BlockPatternBuilder.start().aisle("^^^", "###", "~#~").where('#',
                    (p_58272_) -> p_58272_.getState().is(BlockTags.WITHER_SUMMON_BASE_BLOCKS))
                    .where('^', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks
                    .WITHER_SKELETON_SKULL).or(BlockStatePredicate.forBlock(Blocks
                    .WITHER_SKELETON_WALL_SKULL)))).where('~', (p_284877_) -> p_284877_.getState().isAir())
                    .build();
        }
        return witherPatternFull;
    }
}
