
package com.bilibili.player_ix.noixmod_api.item.magic;

import com.bilibili.player_ix.noixmod_api.magic.Spells;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Rarity;

public class ThunderStaff
extends Staff {
    public ThunderStaff() {
        super(new Properties().rarity(Rarity.UNCOMMON).stacksTo(1).fireResistant());
    }

    public void castSpell(ServerLevel pLevel, LivingEntity pCaster, int pUsedTime) {
        if (pUsedTime < 20) {
            if (pCaster.isCrouching()) {
                Spells.THUNDER.get().castSpell(pLevel, pCaster);
                return;
            }
            pLevel.setWeatherParameters(0, ServerLevel.RAIN_DURATION.getMinValue(),
                    true, true);
        } else {
            Spells.THUNDER.get().castSpell(pLevel, pCaster, 0);
        }
    }
}
