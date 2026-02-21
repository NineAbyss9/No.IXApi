
package com.bilibili.player_ix.noixmod_api.entities.mod;

import com.github.NineAbyss9.ix_api.api.mobs.Ownable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.level.Level;

public abstract class APIGolem extends AbstractGolem implements Ownable {
    protected APIGolem(EntityType<? extends APIGolem> p_27508_, Level p_27509_) {
        super(p_27508_, p_27509_);
    }


}
