
package com.bilibili.player_ix.noixmod_api.magic.nether;

import com.bilibili.player_ix.noixmod_api.entities.projectile.LavaTrap;
import com.github.NineAbyss9.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

public class LavaTrapSpell extends NetherSpell {
    private final int trapCount;
    public LavaTrapSpell(int count) {
        super();
        this.trapCount = count;
    }

    public LavaTrapSpell() {
        this(5);
    }

    public float spellPower() {
        return 25F;
    }

    public void castSpell(ServerLevel pLevel, LivingEntity pCaster) {
        for (int i = 0; i < this.trapCount; i++) {
            LavaTrap trap = new LavaTrap(pLevel);
            trap.setOwner(pCaster);
            trap.moveTo(pCaster.blockPosition().offset(Maths.randomInt(5), 0, Maths.randomInt(5)),
                    0, 0);
            MobUtils.moveToGround(trap);
            pLevel.addFreshEntity(trap);
        }
    }
}
