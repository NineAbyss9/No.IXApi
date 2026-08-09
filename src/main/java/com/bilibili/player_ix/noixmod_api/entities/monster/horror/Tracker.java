
package com.bilibili.player_ix.noixmod_api.entities.monster.horror;

import com.bilibili.player_ix.noixmod_api.world.HorrorModeManager;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.AbstractHorrorMob;
import com.github.NineAbyss9.ix_api.api.mobs.ai.goal.ApiMeleeAttackGoal;
import com.github.NineAbyss9.ix_api.util.Maths;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.concurrent.ThreadLocalRandom;

/**Tracker class.A human-like who was looking at you.*/
public class Tracker
extends AbstractHorrorMob {
    private final int actNameId;
    private Component actName;
    private int trackingTime;
    public static final int NORMAL = 0;
    public static final int CAVE = 1;
    public static final int ATTACKABLE = 2;
    public static final int BACK = 3;
    private static final EntityDataAccessor<Integer> DATA_LIFE;
    private static final EntityDataAccessor<Boolean> DATA_LOOKED;
    private static final EntityDataAccessor<Boolean> CAN_ATTACK;
    private static final EntityDataAccessor<Integer> DATA_TYPE;
    public Tracker(EntityType<? extends Tracker> pType, Level pLevel) {
        super(pType, pLevel);
        this.actNameId = ThreadLocalRandom.current().nextInt(4);
        this.actName = this.getActName(this.actNameId);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_LIFE, 6000);
        this.entityData.define(DATA_LOOKED, false);
        this.entityData.define(CAN_ATTACK, false);
        this.entityData.define(DATA_TYPE, 0);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new TrackerAttackGoal(this, 1.0D));
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Player.class, false));
    }

    public void tick()
    {
        if (this.getDataType() == BACK) {
            this.setNoGravity(true);
            this.noPhysics = true;
        }
        super.tick();
    }

    public void aiStep() {
        super.aiStep();
        if (this.level().isClientSide) {
            LivingEntity target = this.getTarget();
            if (target instanceof Player player) {
                if (isLookingAtMe(player) && !this.entityData.get(DATA_LOOKED)) {
                    this.actName = Component.translatable("entity.minecraft.bat");
                }
            }
        } else {
            if (getLife() > 0) {
                setLife(getLife() - 1);
            } else {
                this.discard();
                return;
            }
            LivingEntity target = this.getTarget();
            if (target instanceof Player player) {
                if (isLookingAtMe(player) && !this.entityData.get(DATA_LOOKED)) {
                    this.entityData.set(DATA_LOOKED, true);
                    this.actName = Component.translatable("entity.minecraft.bat");
                    if (this.canAttack() || this.getDataType() == BACK) {
                        HorrorModeManager.playStrangeSound(this);
                        this.discard();
                        return;
                    }
                }
                if (closerThan(target, 2.5D)) {
                    if (!this.navigation.isDone()) {
                        this.getNavigation().stop();
                    }
                } else {
                    if (this.getDataType() == NORMAL) {
                        this.trackingTime++;
                        if (this.trackingTime > Maths.toTick(30)) {
                            this.moveTo(player.blockPosition().relative(player.getDirection()).above(), 0, 0);
                            this.setBack();
                            HorrorModeManager.playStrangeSound(player);
                            this.setLife(600);
                            return;
                        }
                    }
                    if (this.tickCount % 20 == 0) {
                        this.getNavigation().moveTo(target, 1.0D);
                    }
                }
            }
        }
    }

    public int getLevel() {
        return 1;
    }

    public Component getDisplayName() {
        return this.actName;
    }

    public Component getName() {
        return this.actName;
    }

    private boolean isLookingAtMe(Player pPlayer) {
        Vec3 vec3 = pPlayer.getViewVector(1.0F).normalize();
        Vec3 vec31 = new Vec3(this.getX() - pPlayer.getX(), this.getEyeY() - pPlayer.getEyeY(), this.getZ()
                - pPlayer.getZ());
        double d0 = vec31.length();
        vec31 = vec31.normalize();
        double d1 = vec3.dot(vec31);
        return d1 > 1.0D - 0.025D / d0 && pPlayer.hasLineOfSight(this);
    }

    public Component getActName(int actMobId) {
        return switch (actMobId) {
            case 0 -> Component.translatable("entity.minecraft.pig");
            case 1 -> Component.translatable("entity.minecraft.sheep");
            case 2 -> Component.translatable("entity.minecraft.cow");
            case 3 -> Component.translatable("entity.minecraft.chicken");
            default -> Component.translatable("entity.minecraft.horse");
        };
    }

    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this, this.actNameId);
    }

    public void recreateFromPacket(ClientboundAddEntityPacket pPacket) {
        super.recreateFromPacket(pPacket);
        this.actName = this.getActName(pPacket.getData());
    }

    public boolean isInvulnerable()
    {
        return this.getDataType() == CAVE || this.getDataType() == BACK;
    }
    public void setAttackable() {this.setType(ATTACKABLE);}
    public void setBack() {this.setType(BACK);}
    public void setCave() {this.setType(CAVE);}
    public int getLife() {return this.entityData.get(DATA_LIFE);}
    public void setLife(int pLife) {this.entityData.set(DATA_LIFE, pLife);}
    public boolean canAttack() {return this.entityData.get(CAN_ATTACK);}
    public void setCanAttack(boolean canAttack) {this.entityData.set(CAN_ATTACK, canAttack);}
    public void setCanAttack() {this.entityData.set(CAN_ATTACK, true);
    this.setAttackable();}
    public int getDataType() {return this.entityData.get(DATA_TYPE);}
    public void setType(int pType) {this.entityData.set(DATA_TYPE, pType);}
    protected void playStepSound(BlockPos pPos, BlockState pState) {
        if (this.level().canSeeSky(pPos)) return;
        super.playStepSound(pPos, pState);
    }
    protected void playBlockFallSound() {}
    protected void playSwimSound(float pVolume) {}
    public void die(DamageSource pDamageSource) {
        //this.die();
        super.die(pDamageSource);
    }

    public boolean doHurtTarget(Entity pEntity)
    {
        if (this.level().isClientSide) return false;
        HorrorModeManager.playStrangeSound(this);
        if (pEntity instanceof LivingEntity entity) {
            if (entity instanceof Player player && player.isCreative()) return false;
            if (entity.getHealth() > 2.0F) {
                entity.setHealth(2.0F);
            } else {
                entity.hurt(this.damageSources().mobAttack(this), 1.0F);
            }
        }
        this.discard();
        return true;
    }

    public void addAdditionalSaveData(CompoundTag pCompound)
    {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("Timer", this.getLife());
        pCompound.putBoolean("CanAttack", this.canAttack());
        pCompound.putInt("Type", this.getDataType());
    }

    public void readAdditionalSaveData(CompoundTag pCompound)
    {
        super.readAdditionalSaveData(pCompound);
        this.setLife(pCompound.getInt("Timer"));
        this.setCanAttack(pCompound.getBoolean("CanAttack"));
    }

    protected void actuallyHurt(DamageSource pSource, float pAmount) {
        super.actuallyHurt(pSource, pAmount / 1.5F);
    }

    public static AttributeSupplier createAttributes() {
        return createPathAttributes().add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 5).add(Attributes.FOLLOW_RANGE, 72)
                .add(Attributes.MAX_HEALTH, 40).build();
    }

    static {
        DATA_LIFE = SynchedEntityData.defineId(Tracker.class, EntityDataSerializers.INT);
        DATA_LOOKED = SynchedEntityData.defineId(Tracker.class, EntityDataSerializers.BOOLEAN);
        CAN_ATTACK = SynchedEntityData.defineId(Tracker.class, EntityDataSerializers.BOOLEAN);
        DATA_TYPE = SynchedEntityData.defineId(Tracker.class, EntityDataSerializers.INT);
    }

    private static final class TrackerAttackGoal extends ApiMeleeAttackGoal
    {
        private final Tracker tracker;
        public TrackerAttackGoal(Tracker finder, double speed)
        {
            super(finder, speed);
            this.tracker = finder;
        }

        public boolean canUse()
        {
            if (!this.tracker.canAttack()) return false;
            return super.canUse();
        }
    }
}
