
package com.bilibili.player_ix.noixmod_api.item.magic;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;

public abstract class Staff
extends Item {
    public Staff(Properties p_41383_) {
        super(p_41383_);
    }

    public abstract void castSpell(LivingEntity living);
}
