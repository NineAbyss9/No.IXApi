
package com.bilibili.player_ix.noixmod_api.entities.projectile.summon;

import com.github.NineAbyss9.ix_api.api.mobs.Ownable;
import com.bilibili.player_ix.noixmod_api.entities.servant.OwnedEntity;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIParticleTypes;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import com.bilibili.player_ix.noixmod_api.util.WorldUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SummonEntity
extends OwnedEntity {
    @Nullable
    private Entity entity;
    private EntityType<?> entityType;
    private boolean isDangerous = false;
    private MobSpawnType spawnType = MobSpawnType.MOB_SUMMONED;
    public SummonEntity(EntityType<? extends SummonEntity> type, Level level) {
        super(type, level);
    }

    public EntityType<?> getType() {
        return NoixmodAPIEntities.SUMMON_ENTITY.get();
    }

    public void tick() {
        super.tick();
        List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(
                1.5, 6, 1.5));
        if (!list.isEmpty() && this.isDangerous()) {
            LivingEntity owner = this.getOwner();
            if (owner != null) {
                for (LivingEntity living : list) {
                    if (!MobUtils.canHurt(living, owner)) {
                        continue;
                    }
                    living.hurt(this.damageSources().starve(), 6F);
                }
            }
        }
        if (this.level().isClientSide()) {
            for (int i = 0; i < 2;++i) {
                this.level().addParticle(NoixmodAPIParticleTypes.SUMMON_PARTICLE.get(),
                        this.getRandomX(0.5), this.getRandomY(), this.getRandomZ(0.5),
                        0, 0, 0);
            }
        }
        if (!this.onGround()) {
            MobUtils.moveToGround(this);
        }
    }

    public boolean hasLife() {
        return true;
    }

    public int getDefaultLifeTime() {
        if (this.fast()) {
            return 20;
        }
        return 40;
    }

    public boolean isDangerous() {
        return isDangerous;
    }

    public void setDangerous(boolean dangerous) {
        this.isDangerous = dangerous;
    }

    public boolean fast() {
        return this.getOwner() != null && MobUtils.isHalfHealth(this.getOwner());
    }

    public void entity(Entity o) {
        this.entity = o;
    }

    public void entity(EntityType<?> entityType) {
        this.entityType = entityType;
    }

    public void type(MobSpawnType type) {
        this.spawnType = type;
    }

    @Nullable
    public Entity getEntity() {
        return entity;
    }

    public void handleDeath() {
        if (!this.level().isClientSide()) {
            if (entity != null) {
                ServerLevel level = (ServerLevel) this.level();
                entity.moveTo(this.blockPosition(), 0, 0);
                if (entity instanceof Ownable ownable) {
                    ownable.setOwner(this.getOwner());
                }
                if (entity instanceof Mob mob) {
                    WorldUtil.nullableFinalizeSpawn(mob, level, level.getCurrentDifficultyAt(this.blockPosition()), this.spawnType);
                }
                level.addFreshEntity(entity);
            } else if (entityType != null) {
                ServerLevel level = (ServerLevel) this.level();
                Entity vEntity = entityType.create(level);
                if (vEntity != null) {
                    vEntity.moveTo(this.blockPosition(), 0, 0);
                    if (vEntity instanceof Ownable ownable) {
                        ownable.setOwner(this.getOwner());
                    }
                    if (vEntity instanceof Mob mob) {
                        WorldUtil.nullableFinalizeSpawn(mob, level, level.getCurrentDifficultyAt(this.blockPosition()),
                                this.spawnType);
                    }
                    level.addFreshEntity(vEntity);
                }
            }
        }
    }
}
