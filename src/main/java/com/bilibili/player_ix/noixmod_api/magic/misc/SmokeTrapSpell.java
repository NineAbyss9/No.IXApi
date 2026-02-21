
package com.bilibili.player_ix.noixmod_api.magic.misc;

import com.bilibili.player_ix.noixmod_api.entities.projectile.SmokeTrap;
import com.bilibili.player_ix.noixmod_api.magic.Spell;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.util.OwnerSummon;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

public class SmokeTrapSpell
extends Spell {
    private final int trapCount;
    public SmokeTrapSpell(int count) {
        super();
        this.trapCount = count;
    }

    public SmokeTrapSpell() {
        this(4);
    }

    @Override
    public Type getSpellType() {
        return Type.MISC;
    }

    @Override
    public float spellPower() {
        return 25f;
    }

    @Override
    public void castSpell(ServerLevel pLevel, LivingEntity pCaster) {
        for (int i =0 ;i < trapCount; i++) {
            SmokeTrap trap = new SmokeTrap(NoixmodAPIEntities.SMOKE_TRAP.get(), pLevel);
            OwnerSummon ownerSummon = new OwnerSummon(pCaster);
            ownerSummon.integerSummon(trap, 2);
        }
    }
}
