
package com.bilibili.player_ix.noixmod_api.entities.projectile;

import com.github.NineAbyss9.ix_api.api.mobs.ApiTargeting;
import com.github.NineAbyss9.ix_api.util.Vec9;
import com.bilibili.player_ix.noixmod_api.entities.servant.core.OwnedEntity;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class Cage
extends OwnedEntity
implements ApiTargeting {
    @Nullable
    private LivingEntity target;
    public float yBodyRotO;
    public float yHeadRotO;
    public Cage(EntityType<? extends Cage> type, Level level) {
        super(type, level);
    }

    public void tick() {
        super.tick();
        if (this.getTarget() != null) {
            this.move(MoverType.SELF, Vec9.moveToVec(this, this.getTarget(), 0.15));
            if (this.closerThan(this.getTarget(), 2)) {
                this.getTarget().getDeltaMovement().add(Vec9.moveToVec(this.getTarget(), this, 0.05));
            }
        }
        this.yRotO = this.getYRot();
        this.yHeadRotO = this.getYHeadRot();
    }

    protected void handleDeath() {
        if (!this.level().isClientSide) {
            List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox()
                    .inflate(1, 1, 1), living -> MobUtils.canHurt(living, this));
            if (!list.isEmpty()) {
                for (LivingEntity entity : list){
                    entity.hurt(this.damageSources().genericKill(), 12f);
                }
            }
        }
    }

    public boolean hasLife() {
        return true;
    }

    @Nullable
    public UUID getTargetUuid() {
        return null;
    }

    public int getDefaultLifeTime() {
        return 40;
    }

    public void setTarget(@Nullable LivingEntity living) {
        this.target = living;
    }

    @Nullable
    public LivingEntity getTarget() {
        return target;
    }
}
