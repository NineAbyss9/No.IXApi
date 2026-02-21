
package com.bilibili.player_ix.noixmod_api.entities.projectile;

import com.github.NineAbyss9.ix_api.api.mobs.ApiVillager;
import com.github.NineAbyss9.ix_api.api.mobs.Nihilistic;
import com.github.NineAbyss9.ix_api.api.mobs.Ownable;
import com.github.NineAbyss9.ix_api.api.mobs.OwnableMob;
import com.bilibili.player_ix.noixmod_api.entities.boss.NihilisticLord;
import com.bilibili.player_ix.noixmod_api.entities.villager.VillagerGolem;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class WaterTrap
extends Entity
implements Ownable {
    public int warmupDelayTicks;
    private boolean sentSpikeEvent;
    private int lifeTicks = 24;
    private boolean clientSideAttackStarted;
    @Nullable
    private LivingEntity owner;
    @Nullable
    private UUID ownerUUID;
    private boolean flag;

    public WaterTrap(Level $$0, double $$1, double $$2, double $$3, float $$4, int $$5, LivingEntity $$6) {
        this(NoixmodAPIEntities.WATER_TRAP.get(), $$0);
        this.warmupDelayTicks = $$5;
        this.setOwner($$6);
        this.setYRot($$4 * 57.295776f);
        this.setPos($$1, $$2, $$3);
    }

    public WaterTrap(EntityType<WaterTrap> type, Level world) {
        super(type, world);
    }

    public boolean isFlag() {
        return this.flag;
    }

    public void setFlag(boolean b) {
        this.flag = b;
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public void setOwner(@Nullable LivingEntity $$0) {
        this.owner = $$0;
        this.ownerUUID = $$0 == null ? null : $$0.getUUID();
    }

    @Override
    public void setOwnerUUID(@Nullable UUID ownerUUID) {
        this.ownerUUID = ownerUUID;
    }

    @Override
    @Nullable
    public LivingEntity getOwner() {
        Entity $$0;
        if (this.owner == null && this.ownerUUID != null && this.level() instanceof ServerLevel && ($$0 = ((ServerLevel) this.level()).getEntity(this.ownerUUID)) instanceof LivingEntity) {
            this.owner = (LivingEntity) $$0;
        }
        return this.owner;
    }

    @Nullable
    @Override
    public UUID getOwnerUUID() {
        return this.ownerUUID;
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag $$0) {
        this.warmupDelayTicks = $$0.getInt("Warmup");
        if ($$0.hasUUID("Owner")) {
            this.ownerUUID = $$0.getUUID("Owner");
        }
    }

    public void setLifeTick(int nt) {
        this.lifeTicks = nt;
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag $$0) {
        $$0.putInt("Warmup", this.warmupDelayTicks);
        if (this.ownerUUID != null) {
            $$0.putUUID("Owner", this.ownerUUID);
        }
    }

    public void Knockback(@NotNull LivingEntity lie) {
        lie.push(0, 1.1 - lie.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE), 0);
    }

    @Override
    public void tick() {
        super.tick();
        --this.lifeTicks;
        if (this.level().isClientSide) {
            this.level().addParticle(ParticleTypes.RAIN, this.getRandomX(0.5), this.getRandomY() + 0.25, this.getRandomZ(0.5), 0, -Math.random(), 0);
            if (this.clientSideAttackStarted) {
                if (this.lifeTicks == 14) {
                    for (int i = 0; i < 10; ++i) {
                        double x = this.random.nextGaussian() * 0.3;
                        double y = this.random.nextGaussian() * 0.3;
                        double z = this.random.nextGaussian() * 0.3;
                        this.level().addParticle(ParticleTypes.FALLING_WATER, this.getX(), this.getY() + 0.5, this.getZ(), x, y, z);
                    }
                }
            }
        } else if (--this.warmupDelayTicks < 0) {
            if (this.warmupDelayTicks == -8) {
                List<LivingEntity> $$7 = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(3, 0.1, 3));
                for (LivingEntity $$8 : $$7) {
                    if (this.isHostile()) {
                        if (!($$8 instanceof Nihilistic)) {
                            this.dealDamageTo($$8);
                            this.Knockback($$8);
                        }
                    } else {
                        if (!($$8 instanceof AbstractVillager) && !($$8 instanceof AbstractGolem) && !($$8 instanceof ApiVillager)) {
                            this.dealDamageTo($$8);
                            this.Knockback($$8);
                        }
                    }
                }
            }
            if (!this.sentSpikeEvent) {
                this.level().broadcastEntityEvent(this, (byte)4);
                this.sentSpikeEvent = true;
            }
        }
        if (this.lifeTicks < 0) {
            this.discard();
        }
        if (this.isFlag()) {
            if (this.lifeTicks == 4) {
                MobUtils.rangeHurt(4, 0.2, 4, this, this.damageSources().starve(), 8);
                this.discard();
            }
        }
    }

    public void dealDamageTo(LivingEntity $$0) {
        LivingEntity $$1 = this.getOwner();
        if ($$0 instanceof  Player l && l.isCreative()) {
            return;
        }
        if (!$$0.isAlive() || $$0.isInvulnerable() || $$0 == $$1 || ($$0 instanceof OwnableMob ownableMob && ownableMob.getOwner() == this.getOwner())) {
            return;
        }
        if ($$0 instanceof VillagerGolem villagerGolem && villagerGolem.getOwner() == this.getOwner()) {
            return;
        }
        if (this.isUnowned()) {
            $$0.hurt(this.damageSources().magic(), 10f);
        }
        if (!($$1 instanceof NihilisticLord)) {
            $$0.hurt(this.damageSources().indirectMagic(this, $$1), 10.0f);
        } else if (this.getOwner() instanceof NihilisticLord lord && lord.getPhase() == 1) {
            $$0.hurt(this.damageSources().indirectMagic(this, $$1), 10.0f);
        } else if (this.getOwner() != null && this.getOwner() instanceof NihilisticLord lord && lord.getPhase() != 1) {
            $$0.hurt(this.damageSources().fellOutOfWorld(), 10.0f);
        }
    }

    @Override
    public void handleEntityEvent(byte $$0) {
        super.handleEntityEvent($$0);
        if ($$0 == 4) {
            this.clientSideAttackStarted = true;
            if (!this.isSilent()) {
                this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.FISHING_BOBBER_SPLASH, this.getSoundSource(), 1.0f, this.random.nextFloat() * 0.2f + 0.85f, false);
            }
        }
    }
}
