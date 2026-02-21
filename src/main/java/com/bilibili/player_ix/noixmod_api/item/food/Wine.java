
package com.bilibili.player_ix.noixmod_api.item.food;

import com.github.NineAbyss9.ix_api.ix_api.util.Maths;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Wine extends Item {
    public Wine() {
        super(new Properties().stacksTo(16));
    }

    public UseAnim getUseAnimation(ItemStack p_41452_) {
        return UseAnim.DRINK;
    }

    public int getUseDuration(ItemStack p_41454_) {
        return 20;
    }

    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
        return ItemUtils.startUsingInstantly(p_41432_, p_41433_, p_41434_);
    }

    public ItemStack finishUsingItem(ItemStack p_41409_, Level p_41410_, LivingEntity p_41411_) {
        p_41411_.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, Maths.toTick(30), 1));
        p_41411_.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, Maths.toTick(30), 0));
        if (p_41411_ instanceof Player player && !player.getAbilities().instabuild) {
            p_41409_.shrink(1);
            player.addItem(new ItemStack(Items.GLASS_BOTTLE));
        }
        return super.finishUsingItem(p_41409_, p_41410_, p_41411_);
    }

    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_,
                                TooltipFlag p_41424_) {
        p_41423_.add(Component.translatable("info.noixmodapi.minors_consumes_alcohol"));
    }
}
