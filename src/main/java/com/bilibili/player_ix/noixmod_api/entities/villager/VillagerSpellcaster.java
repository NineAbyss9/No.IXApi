
package com.bilibili.player_ix.noixmod_api.entities.villager;

import com.github.NineAbyss9.ix_api.api.ApiSpells;
import com.github.NineAbyss9.ix_api.api.mobs.ApiPathfinderMob;
import com.github.NineAbyss9.ix_api.api.mobs.ApiVillager;
import com.bilibili.player_ix.noixmod_api.entities.projectile.VillagerFangs;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.util.ProjectileUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

import java.util.List;

public class VillagerSpellcaster
extends VillagerFighter
implements RangedAttackMob {
    public VillagerSpellcaster(EntityType<? extends VillagerSpellcaster> $$0, Level $$1) {
        super($$0, $$1);
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
    }

    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    protected SoundEvent getCastingSoundEvent() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new CastingSpellGoal());
        this.goalSelector.addGoal(1, new RegenSpellGoal());
        this.goalSelector.addGoal(1, new AttackSpellGoal());
        this.goalSelector.addGoal(2, new BSpellGoal());
        this.goalSelector.addGoal(3, new RangedBowAttackGoal<>(this, 0.5, 30,
                20.0f));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(3, new VillagerFighterHurtByTargetGoal(this));
    }

    public void performRangedAttack(LivingEntity $$0, float $$1) {
        ItemStack $$2 = this.getProjectile(this.getItemInHand(ProjectileUtils.getWeaponHoldingHand(this, Items.BOW)));
        AbstractArrow $$3 = ProjectileUtil.getMobArrow(this, $$2, $$1);
        double $$4 = $$0.getX() - this.getX();
        double $$5 = $$0.getY((double)1 / 3) - $$3.getY();
        double $$6 = $$0.getZ() - this.getZ();
        double $$7 = Math.sqrt($$4 * $$4 + $$6 * $$6);
        if ($$3 instanceof Arrow arrow) {
            arrow.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 0));
            arrow.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 0));
        }
        $$3.shoot($$4, $$5 + $$7 * (double) 0.2f, $$6, 1.6f, 14 -
                this.level().getDifficulty().getId() * 4);
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0f, 1.0f / (this.getRandom().nextFloat()
                * 0.4f + 0.8f));
        this.level().addFreshEntity($$3);
    }

    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return new VillagerSpellcaster(NoixmodAPIEntities.VILLAGER_SPELLCASTER.get(), serverLevel);
    }

    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return ApiPathfinderMob.createPathAttributes().add(Attributes.ARMOR, 8)
                .add(Attributes.FOLLOW_RANGE, 72)
                .add(Attributes.MOVEMENT_SPEED, 0.5).add(Attributes.MAX_HEALTH, 50)
                .add(Attributes.ATTACK_DAMAGE, 5)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5);
    }

    private class AttackSpellGoal extends UseSpellGoal {
        public AttackSpellGoal() {
        }

        protected int getCastingInterval() {
            return 300;
        }

        protected ApiSpells.ApiSpell getSpell() {
            return ApiSpells.ApiSpell.ATTACK;
        }

        protected int getCastingTime() {
            return 40;
        }

        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_ATTACK;
        }

        private void strongKnockback(LivingEntity $$0) {
            double x = VillagerSpellcaster.this.getX();
            double y = VillagerSpellcaster.this.getY();
            double z = VillagerSpellcaster.this.getZ();
            $$0.push(x / z * 0, 0.5, y / z * 0);
        }

        protected void performSpellCasting() {
            List<LivingEntity> $$0 = VillagerSpellcaster.this.level().getEntitiesOfClass(LivingEntity.class,
                    VillagerSpellcaster.this.getBoundingBox().inflate(8.0), living ->
                            VillagerFangs.canDamage(living, VillagerSpellcaster.this));
            for (LivingEntity livingEntity : $$0) {
                if (!(livingEntity instanceof ApiVillager)) {
                    livingEntity.hurt(VillagerSpellcaster.this.damageSources().magic(), 6.0f);
                    this.strongKnockback(livingEntity);
                }
            }
            ((ServerLevel)VillagerSpellcaster.this.level()).sendParticles(ParticleTypes.LARGE_SMOKE,
                    VillagerSpellcaster.this.getX(), VillagerSpellcaster.this.getY(), VillagerSpellcaster.this.getZ(),
                    50, 1.0, 1.0, 1.0, 0.25);
            VillagerSpellcaster.this.removeAllEffects();
        }

        public boolean canUse() {
            if (isBaby()) {
                return false;
            }
            return super.canUse();
        }
    }

    private class BSpellGoal extends UseSpellGoal {
        BSpellGoal(){
        }

        protected int getCastingInterval() {
            return 600;
        }

        public boolean canUse() {
            if (isBaby()) {
                return false;
            }
            return super.canUse();
        }

        protected ApiSpells.ApiSpell getSpell() {
            return ApiSpells.ApiSpell.DARK;
        }

        protected int getCastingTime() {
            return 20;
        }

        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.ILLUSIONER_PREPARE_BLINDNESS;
        }

        protected void performSpellCasting() {
            assert VillagerSpellcaster.this.getTarget() != null;
            VillagerSpellcaster.this.getTarget().addEffect(new MobEffectInstance(MobEffects.BLINDNESS,
                    200, 0));
            VillagerSpellcaster.this.getTarget().addEffect(new MobEffectInstance(MobEffects.WITHER,
                    200, 1));
        }
    }

    private class RegenSpellGoal extends UseSpellGoal {
        VillagerSpellcaster spellcaster = VillagerSpellcaster.this;
        public RegenSpellGoal() {
        }

        public boolean canUse() {
            if (VillagerSpellcaster.this.getHealth() > VillagerSpellcaster.this.getMaxHealth() - 4) {
                return false;
            }
            if (isBaby()) {
                return false;
            }
            return super.canUse();
        }

        protected void performSpellCasting() {
            spellcaster.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 5));
            spellcaster.heal(4f);
        }

        protected boolean needTarget() {
            return false;
        }

        protected int getCastingTime() {
            return 20;
        }

        protected int getCastingInterval() {
            return 600;
        }

        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.BELL_RESONATE;
        }

        protected ApiSpells.ApiSpell getSpell() {
            return ApiSpells.ApiSpell.REGEN;
        }
    }
}
