
package com.bilibili.player_ix.noixmod_api.item.grave;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class GravePickaxe
extends PickaxeItem {
    public GravePickaxe(Tier pTier, Properties pProperties) {
        super(pTier, 1, -3.0F, pProperties);
    }

    public boolean mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving) {
        return super.mineBlock(pStack, pLevel, pState, pPos, pEntityLiving);
    }
}
