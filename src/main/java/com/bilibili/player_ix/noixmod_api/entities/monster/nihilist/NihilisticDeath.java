
package com.bilibili.player_ix.noixmod_api.entities.monster.nihilist;

import com.github.NineAbyss9.ix_api.api.APISpells;
import com.github.NineAbyss9.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiMeleeAttackGoal;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.SpellcasterNihilist;
import com.bilibili.player_ix.noixmod_api.entities.servant.worm.Worm;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.Nullable;

public class NihilisticDeath
extends SpellcasterNihilist
implements Enemy {
    public NihilisticDeath(EntityType<NihilisticDeath> type, Level world) {
        super(type, world);
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_AXE));
    }

    public NihilisticDeath(PlayMessages.SpawnEntity entity, Level world) {
        this(NoixmodAPIEntities.NIHILISTIC_DEATH.get(), world);
        entity.getEntity();
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new CastingSpellGoal());
        this.goalSelector.addGoal(1, new NihilisticDeathSummonSpellGoal());
        this.goalSelector.addGoal(2, new ApiMeleeAttackGoal(this, 1, true, false));
        this.goalSelector.addGoal(3, new FloatGoal(this));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, LivingEntity.class, 10f));
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 0.7f));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new MobUtils.HostileNearestAttackableTargetGoal(this, false));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    @Override
    public boolean doHurtTarget(Entity p_21372_) {
        this.heal(1f);
        return super.doHurtTarget(p_21372_);
    }

    @Override
    public boolean killedEntity(ServerLevel p_216988_, LivingEntity p_216989_) {
        this.heal(5f);
        return super.killedEntity(p_216988_, p_216989_);
    }

    public void summonWorm() {
        if (this.level() instanceof ServerLevel level) {
            Worm worm = new Worm(NoixmodAPIEntities.WORM.get(), level);
            worm.moveTo(NihilisticDeath.this.blockPosition().offset(Maths.randomInt(3), 0, Maths.randomInt(3)), 0, 0);
            worm.setOwner(NihilisticDeath.this);
            worm.finalizeSpawn(level, level.getCurrentDifficultyAt(NihilisticDeath.this.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
            level.addFreshEntity(worm);
        }
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.VINDICATOR_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource p_21239_) {
        return SoundEvents.VINDICATOR_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.VINDICATOR_DEATH;
    }

    @Nullable
    @Override
    protected SoundEvent getCastingSoundEvent() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return NihilisticDeath.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.35)
                .add(Attributes.FOLLOW_RANGE, 64).add(Attributes.ATTACK_DAMAGE, 6)
                .add(Attributes.ARMOR, 4).add(Attributes.KNOCKBACK_RESISTANCE, 0.35)
                .add(Attributes.ATTACK_KNOCKBACK, 1).add(Attributes.ARMOR_TOUGHNESS, 2)
                .add(Attributes.MAX_HEALTH, 50);
    }

    public static void init() {
        MobUtils.registerSpawn(NoixmodAPIEntities.NIHILISTIC_DEATH.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (entityType, world, reason, pos, random) -> (world.getDifficulty() != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(world, pos, random) && Mob.checkMobSpawnRules(entityType, world, reason, pos, random)));
        // DungeonHooks.addDungeonMob(NoixmodAPIEntities.NIHILISTIC_DEATH.get(), 180);
    }

    private class NihilisticDeathSummonSpellGoal
    extends UseSpellGoalA {

        @Override
        protected void castSpell() {
            for (int i = 0; i < 4; ++i) {
                NihilisticDeath.this.summonWorm();
            }
        }

        @Override
        protected int getCastingTime() {
            return 20;
        }

        @Override
        protected int getCastingInterval() {
            return 400;
        }

        @Nullable
        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_SUMMON;
        }

        @Override
        protected APISpells.APISpell getSpell() {
            return APISpells.APISpell.FIRE;
        }
    }
}
