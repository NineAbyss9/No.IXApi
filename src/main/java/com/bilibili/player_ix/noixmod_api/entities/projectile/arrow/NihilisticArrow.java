
package com.bilibili.player_ix.noixmod_api.entities.projectile.arrow;

import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIDamageSource;
import com.github.NineAbyss9.ix_api.api.mobs.Nihilistic;
import com.github.NineAbyss9.ix_api.util.Maths;
import com.github.NineAbyss9.ix_api.util.Vec9;
import com.bilibili.player_ix.noixmod_api.api.entity.IX;
import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import com.bilibili.player_ix.noixmod_api.entities.boss.Apostle;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIMobEffects;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIParticleTypes;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class NihilisticArrow
extends Arrow
implements Nihilistic {
    private int lifeTicks = Maths.toTick(9);
    private boolean discardOnGround = false;
    @Nullable
    private LivingEntity target;
    public NihilisticArrow(EntityType<? extends NihilisticArrow> entityType, Level p_36722_) {
        super(entityType, p_36722_);
    }

    public NihilisticArrow(Level world, LivingEntity lie) {
        super(world, lie);
    }

    public EntityType<?> getType() {
        return NoixmodAPIEntities.NIHILISTIC_ARROW.get();
    }

    public void tick() {
        if (this.lifeTicks == 310 && this.target != null) {
            this.setDeltaMovement(Vec9.moveToVec(this, this.target, 0.2));
            this.updateRotation();
        }
        super.tick();
        --this.lifeTicks;
        if (this.level().isClientSide) {
            this.level().addParticle(NoixmodAPIParticleTypes.DARK_SPELL.get(), this.getRandomX(0.5),
                    this.getY(), this.getRandomZ(0.5), 0, 0, 0);
        }
        if (this.lifeTicks <= 0) {
            this.makeHurt();
        }
    }

    public void setFlag(boolean flag) {
        this.discardOnGround = flag;
    }

    protected ItemStack getPickupItem() {
        return new ItemStack(Items.ARROW);
    }

    public void makeHurt() {
        MobUtils.rangeHurt(2, 2, 2, this, NoixmodAPIDamageSource.nihilityOwner(this), 6);
        if (!level().isClientSide) {
            ServerLevel level = (ServerLevel)level();
            level.sendParticles(NoixmodAPIParticleTypes.DARK_SPELL.get(), this.getX(), this.getY(), this.getZ(),
                    15, 0, 0, 0, 0.25);
        }
        this.discard();
    }

    public Vec3 getDeltaMovement() {
        return isFloating() ? Vec3.ZERO : super.getDeltaMovement();
    }

    public boolean isFloating() {
        return this.lifeTicks > 310;
    }

    public void setFloating(@Nullable LivingEntity pTarget) {
        this.target = pTarget;
        this.lifeTicks = Maths.toTick(18);
    }

    protected boolean canHitEntity(Entity pEntity) {
        if (pEntity instanceof LivingEntity entity && !MobUtils.canHurt(entity, this)) {
            return false;
        }
        return super.canHitEntity(pEntity);
    }

    protected void onHitBlock(BlockHitResult p_36755_) {
        super.onHitBlock(p_36755_);
        if (this.discardOnGround) {
            this.makeHurt();
        }
    }

    protected void doPostHurtEffects(LivingEntity p_36873_) {
        super.doPostHurtEffects(p_36873_);
        if (this.getOwner() != null && this.getOwner() instanceof LivingEntity living) {
            if (living instanceof Apostle apostle) {
                if (apostle.isSecondPhase()) apostle.healSelf(1f);
            } else {
                living.heal(1f);
            }
        }
    }

    protected void onHitEntity(EntityHitResult p_36757_) {
        if (!level().isClientSide) {
            ServerLevel level = (ServerLevel)this.level();
            double d = this.getX();
            double d1 = this.getY();
            double d2 = this.getZ();
            level.sendParticles(NoixmodAPIParticleTypes.DARK_SPELL.get(), d, d1, d2, 32, 0.25,
                    0.25, 0.25, 0.25);
            if (p_36757_.getEntity() instanceof LivingEntity lie && !(lie instanceof IX)) {
                if (MobUtils.canHurt(lie, this)) {
                    if (NoixmodAPIMainConfig.HorrorMode.get()) {
                        if (lie.isAlive()) {
                            lie.setHealth(lie.getHealth() - 12.5f);
                        }
                    }
                    lie.addEffect(new MobEffectInstance(NoixmodAPIMobEffects.NIHILISTIC.get(),
                            40, 0), lie);
                    super.onHitEntity(p_36757_);
                }
            }
        }
    }

    public boolean killedEntity(ServerLevel level, LivingEntity lie) {
        if (this.getOwner() != null && this.getOwner() instanceof LivingEntity living) {
            if (this.getOwner() instanceof Apostle apostle) {
                if (apostle.isSecondPhase()) apostle.heal(1f);
                else return super.killedEntity(level, lie);
            }
            else {
                living.heal(1);
            }
        }
        return super.killedEntity(level, lie);
    }
}
