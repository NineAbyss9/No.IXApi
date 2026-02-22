
package com.bilibili.player_ix.noixmod_api.entities.monster;

import com.github.NineAbyss9.ix_api.api.mobs.ApiRangedAttackMob;
import com.github.NineAbyss9.ix_api.api.mobs.ApiVillager;
import com.github.NineAbyss9.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiRangedBowAttackGoal;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.APISpellcaster;
import com.bilibili.player_ix.noixmod_api.entities.projectile.arrow.VampireArrow;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIMobEffects;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import com.bilibili.player_ix.noixmod_api.util.OwnerSummon;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import org.jetbrains.annotations.Nullable;

public class Vampire
extends APISpellcaster
implements ApiRangedAttackMob {
    public final OwnerSummon ownerSummon = new OwnerSummon(this);
    public Vampire(EntityType<? extends Vampire> p_32105_, Level p_32106_) {
        super(p_32105_, p_32106_);
        this.xpReward = 3;
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new CastingSpellGoal());
        this.goalSelector.addGoal(1, new ApiRangedBowAttackGoal(this, 0.75,
                10, 20F));
        this.goalSelector.addGoal(2, new VampireSpellGoal());
        this.goalSelector.addGoal(2, new BlindnessSpellGoal());
        this.goalSelector.addGoal(3, new FloatGoal(this));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, LivingEntity.class, 10f));
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, Raider.class).setAlertOthers());
        this.targetSelector.addGoal(2, new MobUtils.HostileNearestAttackableTargetGoal(this, false));
    }

    public OwnerSummon getSummon() {
        return this.ownerSummon;
    }

    public void performRangedAttack(LivingEntity livingEntity, float v) {
        double x = this.getSummon().projectileDouble(livingEntity)[0];
        double y = this.getSummon().projectileDouble(livingEntity)[1];
        double z = this.getSummon().projectileDouble(livingEntity)[2];
        ItemStack stack = this.getProjectile(this.getItemInHand(ProjectileUtil
                .getWeaponHoldingHand(this, item -> item instanceof BowItem)));
        AbstractArrow arrow = this.getArrow(stack, v);
        if (this.getMainHandItem().getItem() instanceof BowItem bow) {
            arrow = bow.customArrow(arrow);
        }
        arrow.shoot(x, y, z, 1.6f, 14 - this.level().getDifficulty().getId() * 4);
        this.playSound(SoundEvents.SKELETON_SHOOT);
        this.level().addFreshEntity(arrow);
    }

    public AbstractArrow getArrow(ItemStack stack, float pDistanceFactor) {
        VampireArrow arrow = new VampireArrow(this.level(), this);
        arrow.setEffectsFromItem(stack);
        arrow.setEnchantmentEffectsFromEntity(this, pDistanceFactor);
        arrow.setOwner(this);
        if (this.isOnFire()) {
            arrow.setSecondsOnFire(4);
        }
        return arrow;
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    @Override
    public IllagerArmPose getArmPose() {
        if (this.isCastingSpell()) {
            return IllagerArmPose.SPELLCASTING;
        }
        if (this.isAggressive()) {
            return IllagerArmPose.BOW_AND_ARROW;
        }
        return IllagerArmPose.CROSSED;
    }

    @Override
    public void aiStep() {
        MobUtils.burnInTheSun(NoixmodAPIMainConfig.VampireBurnUnderSun.get(), this, 3);
        super.aiStep();
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ILLUSIONER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_33034_) {
        return SoundEvents.ILLUSIONER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ILLUSIONER_DEATH;
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return SoundEvents.PILLAGER_CELEBRATE;
    }

    public static void init() {
        MobUtils.registerSpawn(NoixmodAPIEntities.VAMPIRE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (entityType, world, reason, pos, random) -> (world.getDifficulty() != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(world, pos, random) && Mob.checkMobSpawnRules(entityType, world, reason, pos, random) && NoixmodAPIMainConfig.VampireWillSpawn.get()));
    }

    private class VampireSpellGoal
    extends UseSpellGoal {
        LivingEntity living = Vampire.this.getTarget();

        @Override
        protected void castSpell() {
            if (living != null) {
                living.addEffect(new MobEffectInstance(NoixmodAPIMobEffects.VAMPIRE.get(), Maths.toTick(3), 1));
            }
        }

        @Override
        protected int getCastingTime() {
            return 30;
        }

        @Override
        protected int getCastingInterval() {
            return 500;
        }

        @Override
        public boolean canUse() {
            if (living != null) {
                if (living.getHealth() <= 10) {
                    return false;
                }
            }
            if (!(Vampire.this.level().getDifficulty() == Difficulty.HARD)) {
                return false;
            }
            return super.canUse() && Vampire.this.random.nextFloat() <= 0.1f;
        }

        @Nullable
        @Override
        protected SoundEvent getPrepareSound() {
            return SoundEvents.BELL_RESONATE;
        }

        @Override
        protected IllagerSpellType getSpellType() {
            return IllagerSpellType.DARK;
        }
    }

    private class BlindnessSpellGoal
    extends UseSpellGoal {
        LivingEntity living = Vampire.this.getTarget();

        @Override
        protected void castSpell() {
            if (living != null) {
                living.addEffect(new MobEffectInstance(MobEffects.BLINDNESS));
            }
        }

        @Override
        protected int getCastingTime() {
            return 30;
        }

        @Override
        protected int getCastingInterval() {
            return 400;
        }

        @Nullable
        @Override
        protected SoundEvent getPrepareSound() {
            return SoundEvents.ILLUSIONER_PREPARE_BLINDNESS;
        }

        @Override
        protected IllagerSpellType getSpellType() {
            return IllagerSpellType.DARK;
        }

        @Override
        public boolean canUse() {
            if (!(living instanceof AbstractGolem || living instanceof Player || living instanceof ApiVillager)) {
                return false;
            }
            return super.canUse();
        }
    }
}
