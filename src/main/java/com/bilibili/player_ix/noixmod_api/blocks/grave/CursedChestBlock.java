
package com.bilibili.player_ix.noixmod_api.blocks.grave;

import com.bilibili.player_ix.noixmod_api.blocks.entities.CursedChestBlockEntity;
import com.bilibili.player_ix.noixmod_api.register.ApiBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

@SuppressWarnings("deprecation")
public class CursedChestBlock extends ChestBlock {
    public CursedChestBlock() {
        super(Properties.of().mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASS)
                .strength(3.5F).sound(SoundType.WOOD).noOcclusion(), ApiBlockEntities.CURSED_CHEST::get);
    }

    public BlockEntity newBlockEntity(BlockPos p_153064_, BlockState p_153065_) {
        return new CursedChestBlockEntity(p_153064_, p_153065_);
    }

    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    public boolean isSignalSource(BlockState p_60571_) {
        return true;
    }

    public int getDirectSignal(BlockState p_57582_, BlockGetter p_57583_, BlockPos p_57584_, Direction p_57585_) {
        return p_57585_ == Direction.UP ? p_57582_.getSignal(p_57583_, p_57584_, p_57585_) : 0;
    }

    public MutableComponent getName() {
        return Component.translatable("block.noixmodapi.cursed_chest");
    }
}
