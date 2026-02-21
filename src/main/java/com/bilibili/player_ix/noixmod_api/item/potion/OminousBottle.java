
package com.bilibili.player_ix.noixmod_api.item.potion;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public class OminousBottle extends Item {
    static final MobEffectInstance BAD_OMEN = new MobEffectInstance(MobEffects.BAD_OMEN,
            12000, 0);
    public OminousBottle() {
        super(new Properties().rarity(Rarity.UNCOMMON).stacksTo(1));
    }

    public ItemStack finishUsingItem(ItemStack p_42984_, Level p_42985_, LivingEntity p_42986_) {
        Player $$3 = p_42986_ instanceof Player ? (Player)p_42986_ : null;
        if ($$3 instanceof ServerPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer)$$3, p_42984_);
        }
        if (!p_42985_.isClientSide) {
            p_42986_.addEffect(BAD_OMEN);
        }
        if ($$3 != null) {
            $$3.awardStat(Stats.ITEM_USED.get(this));
            if (!$$3.getAbilities().instabuild) {
                p_42984_.shrink(1);
            }
        }
        if ($$3 == null || !$$3.getAbilities().instabuild) {
            if (p_42984_.isEmpty()) {
                return new ItemStack(Items.GLASS_BOTTLE);
            }
            if ($$3 != null) {
                $$3.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
            }
        }
        p_42986_.gameEvent(GameEvent.DRINK);
        return p_42984_;
    }

    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
        return ItemUtils.startUsingInstantly(p_41432_, p_41433_, p_41434_);
    }

    public int getUseDuration(ItemStack p_41454_) {
        return 32;
    }

    public UseAnim getUseAnimation(ItemStack p_41452_) {
        return UseAnim.DRINK;
    }
}
