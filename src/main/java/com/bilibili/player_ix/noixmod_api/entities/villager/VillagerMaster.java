
package com.bilibili.player_ix.noixmod_api.entities.villager;

import com.github.NineAbyss9.ix_api.api.APISpells;
import com.github.NineAbyss9.ix_api.api.mobs.ApiPathfinderMob;
import com.github.NineAbyss9.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiMeleeAttackGoal;
import com.bilibili.player_ix.noixmod_api.entities.villager.trades.ApiVillagerTrades;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class VillagerMaster
extends VillagerFighter {
    public VillagerMaster(EntityType<? extends VillagerMaster> type, Level world) {
        super(type, world);
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_AXE));
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new CastingSpellGoal());
        this.goalSelector.addGoal(1, new FighterHealSpellGoal(10));
        this.goalSelector.addGoal(1, new BuffSpellGoal());
        this.goalSelector.addGoal(2, new ApiMeleeAttackGoal(this, 1, Maths.square(2)));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, LivingEntity.class, 10f));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 0.5));
        this.goalSelector.addGoal(4, new FloatGoal(this));
        this.targetSelector.addGoal(1, new VillagerFighterHurtByTargetGoal(this,
                VillagerFighter.class).setAlertOthers(VillagerFighter.class));
    }

    public VillagerFighterArmPose getArmPose() {
        if (this.isCastingSpell()) {
            return VillagerFighterArmPose.SPELL_CASTING;
        }
        if (this.isAggressive()) {
            return VillagerFighterArmPose.ATTACKING;
        }
        return VillagerFighterArmPose.CROSSED;
    }

    protected VillagerTrades.ItemListing[] getTradeLists() {
        return ApiVillagerTrades.MASTER_TRADES;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return ApiPathfinderMob.createPathAttributes().add(Attributes.ATTACK_DAMAGE, 5).
                add(Attributes.MAX_HEALTH, 120).add(Attributes.KNOCKBACK_RESISTANCE, 0.75)
                .add(Attributes.ARMOR, 10).add(Attributes.MOVEMENT_SPEED, 0.35)
                .add(Attributes.FOLLOW_RANGE, 90);
    }

    protected SoundEvent getCastingSoundEvent() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    public boolean doHurtTarget(Entity p_21372_) {
        this.heal(NoixmodAPIMainConfig.VillagerMasterHealAmount.get().floatValue());
        return super.doHurtTarget(p_21372_);
    }

    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return new VillagerMaster(NoixmodAPIEntities.VILLAGER_MASTER.get(), serverLevel);
    }

    protected class BuffSpellGoal
    extends UseSpellGoal {
        protected void performSpellCasting() {
            VillagerMaster.this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED,
                    Maths.toTick(20), 1));
            VillagerMaster.this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST,
                    Maths.toTick(20), 1));
        }

        protected int getCastingTime() {
            return 20;
        }

        protected int getCastingInterval() {
            return 600;
        }

        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EMPTY;
        }

        protected APISpells.APISpell getSpell() {
            return APISpells.APISpell.ATTACK;
        }
    }
}
