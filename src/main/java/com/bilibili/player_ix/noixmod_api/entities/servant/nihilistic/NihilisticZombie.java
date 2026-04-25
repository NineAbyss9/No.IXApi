
package com.bilibili.player_ix.noixmod_api.entities.servant.nihilistic;

import com.github.NineAbyss9.ix_api.api.mobs.ApiMobType;
import com.github.NineAbyss9.ix_api.api.mobs.Nihilistic;
import com.github.NineAbyss9.ix_api.api.mobs.Ownable;
import com.github.NineAbyss9.ix_api.api.mobs.OwnableMob;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiOwnerTargetGoal;
import com.bilibili.player_ix.noixmod_api.entities.servant.core.AbstractZombieServant;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.UUID;

public class NihilisticZombie
extends AbstractZombieServant
implements Ownable,
        Nihilistic {
    @Nullable
    protected LivingEntity owner;
    @Nullable
    protected UUID ownerUUID;
    public boolean canAttack(LivingEntity $$0) {
        if (!MobUtils.canHurt($$0, this)) {
            return false;
        }
        return super.canAttack($$0);
    }

    public MobType getMobType() {
        return ApiMobType.NIHILISTIC_UNDEAD;
    }

    public void tick() {
        super.tick();
    }

    public @Nullable ParticleOptions getAmbientParticle() {
        return null;
    }

    public boolean fireImmune() {
        return true;
    }

    public NihilisticZombie(@NotNull EntityType<? extends NihilisticZombie> nihilityZombieEntityType, Level world) {
        super(nihilityZombieEntityType, world);
        this.setMaxUpStep(1f);
        this.xpReward = 15;
        this.setNoAi(false);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new AttackGoal(this, 1.0));
        this.targetSelector.addGoal(0, new ApiOwnerTargetGoal(this));
        this.targetSelector.addGoal(0, new OwnableMob.OwnerHurtTargetGoal<>(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, NihilisticZombie.class));
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        return super.hurt(pSource, pAmount);
    }

    public boolean isHostile() {
        return true;
    }

    public boolean addEffect(MobEffectInstance p_147208_, @Nullable Entity p_147209_) {
        return false;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return NihilisticZombie.createPathAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.ARMOR, 2)
                .add(Attributes.ATTACK_DAMAGE, 5)
                .add(Attributes.FOLLOW_RANGE, 128)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.35)
                .add(Attributes.ATTACK_KNOCKBACK, 1)
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
    }
}
