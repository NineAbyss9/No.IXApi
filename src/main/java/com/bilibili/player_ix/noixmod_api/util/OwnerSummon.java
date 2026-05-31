
package com.bilibili.player_ix.noixmod_api.util;

import com.github.NineAbyss9.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.entities.projectile.summon.SummonEntity;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.github.NineAbyss9.ix_api.api.mobs.Ownable;
import com.github.NineAbyss9.ix_api.api.mobs.OwnableMob;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;

import java.util.Objects;
import java.util.function.Predicate;

public class OwnerSummon extends Summon {
    private final LivingEntity owner;
    public OwnerSummon(LivingEntity living) {
        this.owner = living;
    }

    public static boolean canSummon(ServerLevel level, LivingEntity lie, int max) {
        return OwnerSummon.canSummon(level, lie, max, OwnableMob.ownablePredicate());
    }

    public static boolean canSummon(ServerLevel level, LivingEntity lie, int max, Predicate<Entity> predicate) {
        int count = 0;
        for (Entity entity : level.getAllEntities()) {
            if (!(entity instanceof LivingEntity livingEntity)) continue;
            if (!(entity instanceof Ownable own)) continue;
            if (!predicate.test(livingEntity) || own.getOwner() != lie || !livingEntity.isAlive()
                    || livingEntity.isRemoved()) continue;
            ++count;
        }
        return count < max;
    }

    public static boolean canSummonEntity(ServerLevel level, LivingEntity lie, int max, Predicate<Entity> entityPredicate) {
        int count = 0;
        for (Entity entity : level.getAllEntities()) {
            if (!(entity instanceof Ownable ownable)) continue;
            if (ownable.getOwner() == lie || !entity.isAlive() || entity.isRemoved()) continue;
            if (!entityPredicate.test(entity)) continue;
            ++count;
        }
        return count < max;
    }

    public <T extends Entity & Ownable> void integerSummon(T entity, int i) {
        integerSummon(entity, i, getServerLevel());
    }

    public <T extends Entity & Ownable> void integerSummon(T entity, int range, ServerLevel level) {
        if (entity.isRemoved()) {
            return;
        }
        entity.moveTo(this.blockPos().offset(Maths.randomInteger(range), 0, Maths.randomInteger(range)),
                0, 0);
        entity.setOwner(this.owner);
        MobUtils.moveToGround(entity);
        if (entity instanceof Mob mob) {
            WorldUtil.nullableFinalizeSpawn(mob, level, this.getDifficult(), MobSpawnType.MOB_SUMMONED);
        }
        level.addFreshEntity(entity);
    }

    public <T extends EntityType<? extends Entity>> void summonWithSummonEntity(T entity,
                                                                                int distance, boolean flag) {
        SummonEntity summon = NoixmodAPIEntities.SUMMON_ENTITY.get().create(this.owner.level());
        if (summon == null) return;
        summon.entity(entity);
        summon.setDangerous(flag);
        summon.moveTo(this.blockPos().offset(Maths.randomInteger(distance), 0, Maths.randomInteger(distance)),
                0, 0);
        summon.setOwner(this.owner);
        this.owner.level().addFreshEntity(summon);
    }

    public void moveTo(int x, int y, int z, Entity entity) {
        entity.moveTo(this.getOwner().blockPosition().offset(x, y, z), 0, 0);
    }

    public LivingEntity getOwner() {
        return this.owner;
    }

    public BlockPos blockPos() {
        return this.owner.blockPosition();
    }

    public ServerLevel getServerLevel() {
        return (ServerLevel)this.owner.level();
    }

    public double[] projectileDouble(LivingEntity target) {
        return new double[]{
                target.getX() - this.getX(),
                target.getY(0.5) - this.getY(0.5),
                target.getZ() - this.getZ()
        };
    }

    public static double[] projectileDouble(LivingEntity target, Entity entity) {
        return new double[] {
                target.getX() - entity.getX(),
                target.getY() - entity.getY(),
                target.getZ() - entity.getZ()
        };
    }

    public DifficultyInstance getDifficult() {
        return this.owner.level().getCurrentDifficultyAt(this.blockPos());
    }

    public double getX() {
        return this.owner.getX();
    }

    public double getY() {
        return this.owner.getY();
    }

    public double getZ() {
        return this.owner.getZ();
    }

    public double getX(double d) {
        return this.owner.getX(d);
    }

    public double getY(double d) {
        return this.owner.getY(d);
    }

    public double getZ(double d) {
        return this.owner.getZ(d);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else {
            if (obj instanceof OwnerSummon ownerSummon) {
                return this.owner == ownerSummon.owner;
            }
            return false;
        }
    }

    public int hashCode() {
        return Objects.hashCode(this.owner);
    }
}
