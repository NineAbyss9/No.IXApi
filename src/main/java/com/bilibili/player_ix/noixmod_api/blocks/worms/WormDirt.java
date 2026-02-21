
package com.bilibili.player_ix.noixmod_api.blocks.worms;

import com.bilibili.player_ix.noixmod_api.entities.servant.worm.Worm;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class WormDirt
extends AbstractWormBlock {
    public WormDirt() {
        super(BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.GRASS).sound(SoundType.SCULK).strength(1f, 50f));
    }

    public int getLightBlock(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos) {
        return 15;
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return 20;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return 1;
    }

    @Override
    public boolean isLadder(BlockState state, LevelReader world, BlockPos pos, LivingEntity entity) {
        return true;
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, @NotNull BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        Worm worm = new Worm(NoixmodAPIEntities.WORM.get(), level);
        worm.moveTo(pos.above(), 0, 0);
        if (level instanceof ServerLevel serverLevel) {
            worm.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(pos.above()), MobSpawnType.NATURAL, null, null);
            serverLevel.addFreshEntity(worm);
        }
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    @NotNull
    @Override
    public InteractionResult use(@NotNull BlockState p_60503_, @NotNull Level p_60504_, @NotNull BlockPos p_60505_, @NotNull Player p_60506_, @NotNull InteractionHand p_60507_, @NotNull BlockHitResult p_60508_) {
        Worm worm = new Worm(NoixmodAPIEntities.WORM.get(), p_60504_);
        worm.moveTo(p_60505_, 0, 0);
        if (p_60504_ instanceof ServerLevel serverLevel) {
            worm.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(p_60505_), MobSpawnType.NATURAL, null, null);
            serverLevel.addFreshEntity(worm);
        }
        p_60504_.destroyBlock(p_60505_, false);
        return InteractionResult.SUCCESS;
    }
}
