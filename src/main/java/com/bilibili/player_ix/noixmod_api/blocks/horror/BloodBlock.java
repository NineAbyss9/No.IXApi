
package com.bilibili.player_ix.noixmod_api.blocks.horror;

import com.bilibili.player_ix.noixmod_api.api.block.ApiBlockProperties;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIItems;
import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
//import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
//import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
//import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

@SuppressWarnings("deprecation")
public class BloodBlock
extends Block {
    private static final VoxelShape SHAPE;
    public static final IntegerProperty TEXTURE = ApiBlockProperties.TEXTURE;
    public BloodBlock() {
        super(Properties.of().instabreak().sound(SoundType.SLIME_BLOCK)//.mapColor(MapColor.COLOR_RED)
                .replaceable().noOcclusion().instrument(NoteBlockInstrument.BASEDRUM));
        this.stateDefinition.any().setValue(TEXTURE, 0);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(TEXTURE);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(TEXTURE, new Random().nextInt(5));
    }

    public boolean canBeReplaced(BlockState pState, BlockPlaceContext pUseContext) {
        return !pUseContext.getItemInHand().is(NoixmodAPIItems.BLOOD.get())
                && super.canBeReplaced(pState, pUseContext);
    }

    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    static {
        SHAPE = box(0, 0, 0, 16, 0.1, 16);
    }
}
