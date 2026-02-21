
package com.bilibili.player_ix.noixmod_api.blocks.horror;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;

public class Window
extends IronBarsBlock {
    private final boolean isHorror;
    public Window(boolean pHorror) {
        super(Properties.of().sound(SoundType.GLASS).noOcclusion().strength(0.3F)
                .instrument(NoteBlockInstrument.HAT));
        isHorror = pHorror;
    }

    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        if (isHorror && pRandom.nextDouble() < 0.01) {
            pLevel.playLocalSound(pPos, SoundEvents.EMPTY, SoundSource.BLOCKS,
                    Math.max(pRandom.nextFloat(), 0.8F), Math.max(pRandom.nextFloat(), 0.5F), false);
        }
    }
}
