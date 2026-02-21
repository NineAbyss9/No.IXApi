
package com.bilibili.player_ix.noixmod_api.item.magic;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ThunderStaff
extends Staff {
    public ThunderStaff() {
        super(new Properties().rarity(Rarity.UNCOMMON).stacksTo(1).fireResistant());
    }

    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
        if (!p_41432_.isClientSide) {
            ServerLevel level = (ServerLevel)p_41432_;
            if (p_41433_.isShiftKeyDown()) {
                level.setWeatherParameters(0, ServerLevel.RAIN_DURATION.getMinValue(),
                        true, true);
            }
        }
        return ItemUtils.startUsingInstantly(p_41432_, p_41433_, p_41434_);
    }

    public void castSpell(@NotNull LivingEntity living) {
    }
}
