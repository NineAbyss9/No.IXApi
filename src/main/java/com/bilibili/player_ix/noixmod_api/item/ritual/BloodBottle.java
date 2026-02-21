
package com.bilibili.player_ix.noixmod_api.item.ritual;

import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIParticleTypes;
import com.github.NineAbyss9.ix_api.util.ItemUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

public class BloodBottle
extends RitualSupplies {
    public BloodBottle() {
        super(new Properties().rarity(Rarity.UNCOMMON).stacksTo(16));
    }

    @Override
    public UseAnim getUseAnimation(ItemStack p_41452_) {
        return UseAnim.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack p_41454_) {
        return 32;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
        return ItemUtils.startUsingInstantly(p_41432_, p_41433_, p_41434_);
    }

    @Override
    public void onUseTick(Level p_41428_, LivingEntity p_41429_, ItemStack p_41430_, int p_41431_) {
        p_41429_.addEffect(new MobEffectInstance(MobEffects.HUNGER, 10, 0));
        p_41429_.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 10, 0));
        if (p_41428_.isClientSide()) {
            p_41428_.addParticle(NoixmodAPIParticleTypes.BLOOD.get(), p_41429_.getRandomX(0.5),
                    p_41429_.getY() + 1, p_41429_.getRandomZ(0.5), 0, 0, 0);
        }
        super.onUseTick(p_41428_, p_41429_, p_41430_, p_41431_);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack p_41409_, Level p_41410_, LivingEntity p_41411_) {
        if (p_41411_ instanceof Player player) {
            ItemUtil.shrink(p_41409_, player);
            player.addItem(new ItemStack(Items.GLASS_BOTTLE));
        }
        return super.finishUsingItem(p_41409_, p_41410_, p_41411_);
    }
}
