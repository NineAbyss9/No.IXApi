
package com.bilibili.player_ix.noixmod_api.api.entity;

import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.Nihilist;
import net.minecraft.world.entity.Entity;

import java.util.List;
import java.util.function.Predicate;

public interface IX {
    Predicate<Nihilist> NOT_IX = nihilist -> !(nihilist instanceof IX);
    Predicate<Nihilist> IS_IX = nihilist -> nihilist instanceof IX;
    Entity ixSelf();

    default List<Nihilist> followers() {
        return this.ixSelf().level().getEntitiesOfClass(Nihilist.class, this.ixSelf().getBoundingBox().inflate(99),
                NOT_IX);
    }
}
