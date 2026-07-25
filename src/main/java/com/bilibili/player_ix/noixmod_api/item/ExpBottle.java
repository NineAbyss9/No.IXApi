
package com.bilibili.player_ix.noixmod_api.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ExpBottle
extends Item
{
    public static final String EXP = "exp";
    public ExpBottle(Properties pProperties)
    {
        super(pProperties);
    }

    public static int getExp(CompoundTag pTag)
    {
        if (!pTag.contains(EXP)) return 0;
        return pTag.getInt(EXP);
    }

    public static void setExp(CompoundTag pTag, int pExpLvl)
    {
        pTag.putInt(EXP, pExpLvl);
    }

    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced)
    {
        if (pLevel == null) return;
        pTooltipComponents.add(Component.translatable("info.noixmodapi.exp_bottle",
                getExp(pStack.getOrCreateTag())));
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand)
    {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        CompoundTag tag = stack.getOrCreateTag();
        if (getExp(tag) == 0) {
            if (pPlayer.experienceLevel <= 0) return InteractionResultHolder.fail(stack);
            setExp(tag, pPlayer.experienceLevel);
            pPlayer.giveExperienceLevels(-pPlayer.experienceLevel);
        } else {
            pPlayer.giveExperienceLevels(getExp(tag));
            setExp(tag, 0);
        }
        return InteractionResultHolder.sidedSuccess(stack, pLevel.isClientSide);
    }
}
