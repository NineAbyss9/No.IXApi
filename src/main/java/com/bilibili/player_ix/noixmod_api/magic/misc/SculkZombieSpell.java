
package com.bilibili.player_ix.noixmod_api.magic.misc;

import com.bilibili.player_ix.noixmod_api.entities.servant.sculk.SculkZombie;
import com.bilibili.player_ix.noixmod_api.magic.Spell;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.util.OwnerSummon;
import com.github.NineAbyss9.ix_api.api.mobs.Ownable;
import com.github.NineAbyss9.ix_api.util.Maths;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.phys.Vec3;

import static com.github.NineAbyss9.ix_api.api.mobs.OwnableMob.ownerOrThis;

public class SculkZombieSpell
extends Spell
{
    public Type getSpellType()
    {
        return Type.OVERWORLD;
    }

    public float spellPower()
    {
        return 12.5F;
    }

    public void castSpell(ServerLevel pLevel, LivingEntity pCaster)
    {
        var entity = pCaster instanceof Ownable ownable ? ownerOrThis(ownable) : pCaster;
        if (OwnerSummon.canSummon(pLevel, entity, 8, e -> e instanceof SculkZombie)) {
            for (int i = 0;i < 4;i++) {
                SculkZombie zombie = NoixmodAPIEntities.SCULK_ZOMBIE.get().create(pLevel);
                if (zombie == null) {
                    return;
                }
                zombie.setOwner(entity);
                zombie.moveTo(new Vec3(pCaster.getX() + Maths.randomInt(4), pCaster.getY(), pCaster.getZ()
                        + Maths.randomInt(4)));
                zombie.finalizeSpawn(pLevel, pLevel.getCurrentDifficultyAt(pCaster.blockPosition()),
                        MobSpawnType.MOB_SUMMONED);
                pLevel.addFreshEntity(zombie);
            }
        }
    }
}
