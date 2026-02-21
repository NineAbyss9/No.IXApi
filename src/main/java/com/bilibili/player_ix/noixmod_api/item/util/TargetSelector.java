
package com.bilibili.player_ix.noixmod_api.item.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class TargetSelector
extends Item {
    @Nullable
    protected Mob firstMob;
    @Nullable
    protected LivingEntity secondMob;
    protected boolean isSelected;
    public TargetSelector() {
        super(new Properties().stacksTo(1).fireResistant());
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if (this.isSelected) {
            if (entity instanceof LivingEntity living) {
                this.secondMob = living;
            }
            this.setTarget();
        } else {
            if (this.firstMob == null && entity instanceof Mob mob) {
                this.isSelected = true;
                this.firstMob = mob;
            }
        }
        return true;
    }

    public void setTarget() {
        if (this.firstMob != null) {
            this.firstMob.setTarget(this.secondMob);
        }
        this.firstMob = null;
        this.secondMob = null;
        this.isSelected = false;
    }
}
