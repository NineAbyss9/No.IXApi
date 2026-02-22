
package com.bilibili.player_ix.noixmod_api.entities.servant.nihilistic;

import com.github.NineAbyss9.ix_api.api.APISpells;
import com.github.NineAbyss9.ix_api.api.mobs.ApiMobType;
import com.github.NineAbyss9.ix_api.api.mobs.OwnableMob;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiMeleeAttackGoal;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.Nullable;

public class CursedNihilisticEvoker
extends OwnableNihilist {
    public CursedNihilisticEvoker(EntityType<? extends CursedNihilisticEvoker> type, Level level) {
        super(type, level);
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.IRON_SWORD));
    }

    public CursedNihilisticEvoker(PlayMessages.SpawnEntity packet, Level world) {
        this(NoixmodAPIEntities.CURSED_NIHILISTIC_EVOKER.get(), world);
        packet.getEntity();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return CursedNihilisticEvoker.createPathAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.5)
                .add(Attributes.MAX_HEALTH, 100)
                .add(Attributes.ARMOR, 2)
                .add(Attributes.ATTACK_DAMAGE, 5)
                .add(Attributes.FOLLOW_RANGE, 128).add(Attributes.KNOCKBACK_RESISTANCE,
                        0.85).add(Attributes.ATTACK_KNOCKBACK, 1);
    }

    public MobType getMobType() {
        return ApiMobType.NIHILISTIC_UNDEAD;
    }

    public boolean fireImmune() {
        return true;
    }

    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            for(int $$0 = 0; $$0 < 2; ++$$0) {
                this.level().addParticle(NoixmodAPIParticleTypes.DARK_SPELL.get(),
                        this.getRandomX(0.5), this.getRandomY(), this.getRandomZ(0.5),
                        0.0, 0.0, 0.0);
            }
        }
        if (this.getOwner() != null && this.getOwner() instanceof Mob mob && mob.getTarget() != null) {
            this.setTarget(mob.getTarget());
        }
    }

    protected SpellCastType getSpellCastType() {
        return SpellCastType.CULTIST;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.EVOKER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_33034_) {
        return SoundEvents.EVOKER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.EVOKER_DEATH;
    }

    public boolean addEffect(MobEffectInstance p_147208_, @Nullable Entity p_147209_) {
        return false;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new CastingSpellGoal());
        this.goalSelector.addGoal(1, new SummonSpellGoal());
        this.goalSelector.addGoal(2, new ApiMeleeAttackGoal(this, 0.5, false, false));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, LivingEntity.class, 10f));
        this.targetSelector.addGoal(0, new OwnableMob.OwnableTargetGoal<>(this, false));
        this.targetSelector.addGoal(1, new OwnableMob.OwnerHurtTargetGoal<>(this));
        this.targetSelector.addGoal(5, new HurtByTargetGoal(this, Raider.class));
    }

    public void die(DamageSource p_21014_) {
        super.die(p_21014_);
    }

    private class SummonSpellGoal extends UseSpellGoalA {
        @Override
        protected int getCastingInterval() {
            return 480;
        }

        @Override
        protected APISpells.APISpell getSpell() {
            return APISpells.APISpell.NIHILISTIC;
        }

        @Override
        protected int getCastingTime() {
            return 60;
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_SUMMON;
        }

        @Override
        protected void castSpell() {
            CursedNihilisticEvoker c = CursedNihilisticEvoker.this;
            if (c.level() instanceof ServerLevel $$0) for (int $$1 = 0; $$1 < 3; ++$$1) {
                BlockPos $$2 = c.blockPosition().offset(-2 + c.random.nextInt(5), 1, -2 + c.random.nextInt(5));
                NihilisticZombie $$3 = new NihilisticZombie(NoixmodAPIEntities.NIHILISTIC_ZOMBIE.get(), $$0);
                $$3.moveTo($$2, 0.0f, 0.0f);
                $$3.finalizeSpawn($$0, c.level().getCurrentDifficultyAt($$2), MobSpawnType.MOB_SUMMONED, null, null);
                $$3.setOwner(c);
                $$0.addFreshEntity($$3);
            }
        }
    }
}
