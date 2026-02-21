
package com.bilibili.player_ix.noixmod_api.item.ritual;

import com.bilibili.player_ix.noixmod_api.entities.projectile.summon.SummonApostle;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

//The main item of summoning Apostles.
public class BannedBook extends Item {
    public BannedBook() {
        super(new Properties().rarity(Rarity.EPIC).fireResistant().stacksTo(1));
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide) {
            SummonApostle apostle= new SummonApostle(NoixmodAPIEntities.SUMMON_APOSTLE.get(), pLevel);
            apostle.setBoss(true);
            apostle.moveTo(pPlayer.position());
            pLevel.addFreshEntity(apostle);
            pPlayer.getCooldowns().addCooldown(this, 40);
        }
        return ItemUtils.startUsingInstantly(pLevel, pPlayer, pUsedHand);
    }
}
