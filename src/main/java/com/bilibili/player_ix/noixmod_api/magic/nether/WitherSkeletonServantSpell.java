
package com.bilibili.player_ix.noixmod_api.magic.nether;

import com.bilibili.player_ix.noixmod_api.entities.servant.nether.WitherSkeletonServant;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.github.NineAbyss9.ix_api.ix_api.util.ParticleUtil;
import com.bilibili.player_ix.noixmod_api.util.OwnerSummon;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

public class WitherSkeletonServantSpell extends NetherSpell {
    @Override
    public float spellPower() {
        return 30;
    }

    @Override
    public void castSpell(ServerLevel pLevel, LivingEntity pCaster) {
        for (int i = 0;i < pLevel.random.nextInt(3) + 2;++i) {
            OwnerSummon ownerSummon = new OwnerSummon(pCaster);
            WitherSkeletonServant servant = new WitherSkeletonServant(NoixmodAPIEntities.WITHER_SKELETON_SERVANT.get(), pLevel);
            ownerSummon.integerSummon(servant, 2);
            ParticleUtil.sendParticles(pLevel, ParticleTypes.LARGE_SMOKE, servant.position(),
                    6, 1.5, 1.5, 1.5, 0);
        }
    }
}
