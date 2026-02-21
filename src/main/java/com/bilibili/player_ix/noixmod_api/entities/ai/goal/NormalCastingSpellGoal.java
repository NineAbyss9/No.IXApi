
package com.bilibili.player_ix.noixmod_api.entities.ai.goal;

import com.github.NineAbyss9.ix_api.api.mobs.SpellCasterMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class NormalCastingSpellGoal extends Goal {
    protected final Mob looker;
    protected final SpellCasterMob caster;
    public NormalCastingSpellGoal(Mob mob) {
        this.looker = mob;
        this.caster = (SpellCasterMob)mob;
        this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
    }

    public boolean canUse() {
        return this.caster.isCastingSpell();
    }

    public void start() {
        this.looker.getNavigation().stop();
    }

    public void tick() {
        LivingEntity entity = this.looker.getTarget();
        if (entity != null) {
            this.looker.getLookControl().setLookAt(entity, this.looker.getMaxHeadYRot(), this.looker.getMaxHeadXRot());
        }
    }

    public void stop() {
        this.caster.stopSpell();
    }
}
