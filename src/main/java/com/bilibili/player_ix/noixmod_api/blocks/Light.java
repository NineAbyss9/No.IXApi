
package com.bilibili.player_ix.noixmod_api.blocks;

import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public class Light extends Block {
    protected static final BooleanProperty LIT;
    public Light(Properties p) {
        super(p);
        this.registerDefaultState(this.defaultBlockState().setValue(LIT, Boolean.FALSE));
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext p_55659_) {
        return this.defaultBlockState().setValue(LIT, p_55659_.getLevel().hasNeighborSignal(p_55659_.getClickedPos()));
    }

    public void neighborChanged(BlockState p_55666_, Level p_55667_, BlockPos p_55668_, Block p_55669_,
                                BlockPos p_55670_, boolean p_55671_) {
        if (!p_55667_.isClientSide && !NoixmodAPIMainConfig.HorrorMode.get()) {
            boolean $$6 = p_55666_.getValue(LIT);
            if ($$6 == p_55667_.hasNeighborSignal(p_55668_)) {
                return;
            }
            if ($$6) {
                p_55667_.scheduleTick(p_55668_, this, 4);
            } else {
                p_55667_.setBlock(p_55668_, p_55666_.cycle(LIT), 2);
            }
        }
    }

    public void tick(BlockState p_221937_, ServerLevel p_221938_, BlockPos p_221939_, RandomSource p_221940_) {
        if (NoixmodAPIMainConfig.HorrorMode.get()) {
            if (p_221938_.getGameTime() % 600L == 0L) {
                p_221938_.setBlock(p_221939_, p_221937_.setValue(LIT, true), 0);
            } else
                p_221938_.setBlock(p_221939_, p_221937_.setValue(LIT, false), 0);
        } else
        if (p_221937_.getValue(LIT) && !p_221938_.hasNeighborSignal(p_221939_)) {
            p_221938_.setBlock(p_221939_, p_221937_.cycle(LIT), 2);
        }
    }

    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer,
                                 InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide) {
            pLevel.setBlock(pPos, pState.cycle(LIT), 2);
            pLevel.playSound(null, pPos, SoundEvents.LEVER_CLICK,
                    net.minecraft.sounds.SoundSource.BLOCKS, 0.3F, 1.0F);
        }
        return InteractionResult.sidedSuccess(pLevel.isClientSide);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        p_49915_.add(LIT);
    }

    static {
        LIT = BlockStateProperties.LIT;
    }
}
