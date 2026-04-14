
package com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public abstract class BeeQueen
extends AbstractBee {
    protected long breedCooldown = this.getBreedCooldown();
    public BeeQueen(EntityType<? extends BeeQueen> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
    }

    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.breedCooldown > 0) {
            --this.breedCooldown;
        } else {
            this.breedCooldown = this.getBreedCooldown();
        }
    }

    protected long getBreedCooldown() {
        return 400L;
    }
}
