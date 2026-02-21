
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

public class WormBlock
extends AbstractWormBlock {
    public WormBlock() {
        super(BlockBehaviour.Properties.of().ignitedByLava().strength(1f, 50f)
                .instrument(NoteBlockInstrument.BASS).mapColor(MapColor.GOLD).sound(SoundType.SCULK));
    }

    public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return 15;
    }

    public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return 20;
    }

    public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return 1;
    }

    public boolean isLadder(BlockState state, LevelReader world, BlockPos pos, LivingEntity entity) {
        return true;
    }

    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player,
                                       boolean willHarvest, FluidState fluid) {
        for (int i = 0; i < 3; ++i) {
            Worm worm = new Worm(NoixmodAPIEntities.WORM.get(), level);
            worm.moveTo(pos.above(), 0, 0);
            if (level instanceof ServerLevel serverLevel) {
                worm.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(pos.above()), MobSpawnType.NATURAL);
                serverLevel.addFreshEntity(worm);
            }
        }
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    public InteractionResult use(BlockState p_60503_, Level p_60504_, BlockPos p_60505_, Player p_60506_,
                                 InteractionHand p_60507_, BlockHitResult p_60508_) {
        Worm worm = NoixmodAPIEntities.WORM.get().create(p_60504_);
        if (worm != null) {
            worm.moveTo(p_60505_, 0, 0);
            if (p_60504_ instanceof ServerLevel serverLevel) {
                worm.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(p_60505_), MobSpawnType.NATURAL);
                serverLevel.addFreshEntity(worm);
            }
        }
        p_60504_.destroyBlock(p_60505_, false);
        return InteractionResult.SUCCESS;
    }
}
