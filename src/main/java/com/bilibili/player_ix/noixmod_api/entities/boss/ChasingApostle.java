
package com.bilibili.player_ix.noixmod_api.entities.boss;

import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.IHorror;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.world.HorrorModeManager;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

public class ChasingApostle
extends Apostle
implements IHorror
{
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
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, false));
    }

    public void tick() {
        if (!HorrorModeManager.horrorModeEnabled()) {
            this.setRemoved(RemovalReason.KILLED);
            return;
        }
        super.tick();
        if (!this.level().isClientSide) {
            LivingEntity var10000 = this.getTarget();
            if (var10000 != null) {
                if (this.getNavigation().isDone())
                    this.getNavigation().moveTo(var10000, this.getDataSpeed());
                if (this.closerThan(var10000, 1.5D)) {
                    this.onChase(var10000);
                }
            }
        }
    }

    protected void handleBossEvent()
    {
        if (this.tickCount % 5 == 0) {
            this.horrorEvent.update();
        }
        this.horrorEvent.setProgress(this.getTrueHealth() / 10.0F);
    }

    public void summonServant()
    {
    }

    public void summonServants()
    {
    }

    public void summonRangedServant()
    {
    }

    public void teleport()
    {
    }

    public void chaseTeleport()
    {
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.getDirectEntity() instanceof Projectile) {
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

    public int getTrueHealth() {
        return this.entityData.get(DATA_HEALTH);
    }

    public int getLevel()
    {
        return 3;
    }

    public boolean isAlive() {
        return this.health() > 0;
    }

    public boolean isDeadOrDying() {
        return this.health() <= 0;
    }

    public int health() {
        return this.entityData.get(DATA_HEALTH);
    }

    public void hurt() {
        this.entityData.set(DATA_HEALTH, health() - 1);
        if (this.isDeadOrDying()) {
            //HorrorModeSavedData.getInstanceUnsafe().updateNextMobWillSpawn(-1);
            this.discard();
        }
    }

    private float getDataSpeed() {
        return this.entityData.get(DATA_SPEED);
    }

    private void setDataSpeed(float pSpeed) {
        this.entityData.set(DATA_SPEED, pSpeed);
    }

    private void onChase(LivingEntity player) {
        player.setHealth(1.0F);
        this.setRemoved(RemovalReason.KILLED);
    }

    public EntityType<?> getType() {
        return NoixmodAPIEntities.CH_APOSTLE.get();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 72)
                .add(Attributes.ATTACK_DAMAGE, 1)
                .add(Attributes.MAX_HEALTH, 10)
                .add(Attributes.ARMOR, 0)
                .add(Attributes.MOVEMENT_SPEED, 0.3);
    }

    static {
        DATA_HEALTH = SynchedEntityData.defineId(ChasingApostle.class, EntityDataSerializers.INT);
        DATA_SPEED = SynchedEntityData.defineId(ChasingApostle.class, EntityDataSerializers.FLOAT);
    }
}
