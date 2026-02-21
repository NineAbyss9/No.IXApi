
package com.bilibili.player_ix.noixmod_api.item.ritual;

import com.github.NineAbyss9.ix_api.ix_api.util.ItemUtil;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.util.WorldUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class NihilisticEssence
extends RitualSupplies {
    public NihilisticEssence() {
        super(new Properties().rarity(Rarity.UNCOMMON).stacksTo(64));
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        if (!pLevel.isClientSide && stack.getCount() > 6) {
            Mob boss;
            if (pLevel.dimension() == Level.NETHER)
                boss = NoixmodAPIEntities.EVIL_SUMMONER.get().create(pLevel);
            else if (pLevel.dimension() == Level.OVERWORLD)
                boss = NoixmodAPIEntities.PRIEST.get().create(pLevel);
            else
                boss = NoixmodAPIEntities.STAR_GUARDIAN.get().create(pLevel);
            if (boss != null) {
                boss.moveTo(pPlayer.position());
                WorldUtil.nullableFinalizeSpawn(boss, pLevel, pPlayer.blockPosition(), MobSpawnType.EVENT);
                if (pLevel.addFreshEntity(boss)) {
                    ItemUtil.shrink(stack, pPlayer, 6);
                }
            }
        }
        return ItemUtils.startUsingInstantly(pLevel, pPlayer, pUsedHand);
    }
}
