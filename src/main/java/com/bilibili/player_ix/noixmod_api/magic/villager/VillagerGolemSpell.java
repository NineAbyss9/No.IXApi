
package com.bilibili.player_ix.noixmod_api.magic.villager;

import com.bilibili.player_ix.noixmod_api.entities.villager.VillagerGolem;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.github.NineAbyss9.ix_api.ix_api.util.Maths;
import com.github.NineAbyss9.ix_api.ix_api.util.ParticleUtil;
import com.bilibili.player_ix.noixmod_api.util.WorldUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;

public class VillagerGolemSpell extends VillagerSpell {
    final boolean hasLife;
    public VillagerGolemSpell(boolean hasLife) {
        super();
        this.hasLife = hasLife;
    }

    public VillagerGolemSpell() {
        this(true);
    }

    public float spellPower() {
        return 75;
    }

    public void castSpell(ServerLevel pLevel, LivingEntity pCaster) {
        for (int i = 0; i < 3; i++) {
            VillagerGolem golem = NoixmodAPIEntities.VILLAGER_GOLEM.get().create(pLevel);
            if (golem == null) continue;
            golem.setOwner(pCaster);
            if (hasLife) {
                golem.setLimitedLife(20 * (20 + pLevel.random.nextInt(20)));
            }
            golem.moveTo(pCaster.getX() + Maths.randomInteger(2), pCaster.getY(), pCaster.getZ()
                    + Maths.randomInteger(2));
            WorldUtil.nullableFinalizeSpawn(golem, pLevel, pLevel.getCurrentDifficultyAt(pCaster.blockPosition()),
                    MobSpawnType.MOB_SUMMONED);
            pLevel.addFreshEntityWithPassengers(golem);
            ParticleUtil.spawnAnim(golem);
        }
    }
}
