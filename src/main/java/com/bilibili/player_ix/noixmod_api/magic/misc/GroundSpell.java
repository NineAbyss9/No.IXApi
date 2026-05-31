
package com.bilibili.player_ix.noixmod_api.magic.misc;

import com.bilibili.player_ix.noixmod_api.magic.Spell;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import com.github.NineAbyss9.ix_api.util.ParticleUtil;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;

public class GroundSpell extends Spell {
    public Type getSpellType() {
        return Type.MISC;
    }

    public float spellPower() {
        return 9;
    }

    public void castSpell(ServerLevel pLevel, LivingEntity pCaster)
    {
        pLevel.playSound(pCaster, pCaster.blockPosition(), SoundEvents.GENERIC_EXPLODE, pCaster.getSoundSource(),
                2f, 1f);
        MobUtils.rangeHurt(3, 3, 3, pCaster, pCaster.damageSources().indirectMagic(pCaster, pCaster), 6f);
        ParticleUtil.sendParticles(pLevel, new BlockParticleOption(ParticleTypes.BLOCK, pCaster.getFeetBlockState()),
                pCaster.position(), 10, 0.5D, 0.1D, 0.5D, 0.1D);
    }
}
