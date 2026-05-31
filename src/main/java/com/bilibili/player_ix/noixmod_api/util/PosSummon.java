
package com.bilibili.player_ix.noixmod_api.util;

import com.github.NineAbyss9.ix_api.api.annotation.ServerOnly;
import com.github.NineAbyss9.ix_api.util.MutableVec3;
import com.github.NineAbyss9.ix_api.util.Vec9;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;

import java.util.function.Consumer;

@ServerOnly
public class PosSummon<T extends Entity> extends Summon {
    private final EntityType<? extends T> entityType;
    private final Vec3 position;
    private ServerLevel level;
    PosSummon(Vec3 vec3, EntityType<? extends T> pType) {
        position = vec3;
        this.entityType = pType;
    }

    public double getX() {
        return this.position.x;
    }

    public double getY() {
        return this.position.y;
    }

    public double getZ() {
        return this.position.z;
    }

    public void lock() {
    }

    public boolean hasLevel()
    {
        return this.level != null;
    }

    public PosSummon<T> pickLevel(ServerLevel pLevel) {
        this.level = pLevel;
        return this;
    }

    public Vec3 position() {
        return position;
    }

    public void summon()
    {
        Entity entity = entityType.create(level);
        if (entity == null) return;
        entity.moveTo(position);
        level.addFreshEntity(entity);
    }

    public void summon(Consumer<? super T> consumer) {
        T entity = entityType.create(level);
        if (entity == null) {
            return;
        }
        entity.moveTo(position);
        consumer.accept(entity);
        level.addFreshEntity(entity);
    }

    public EntityType<? extends T> getEntityType() {
        return entityType;
    }

    public static <T extends Entity> PosSummon<T> create(MutableVec3 vec3, EntityType<T> pType) {
        return new PosSummon<>(vec3.toVec3(), pType);
    }

    public static <T extends Entity> PosSummon<T> create(Vec3 vec3, EntityType<T> pType) {
        return new PosSummon<>(vec3, pType);
    }

    public static <T extends Entity> PosSummon<T> create(BlockPos pos, EntityType<T> pEntity) {
        return create(Vec9.of(pos), pEntity);
    }
}
