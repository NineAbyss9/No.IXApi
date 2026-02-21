
package com.bilibili.player_ix.noixmod_api.commands;

import com.bilibili.player_ix.noixmod_api.util.WorldUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.phys.Vec3;

public class CommandSummoner {
    public CommandSummoner() {
    }

    static int summon(CommandSourceStack stack, EntityType<?> entityType, ServerLevel level, Vec3 pos) {
        Entity entity = entityType.create(level);
        if (entity != null) {
            entity.moveTo(pos);
            if (entity instanceof Mob mob) {
                WorldUtil.finalizeSpawn(mob, level, level.getCurrentDifficultyAt(BlockPos.containing(pos)),
                        MobSpawnType.COMMAND, null, null);
            }
            if (level.addFreshEntity(entity)) {
                stack.sendSuccess(()-> Component.translatable("commands.summon.success", 
                                entity.getDisplayName()), true);
                return 1;
            }
        }
        return 0;
    }
}
