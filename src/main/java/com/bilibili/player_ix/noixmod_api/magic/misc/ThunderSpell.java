
package com.bilibili.player_ix.noixmod_api.magic.misc;

import com.bilibili.player_ix.noixmod_api.magic.Spell;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;

public class ThunderSpell
extends Spell {
    public Type getSpellType() {
        return Type.MISC;
    }

    public float spellPower() {
        return 30;
    }

    public void castSpell(ServerLevel pLevel, LivingEntity pCaster) {
        MobUtils.rangeHurt(3, 3, 3, pCaster, pLevel.damageSources().lightningBolt(), 6f);
        var bolt = new LightningBolt(EntityType.LIGHTNING_BOLT, pLevel);
        bolt.setVisualOnly(true);
        bolt.moveTo(pCaster.position());
        pLevel.addFreshEntity(bolt);
    }
}
