
package com.bilibili.player_ix.noixmod_api.magic.nihilistic;

import com.bilibili.player_ix.noixmod_api.entities.servant.nihilistic.NihilisticServant;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.util.OwnerSummon;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

public class NihilisticServantSpell
extends NihilisticSpell {
    protected final int count;
    public NihilisticServantSpell(int count) {
        super();
        this.count = count;
    }

    public NihilisticServantSpell() {
        this(3);
    }

    public float spellPower() {
        return 90;
    }

    public void castSpell(ServerLevel pLevel, LivingEntity pCaster) {
        OwnerSummon ownerSummon = new OwnerSummon(pCaster);
        for (int i =0; i< count;++i) {
            NihilisticServant servant = NoixmodAPIEntities.NIHILISTIC_SERVANT.get().create(pLevel);
            if (servant != null) {
                ownerSummon.integerSummon(servant, 3, pLevel);
            }
        }
    }
}
