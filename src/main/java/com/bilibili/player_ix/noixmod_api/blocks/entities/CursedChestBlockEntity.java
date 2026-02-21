
package com.bilibili.player_ix.noixmod_api.blocks.entities;

import com.bilibili.player_ix.noixmod_api.register.ApiBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CursedChestBlockEntity
extends ChestBlockEntity {
    public CursedChestBlockEntity(BlockPos p_155328_, BlockState p_155329_) {
        super(ApiBlockEntities.CURSED_CHEST.get(), p_155328_, p_155329_);
    }

    protected void signalOpenCount(Level p_155865_, BlockPos p_155866_, BlockState p_155867_, int p_155868_, int p_155869_) {
        super.signalOpenCount(p_155865_, p_155866_, p_155867_, p_155868_, p_155869_);
        if (p_155868_ != p_155869_) {
            Block $$5 = p_155867_.getBlock();
            p_155865_.updateNeighborsAt(p_155866_, $$5);
            p_155865_.updateNeighborsAt(p_155866_.below(), $$5);
        }
    }

    public Component getName() {
        return Component.translatable("block.noixmodapi.cursed_chest");
    }
}
