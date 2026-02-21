
package com.bilibili.player_ix.noixmod_api.magic.nether;

import com.github.NineAbyss9.ix_api.ix_api.api.annotation.MobOnly;
import com.bilibili.player_ix.noixmod_api.entities.servant.nether.NetherSoul;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.github.NineAbyss9.ix_api.ix_api.util.ParticleUtil;
import com.bilibili.player_ix.noixmod_api.util.OwnerSummon;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

@MobOnly
public class NetherSoulSpell extends NetherSpell {
    public NetherSoulSpell() {
        super();
    }

    public float spellPower() {
        return 50F;
    }

    public void castSpell(ServerLevel pLevel, LivingEntity pCaster) {
        for (int i =0; i<5;++i) {
            NetherSoul soul = NoixmodAPIEntities.NETHER_SOUL.get().create(pLevel);
            if (soul != null) {
                OwnerSummon ownerSummon = new OwnerSummon(pCaster);
                ownerSummon.integerSummon(soul, 3);
                ParticleUtil.sendParticles(pLevel, DustParticleOptions.REDSTONE, pCaster.position(),
                        20, 1, 1, 1, 0);
            }
        }
    }
}
