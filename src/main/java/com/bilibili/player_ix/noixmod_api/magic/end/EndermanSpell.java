
package com.bilibili.player_ix.noixmod_api.magic.end;

import com.bilibili.player_ix.noixmod_api.entities.servant.end.EnderManServant;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.github.NineAbyss9.ix_api.util.ParticleUtil;
import com.bilibili.player_ix.noixmod_api.util.OwnerSummon;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

public class EndermanSpell extends EndSpell {
    public EndermanSpell() {super();}

    public float spellPower() {
        return 75;
    }

    public void castSpell(ServerLevel pLevel, LivingEntity pCaster) {
        for (int i = 0; i < this.random.nextInt(3) + 1;i++) {
            EnderManServant man = NoixmodAPIEntities.ENDER_MAN_SERVANT.get().create(pLevel);
            OwnerSummon ownerSummon = new OwnerSummon(pCaster);
            if (man != null) {
                ownerSummon.integerSummon(man, 3);
                ParticleUtil.sendParticles(pLevel, ParticleTypes.WITCH, man.position(),
                        15, 1.5, 0, 1.5, 0);
            }
        }
    }
}
