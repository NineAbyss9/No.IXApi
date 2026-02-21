
package com.bilibili.player_ix.noixmod_api.item.magic;

import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Rarity;

public class NihilisticStaff
extends Staff {
    public NihilisticStaff() {
        super(new Properties().rarity(Rarity.EPIC).fireResistant().stacksTo(1));
    }

    public void castSpell(LivingEntity living) {
        if (living.isShiftKeyDown()) {
            MobUtils.rangeHurt(4, 4, 4, living, living.damageSources().starve(), 9F);
        } else {
            living.heal(5F);
        }
        if (living instanceof Player player) {
            player.getCooldowns().addCooldown(this, 90);
        }
    }
}
