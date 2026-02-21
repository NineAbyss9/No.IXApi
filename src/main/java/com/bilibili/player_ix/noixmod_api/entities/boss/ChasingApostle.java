
package com.bilibili.player_ix.noixmod_api.entities.boss;

import net.minecraft.CrashReport;
import net.minecraft.client.Minecraft;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

public class ChasingApostle
extends Apostle {
    private static final EntityDataAccessor<Integer> DATA_HEALTH;
    private static final EntityDataAccessor<Float> DATA_SPEED;
    public ChasingApostle(EntityType<? extends ChasingApostle> apostle, Level world) {
        super(apostle, world);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_HEALTH, 10);
        this.entityData.define(DATA_SPEED, 1.0F);
    }

    protected void registerGoals() {
    }

    public void tick() {
        super.tick();
        LivingEntity var100000 = this.getTarget();
        if (var100000 != null) {
            this.getNavigation().moveTo(var100000, this.getDataSpeed());
        }
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.getDirectEntity() instanceof Projectile) {
            //pAmount = 1;
            this.hurt();
            this.setDataSpeed(this.getDataSpeed() + 1);
            return true;
        }
        return false;
    }

    protected void actuallyHurt(DamageSource ds, float var0) {
    }

    public void setHealth(float amount) {
    }

    public int health() {
        return this.entityData.get(DATA_HEALTH);
    }

    public void hurt() {
        this.entityData.set(DATA_HEALTH, health() - 1);
        if (this.health() <= 0) {
            this.discard();
        }
    }

    private float getDataSpeed() {
        return this.entityData.get(DATA_SPEED);
    }

    private void setDataSpeed(float pSpeed) {
        this.entityData.set(DATA_SPEED, pSpeed);
    }

    private void onChase() {
        this.setRemoved(RemovalReason.KILLED);
        Minecraft.crash(new CrashReport("$&*^&*&^*#!", new RuntimeException()));
    }

    static {
        DATA_HEALTH = SynchedEntityData.defineId(ChasingApostle.class, EntityDataSerializers.INT);
        DATA_SPEED = SynchedEntityData.defineId(ChasingApostle.class, EntityDataSerializers.FLOAT);
    }
}
