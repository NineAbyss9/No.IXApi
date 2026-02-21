
package com.bilibili.player_ix.noixmod_api.magic.nihilistic;

import com.bilibili.player_ix.noixmod_api.entities.projectile.NihilisticCrack;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

public class CrackSpell
extends NihilisticSpell {
    public float spellPower() {
        return 50.0F;
    }

    public void castSpell(ServerLevel pLevel, LivingEntity pCaster) {
        NihilisticCrack crack = NoixmodAPIEntities.NIHILISTIC_CRACK.get().create(pLevel);
        if (crack != null) {
            crack.setOwner(pCaster);
            crack.moveTo(pCaster.position());
            pLevel.addFreshEntity(crack);
        }
    }
}
