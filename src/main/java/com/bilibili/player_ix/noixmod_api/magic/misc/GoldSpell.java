
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

    public Type getSpellType() {
        return Type.MISC;
    }

    public float spellPower() {
        return 12;
    }

    public void castSpell(ServerLevel pLevel, LivingEntity pCaster) {
        BlockPos pos = pCaster.blockPosition().below();
        pLevel.removeBlock(pos, true);
        pLevel.setBlock(pos, Blocks.GOLD_BLOCK.defaultBlockState(), 3);
    }
}
