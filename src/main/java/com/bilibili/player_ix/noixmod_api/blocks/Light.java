
package com.bilibili.player_ix.noixmod_api.blocks;

import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

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
        if (!p_55667_.isClientSide) {
            boolean $$6 = p_55666_.getValue(LIT);
            if ($$6 != p_55667_.hasNeighborSignal(p_55668_)) {
                if ($$6) {
                    p_55667_.scheduleTick(p_55668_, this, 4);
                } else {
                    p_55667_.setBlock(p_55668_, p_55666_.cycle(LIT), 2);
                }
            }
        }
    }

    public void tick(BlockState p_221937_, ServerLevel p_221938_, BlockPos p_221939_, RandomSource p_221940_) {
        if (NoixmodAPIMainConfig.HorrorMode.get()) {
            p_221938_.setBlock(p_221939_, p_221937_.cycle(LIT), 0);
        } else
        if (p_221937_.getValue(LIT) && !p_221938_.hasNeighborSignal(p_221939_)) {
            p_221938_.setBlock(p_221939_, p_221937_.cycle(LIT), 2);
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        p_49915_.add(LIT);
    }

    static {
        LIT = BlockStateProperties.LIT;
    }
}
