
package com.bilibili.player_ix.noixmod_api.magic.misc;

import com.bilibili.player_ix.noixmod_api.magic.Spell;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

public class ThunderSpell
extends Spell {
    @Override
    public Type getSpellType() {
        return Type.MISC;
    }

    @Override
    public float spellPower() {
        return 30;
    }

    @Override
    public void castSpell(ServerLevel pLevel, LivingEntity pCaster) {
        MobUtils.rangeHurt(3, 3, 3, pCaster, pLevel.damageSources().lightningBolt(), 6f);
    }
}
