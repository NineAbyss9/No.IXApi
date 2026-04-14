
package com.bilibili.player_ix.noixmod_api.item.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TargetSelector
extends Item {
    public static final String SELECTED = "Selected";
    public static final String FIRST = "First";
    public static final String SECOND = "Second";
    public TargetSelector() {
        super(new Properties().stacksTo(1).fireResistant());
    }

    public ItemStack getDefaultInstance() {
        var stack = super.getDefaultInstance();
        setSelected(stack, false);
        return stack;
    }

    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if (isSelected(stack)) {
            if (entity instanceof LivingEntity living) {
                setSecond(stack, living.getId());
            }
            this.setTarget(player.level(), stack);
        } else {
            setSelected(stack, true);
            setFirst(stack, entity.getId());
        }
        return true;
    }

    public void setTarget(Level pLevel, ItemStack stack) {
        var first = pLevel.getEntity(getFirst(stack));
        var second = pLevel.getEntity(getSecond(stack));
        if (first instanceof Mob mob) {
            if (second instanceof LivingEntity entity) {
                mob.setTarget(entity);
            }
        }
        if (second instanceof Mob mob) {
            if (first instanceof LivingEntity entity) {
                mob.setTarget(entity);
            }
        }
        setSelected(stack, false);
        setFirst(stack, -1);
        setSecond(stack, -1);
    }

    public static boolean isSelected(ItemStack stack) {
        var tag = stack.getOrCreateTag();
        return tag.contains(SELECTED) && tag.getBoolean(SELECTED);
    }

    public static void setSelected(ItemStack stack, boolean flag) {
        stack.getOrCreateTag().putBoolean(SELECTED, flag);
    }

    public static int getFirst(ItemStack stack) {
        var tag = stack.getOrCreateTag();
        return tag.contains(FIRST) ? tag.getInt(FIRST) : -1;
    }

    public static void setFirst(ItemStack stack, int id) {
        stack.getOrCreateTag().putInt(FIRST, id);
    }

    public static int getSecond(ItemStack stack) {
        var tag = stack.getOrCreateTag();
        return tag.contains(SECOND) ? tag.getInt(SECOND) : -1;
    }

    public static void setSecond(ItemStack stack, int id) {
        stack.getOrCreateTag().putInt(SECOND, id);
    }
}
