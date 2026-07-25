
package com.bilibili.player_ix.noixmod_api.item.util;

import com.bilibili.player_ix.noixmod_api.api.item.ApiTier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;

public class InfernalIronPickaxe
extends ApiPickaxe {
    public InfernalIronPickaxe() {
        super(ApiTier.NETHERITE, 2, -3.0f, new Properties()
                .fireResistant().stacksTo(1));
    }

    public boolean mineBlock(ItemStack pStack, Level pLevel, BlockState pState,
                             BlockPos pPos, LivingEntity pEntityLiving) {
        BlockEntity entity = pLevel.getBlockEntity(pPos);
        if (pState.is(Tags.Blocks.ORES)) {
            Block.dropResources(pState, pLevel, pPos, entity, pEntityLiving, pStack);
        }
        return super.mineBlock(pStack, pLevel, pState, pPos, pEntityLiving);
    }
}
