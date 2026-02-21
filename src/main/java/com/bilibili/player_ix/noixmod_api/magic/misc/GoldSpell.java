
package com.bilibili.player_ix.noixmod_api.magic.misc;

import com.bilibili.player_ix.noixmod_api.magic.Spell;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;

public class GoldSpell
extends Spell {
    public GoldSpell() {
        super();
    }

    @Override
    public Type getSpellType() {
        return Type.MISC;
    }

    @Override
    public float spellPower() {
        return 12;
    }

    @Override
    public void castSpell(ServerLevel pLevel, LivingEntity pCaster) {
        BlockPos pos = pCaster.blockPosition().below();
        pLevel.removeBlock(pos, true);
        pLevel.setBlock(pos, Blocks.GOLD_BLOCK.defaultBlockState(), 1);
    }
}
