
package com.bilibili.player_ix.noixmod_api.item.block;

import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class VoidBlockItem
extends BlockItem {
    public VoidBlockItem() {
        super(NoixmodAPIBlocks.VOID_BLOCK.get(), new Item.Properties().fireResistant()
                .rarity(Rarity.RARE));
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand)
    {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        BlockPos pos = pPlayer.blockPosition().below();
        if (!pLevel.getBlockState(pos).canBeReplaced()) {
            return InteractionResultHolder.fail(stack);
        }
        if (!pLevel.isClientSide) {
            pLevel.setBlock(pos, NoixmodAPIBlocks.VOID_BLOCK.get().defaultBlockState(), 3);
            if (!pPlayer.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }
        return InteractionResultHolder.sidedSuccess(stack, pLevel.isClientSide);
    }

    public InteractionResult useOn(UseOnContext pContext)
    {
        return super.useOn(pContext);
    }
}
