
package com.bilibili.player_ix.noixmod_api.item.util;

import com.bilibili.player_ix.noixmod_api.api.item.ApiTier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class InfernalIronPickaxe
extends ApiPickaxe {
    public InfernalIronPickaxe() {
        super(ApiTier.NETHERITE, 2, -3.0f, new Properties()
                .fireResistant().stacksTo(1));
    }

    public boolean mineBlock(ItemStack p_40998_, Level p_40999_, BlockState p_41000_,
                             BlockPos p_41001_, LivingEntity p_41002_) {
        BlockEntity entity = p_40999_.getBlockEntity(p_41001_);
        Block.dropResources(p_41000_, p_40999_, p_41001_, entity, p_41002_, ItemStack.EMPTY);
        return super.mineBlock(p_40998_, p_40999_, p_41000_, p_41001_, p_41002_);
    }
}
