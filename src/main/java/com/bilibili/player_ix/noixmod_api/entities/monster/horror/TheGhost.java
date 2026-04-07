
package com.bilibili.player_ix.noixmod_api.entities.monster.horror;

import com.bilibili.player_ix.noixmod_api.NoixmodAPI;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.AbstractHorrorMob;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIItems;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIParticleTypes;
import com.bilibili.player_ix.noixmod_api.util.EntityEventHandler;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import com.github.NineAbyss9.ix_api.api.mobs.IFlagMob;
import com.github.NineAbyss9.ix_api.api.mobs.ai.goal.MeleeGoal;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.NineAbyss9.math.MathSupport;
import org.jetbrains.annotations.Nullable;

public class TheGhost
extends AbstractHorrorMob
implements IFlagMob {
    private static final EntityDataAccessor<Integer> DATA_FLAGS;
    private static final EntityDataAccessor<Integer> DATA_ANI_TICK;
    public AnimationState attack = new AnimationState();
    public TheGhost(EntityType<? extends TheGhost> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(NoixmodAPIItems.BONE_SWORD.get()));
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS, 0);
        this.entityData.define(DATA_ANI_TICK, 0);
    }

    public void aiStep() {
        super.aiStep();
        if (this.level().isClientSide) {
            this.level().addParticle(NoixmodAPIParticleTypes.CORRUPTION.get(),
                    this.getRandomX(0.8), this.getRandomY() - 0.2, this.getRandomZ(0.8),
                    0, 0.1D, 0);
        } else {
            if (this.isFlag(0)) {
                if (this.closerThan(this.getTarget(), 3.5D)) {
                    this.setFlag(1);
                }
            }
            if (this.isFlag(1)) {
                this.attack();
            }
        }
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new MeleeGoal(this, 1.0));
        this.goalSelector.addGoal(2, new FloatGoal(this));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 0.8));
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        if (DATA_FLAGS.equals(pKey)) {
            if (this.level().isClientSide) {
                if (this.getFlag() == 1) {
                    this.attack.stop();
                    this.attack.startIfStopped(tickCount);
                }
            } else {
                if (this.getFlag() != 0 && this.getFlag() != 1) {
                    NoixmodAPI.LOGGER.warn("TheGhost {} has invalid flag {}, resetting to 0", this.getStringUUID(), this.getFlag());
                    this.setFlag(0);
                }
            }
        }
        super.onSyncedDataUpdated(pKey);
    }

    public void attack() {
        increaseAniTick();
        if (this.aniTickEquals(10)) {
            MobUtils.areaAttack(this, 3F, 3F, 90F,
                    ((float)this.getAttributeValue(Attributes.ATTACK_DAMAGE)) * (MathSupport.random.nextFloat() + 0.2F),
                    0.05F, 5, this.damageSources().mobAttack(this), false, e -> {
                        EntityEventHandler.broadcastEntityEvent(e, 4);
                    }, false);
        }
        if (this.aniTick(20)) {
            this.resetState();
        }
    }

    public int getFlag() {
        return this.entityData.get(DATA_FLAGS);
    }

    public void setFlag(int i) {
        this.entityData.set(DATA_FLAGS, i);
    }

    public int getAniTick() {
        return this.entityData.get(DATA_ANI_TICK);
    }

    public void setAniTick(int aniTick) {
        this.entityData.set(DATA_ANI_TICK, aniTick);
    }

    public void setTarget(@Nullable LivingEntity pTarget) {
        super.setTarget(pTarget);
        if (pTarget != null) {
            pTarget.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 200, 0));
            pTarget.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 0));
            pTarget.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 200, 0));
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createPathAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ARMOR, 6.0D);
    }

    static {
        DATA_FLAGS = SynchedEntityData.defineId(TheGhost.class, EntityDataSerializers.INT);
        DATA_ANI_TICK = SynchedEntityData.defineId(TheGhost.class, EntityDataSerializers.INT);
    }
}
