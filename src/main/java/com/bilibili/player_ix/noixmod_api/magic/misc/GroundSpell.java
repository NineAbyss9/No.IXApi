
package com.bilibili.player_ix.noixmod_api.magic.misc;

import com.bilibili.player_ix.noixmod_api.magic.Spell;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;

public class GroundSpell extends Spell {
    @Override
    public Type getSpellType() {
        return Type.MISC;
    }

    @Override
    public float spellPower() {
        return 9;
    }

    @Override
    public void castSpell(ServerLevel pLevel, LivingEntity pCaster) {
        pLevel.playSound(pCaster, pCaster.blockPosition(), SoundEvents.GENERIC_EXPLODE, pCaster.getSoundSource(),
                2f, 1f);
        MobUtils.rangeHurt(3, 3, 3, pCaster, pCaster.damageSources().indirectMagic(pCaster, pCaster), 6f);
        for (int j = 0; j < 3; j++) {
            BlockPos pos = pCaster.blockPosition().offset(0, -1, 0);
            pLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, pLevel.getBlockState(pos)), pos.getX(),
                    pos.getY(), pos.getZ(), 5, 0.5, 0, 0.5, 0);
        }
    }
}
