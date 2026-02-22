
package com.bilibili.player_ix.noixmod_api.entities.monster.illager;

import com.github.NineAbyss9.ix_api.api.item.ItemStacks;
import com.github.NineAbyss9.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiMeleeAttackGoal;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.APISpellcaster;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class Abomination
extends APISpellcaster {
    public Abomination(EntityType<? extends APISpellcaster> p_32105_, Level p_32106_) {
        super(p_32105_, p_32106_);
        this.setItemInHand(InteractionHand.MAIN_HAND, ItemStacks.of(Items.IRON_SWORD));
        this.setItemInHand(InteractionHand.OFF_HAND, ItemStacks.of(Items.TOTEM_OF_UNDYING));
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new CastingSpellGoal());
        this.goalSelector.addGoal(1, new AttackSpellGoal());
        this.goalSelector.addGoal(2, new ApiMeleeAttackGoal(this, 1.2,
                Maths.square(1.5)));
        this.targetSelector.addGoal(1, new MobUtils.HostileNearestAttackableTargetGoal(this,
                false));
    }

    public SoundEvent getCelebrateSound() {
        return SoundEvents.EVOKER_CELEBRATE;
    }

    public IllagerArmPose getArmPose() {
        if (this.isCastingSpell()) {
            return IllagerArmPose.SPELLCASTING;
        }
        if (this.isAggressive()) {
            return IllagerArmPose.ATTACKING;
        }
        return IllagerArmPose.CROSSED;
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return SoundEvents.EVOKER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_33034_) {
        return SoundEvents.EVOKER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.EVOKER_DEATH;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Abomination.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 56)
                .add(Attributes.MAX_HEALTH, 26).add(Attributes.ATTACK_DAMAGE, 3)
                .add(Attributes.MOVEMENT_SPEED, 0.3);
    }

    private class AttackSpellGoal extends UseSpellGoal {

        protected void castSpell() {
            LivingEntity target = getTarget();
            if (target != null) {
                Level level = level();
                for (int i = 0;i <5;i++) {
                    EvokerFangs fangs = EntityType.EVOKER_FANGS.create(level);
                    if (fangs==null)continue;
                    Vec3 vec3;
                    if (i == 0) {
                        vec3 = Vec3.ZERO;
                    } else if (i == 1) {
                        vec3 = new Vec3(2, 0, 0);
                    } else if (i == 2) {
                        vec3 = new Vec3(0, 0, 2);
                    } else if (i == 3) {
                        vec3 = new Vec3(0, 0, -2);
                    } else {
                        vec3 = new Vec3(-2, 0, 0);
                    }
                    fangs.moveTo(target.position().add(vec3));
                    fangs.setOwner(Abomination.this);
                    level.addFreshEntity(fangs);
                }
            }
        }

        @Override
        protected int getCastingTime() {
            return 30;
        }

        @Override
        protected int getCastingInterval() {
            return 200;
        }

        @Nullable
        @Override
        protected SoundEvent getPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_ATTACK;
        }

        @Override
        protected IllagerSpellType getSpellType() {
            return IllagerSpellType.ATTACK;
        }
    }
}
