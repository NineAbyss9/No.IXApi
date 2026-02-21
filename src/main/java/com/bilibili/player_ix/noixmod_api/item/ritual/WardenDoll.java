
package com.bilibili.player_ix.noixmod_api.item.ritual;

import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.util.OwnerSummon;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class WardenDoll
extends RitualSupplies {
    public WardenDoll() {
        super(new Properties().rarity(Rarity.RARE));
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (pLevel.isClientSide) {
            pLevel.playLocalSound(pPlayer.blockPosition(), SoundEvents.WARDEN_AGITATED, SoundSource.PLAYERS,
                    2.0F, 1.0F, false);
        } else {
            boolean flag = false;
            var servant = NoixmodAPIEntities.WARDEN_SERVANT.get().create(pLevel);
            if (servant != null) {
                OwnerSummon summon = new OwnerSummon(pPlayer);
                summon.integerSummon(servant, 2);
                flag = true;
            }
            if (flag && !pPlayer.getAbilities().instabuild)
                pPlayer.getItemInHand(pUsedHand).shrink(1);
        }
        return ItemUtils.startUsingInstantly(pLevel, pPlayer, pUsedHand);
    }
}
