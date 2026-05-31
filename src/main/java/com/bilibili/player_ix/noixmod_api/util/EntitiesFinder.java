
package com.bilibili.player_ix.noixmod_api.util;

import com.github.NineAbyss9.ix_api.api.annotation.ServerOnly;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.UUID;

/// All are {@linkplain Nullable}
public record EntitiesFinder(Level source) {
    public Entity getEntity(UUID uuid)
    {
        return getEntity(source, uuid);
    }

    public Entity getEntity(int id)
    {
        return getEntity(source, id);
    }

    public LivingEntity getLivingEntity(UUID uuid)
    {
        return getLivingEntity(source, uuid);
    }

    public LivingEntity getLivingEntity(int id)
    {
        return getLivingEntity(source, id);
    }

    @ServerOnly
    public static Entity getEntity(Level level, UUID uuid) {
        if (uuid == null) {
            return null;
        }
        return ((ServerLevel)level).getEntity(uuid);
    }

    public static Entity getEntity(Level level, int id) {
        return level.getEntity(id);
    }

    @ServerOnly
    public static LivingEntity getLivingEntity(Level level, UUID uuid) {
        return getEntity(level, uuid) instanceof LivingEntity living ? living : null;
    }

    public static LivingEntity getLivingEntity(Level level, int id) {
        return getEntity(level, id) instanceof LivingEntity living ? living : null;
    }
}
