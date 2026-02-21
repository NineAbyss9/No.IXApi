
package com.bilibili.player_ix.noixmod_api.util;

import com.github.NineAbyss9.ix_api.ix_api.api.annotation.ServerOnly;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.UUID;

@ParametersAreNonnullByDefault
public record EntitiesFinder(Entity pSourceEntity) {

    @Nullable
    @ServerOnly
    public static Entity getEntity(Level level, @Nullable UUID uuid) {
        if (uuid == null) {
            return null;
        }
        return ((ServerLevel)level).getEntity(uuid);
    }

    @Nullable
    public static Entity getEntity(Level level, int id) {
        return level.getEntity(id);
    }

    @ServerOnly
    @Nullable
    public static LivingEntity getLivingEntity(Level level, @Nullable UUID uuid) {
        return getEntity(level, uuid) instanceof LivingEntity living ? living : null;
    }

    @Nullable
    public static LivingEntity getLivingEntity(Level level, int id) {
        return getEntity(level, id) instanceof LivingEntity living ? living : null;
    }
}
