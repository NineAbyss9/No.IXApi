
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

    public void castSpell(ServerLevel pLevel, LivingEntity pCaster, int pData) {
        if (pData == 0) {
            LivingEntity target = (LivingEntity)MobUtils.getSingleTarget(pLevel, pCaster, 15.0D, 15.0D,
                    entity -> entity instanceof LivingEntity && MobUtils.canHurt((LivingEntity)entity, pCaster));
            if (target == null) {
                return;
            }
            LightningBolt bolt = new LightningBolt(EntityType.LIGHTNING_BOLT, pLevel);
            bolt.moveTo(target.position());
            pLevel.addFreshEntity(bolt);
        } else {
            LightningBolt bolt = new LightningBolt(EntityType.LIGHTNING_BOLT, pLevel);
            bolt.setVisualOnly(true);
            bolt.moveTo(pCaster.position());
            pLevel.addFreshEntity(bolt);
            MobUtils.rangeHurt(3, 3, 3, pCaster, pLevel.damageSources().lightningBolt(), 6f);
        }
    }

    public void castSpell(ServerLevel pLevel, LivingEntity pCaster) {
        this.castSpell(pLevel, pCaster, 1);
    }
}
