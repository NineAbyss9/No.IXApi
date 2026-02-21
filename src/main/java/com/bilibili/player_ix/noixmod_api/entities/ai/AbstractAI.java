
package com.bilibili.player_ix.noixmod_api.entities.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public abstract class AbstractAI {
    protected final PathfinderMob mob;
    protected static final Random RANDOM = new Random();
    public AbstractAI(PathfinderMob finder) {
        this.mob = finder;
    }

    public abstract boolean needTeleport();

    public abstract boolean isInDanger(@Nullable LivingEntity target);

     public boolean isInDanger() {
         return this.isInDanger(this.mob.getTarget());
     }

     public abstract boolean canUpdate();

     public abstract void update();
}
