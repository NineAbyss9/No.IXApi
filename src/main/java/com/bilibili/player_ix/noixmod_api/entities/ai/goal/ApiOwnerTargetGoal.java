
package com.bilibili.player_ix.noixmod_api.entities.ai.goal;

import com.github.NineAbyss9.ix_api.api.mobs.Ownable;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;

import java.util.function.Predicate;

public class ApiOwnerTargetGoal extends TargetGoal {
    public ApiOwnerTargetGoal(Mob ownable, boolean pMustSee, boolean pMustReach) {
        super(ownable, pMustSee, pMustReach);
    }

    public ApiOwnerTargetGoal(Mob pMob) {
        this(pMob, false, false);
    }

    public boolean canUse() {
        return this.mob instanceof Ownable ownable && ownable.getOwner() instanceof Mob newMob
                && newMob.getTarget() != null && this.mob.canAttack(newMob.getTarget());
    }

    @SuppressWarnings("all")
    public void start() {
        this.mob.setTarget(((Mob)((Ownable)mob).getOwner()).getTarget());
        super.start();
    }

    public static Predicate<LivingEntity> predicate(Mob pMob) {
        return target -> {
            LivingEntity owner = null;
            if (Ownable.getOwner(pMob) != null){
                owner = Ownable.getOwner(pMob);
            }
            if (owner instanceof Enemy
                    || (owner instanceof Ownable owned && owned.isHostile())
                    || (pMob instanceof Enemy && !(pMob instanceof Ownable))
                    || (pMob instanceof Ownable ownedAttacker && ownedAttacker.isHostile())){
                return target instanceof Player player && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(player);
            } else if (target instanceof NeutralMob neutralMob) {
                return (owner != null && neutralMob.getTarget() == owner) || (neutralMob.getTarget() == pMob);
            } else
                return ((target instanceof Enemy && !(target instanceof Ownable)) || (target instanceof Ownable ownedTarget &&
                        ownedTarget.isHostile()));
        };
    }
}
