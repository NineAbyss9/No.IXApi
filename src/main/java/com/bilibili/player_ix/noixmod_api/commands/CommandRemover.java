
package com.bilibili.player_ix.noixmod_api.commands;

import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.Collection;

public class CommandRemover {
    public CommandRemover() {
    }

    static int die(CommandSourceStack stack, Collection<? extends Entity> collect, DamageSource source) {
        for (Entity e : collect) {
            if (e instanceof LivingEntity)
                ((LivingEntity)e).die(source);
        }
        int size = collect.size();
        if (size == 1)
            stack.sendSuccess(() -> Component.translatable("commands.kill.success.single",
                    collect.iterator().next().getDisplayName()), false);
        else
            stack.sendSuccess(() -> Component.translatable("commands.kill.success.multiple",
                    collect.size()), false);
        return size;
    }

    static int remove(CommandSourceStack p_137814_, Collection<? extends Entity> p_137815_, int removeReason) {
        for (Entity $$2 : p_137815_) {
            switch (removeReason) {
                case 0: {
                    $$2.remove(Entity.RemovalReason.KILLED);
                    break;
                }
                case 1: {
                    $$2.remove(Entity.RemovalReason.DISCARDED);
                    break;
                }
                case 2: {
                    $$2.remove(Entity.RemovalReason.CHANGED_DIMENSION);
                    break;
                }
                case 3: {
                    $$2.remove(Entity.RemovalReason.UNLOADED_TO_CHUNK);
                }
                case 4: {
                    $$2.remove(Entity.RemovalReason.UNLOADED_WITH_PLAYER);
                    break;
                }
                default: throw new CommandRuntimeException(Component.literal("Entered integer out of index."));
            }
        }
        int size = p_137815_.size();
        if (size == 1)
            p_137814_.sendSuccess(() -> Component.translatable("commands.kill.success.single",
                    p_137815_.iterator().next().getDisplayName()), false);
        else
            p_137814_.sendSuccess(() -> Component.translatable("commands.kill.success.multiple",
                    p_137815_.size()), false);
        return size;
    }

    static int setRemoved(CommandSourceStack p_137814_, Collection<? extends Entity> p_137815_, int removeReason) {
        for (Entity $$2 : p_137815_) {
            switch (removeReason) {
                case 0: {
                    $$2.setRemoved(Entity.RemovalReason.KILLED);
                    break;
                }
                case 1: {
                    $$2.setRemoved(Entity.RemovalReason.DISCARDED);
                    break;
                }
                case 2: {
                    $$2.setRemoved(Entity.RemovalReason.CHANGED_DIMENSION);
                    break;
                }
                case 3: {
                    $$2.setRemoved(Entity.RemovalReason.UNLOADED_TO_CHUNK);
                }
                case 4: {
                    $$2.setRemoved(Entity.RemovalReason.UNLOADED_WITH_PLAYER);
                    break;
                }
                default: throw new CommandRuntimeException(Component.literal("Entered integer out of index."));
            }
        }
        int size = p_137815_.size();
        if (size == 1) {
            p_137814_.sendSuccess(() -> Component.translatable("commands.kill.success.single",
                    p_137815_.iterator().next().getDisplayName()), false);
        } else {
            p_137814_.sendSuccess(() -> Component.translatable("commands.kill.success.multiple",
                    p_137815_.size()), false);
        }
        return size;
    }

    static int onRemovedFromWorld(CommandSourceStack p_137814_, Collection<? extends Entity> p_137815_) {
        for (Entity $$2 : p_137815_) {
            $$2.onRemovedFromWorld();
        }
        int size = p_137815_.size();
        if (size == 1) {
            p_137814_.sendSuccess(() -> Component.translatable("commands.kill.success.single",
                    p_137815_.iterator().next().getDisplayName()), false);
        } else {
            p_137814_.sendSuccess(() -> Component.translatable("commands.kill.success.multiple",
                    p_137815_.size()), false);
        }
        return size;
    }

    static int onRemove(CommandSourceStack stack, Collection<? extends Entity> collection) {
        for (Entity $$2 : collection) {
            MobUtils.onRemove($$2, Entity.RemovalReason.KILLED);
        }
        int size = collection.size();
        if (size == 1)
            stack.sendSuccess(() -> Component.translatable("commands.kill.success.single",
                    collection.iterator().next().getDisplayName()), true);
        else
            stack.sendSuccess(() -> Component.translatable("commands.kill.success.multiple",
                    size), true);
        return size;
    }
}
