
package com.bilibili.player_ix.noixmod_api.magic.misc;

import com.bilibili.player_ix.noixmod_api.magic.Spell;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.EntityBasedExplosionDamageCalculator;
import net.minecraft.world.level.Level;

public class CreeperSpell extends Spell {
    public Type getSpellType() {
        return Type.MISC;
    }

    public float spellPower() {
        return 40F;
    }

    public void castSpell(ServerLevel pLevel, LivingEntity pCaster) {
        pLevel.explode(pCaster, pLevel.damageSources().explosion(pCaster, pCaster),
                new EntityBasedExplosionDamageCalculator(pCaster),
                pCaster.getX(), pCaster.getY(), pCaster.getZ(), 1.5F, true, Level.ExplosionInteraction.MOB);
    }
}
