
package com.bilibili.player_ix.noixmod_api.entities.monster.nihilist;

import com.github.NineAbyss9.ix_api.api.ApiSpells;
import com.github.NineAbyss9.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.Nihilist;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.SpellcasterNihilist;
import com.bilibili.player_ix.noixmod_api.entities.projectile.NihilisticFireball;
import com.bilibili.player_ix.noixmod_api.entities.servant.ZombieVindicator;
import com.bilibili.player_ix.noixmod_api.entities.servant.nihilistic.NihilisticBlaze;
import com.bilibili.player_ix.noixmod_api.entities.servant.nihilistic.NihilisticServant;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIParticleTypes;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPISounds;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import com.bilibili.player_ix.noixmod_api.util.OwnerSummon;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.Nullable;

public class Cultist
extends SpellcasterNihilist
implements Enemy {
    private int fireballTick;
    public final OwnerSummon ownerSummon = new OwnerSummon(this);
    public Cultist(EntityType<Cultist> type, Level level) {
        super(type, level);
        this.xpReward = 10;
    }

    public Cultist(PlayMessages.SpawnEntity entity, Level world) {
        this(NoixmodAPIEntities.CULTIST.get(), world);
        entity.getEntity();
    }

    public void convert() {
        int i = NoixmodAPIMainConfig.CultistConversionInt.get();
        if (this.getTarget() instanceof Villager villager) {
            if (this.level() instanceof ServerLevel level) {
                switch (i) {
                    case 0: {
                        ZombieVindicator vindicator = NoixmodAPIEntities.ZOMBIE_VINDICATOR.get().create(level);
                        if (vindicator != null) {
                            vindicator.moveTo(villager.blockPosition(), villager.yHeadRot, 0);
                            vindicator.setOwner(this);
                            vindicator.finalizeSpawn(level, level.getCurrentDifficultyAt(villager.blockPosition()),
                                    MobSpawnType.CONVERSION);
                            level.addFreshEntity(vindicator);
                        }
                        break;
                    }
                    case 1: {
                        NihilisticServant servant = new NihilisticServant(NoixmodAPIEntities.NIHILISTIC_SERVANT.get(), level);
                        servant.setOwner(this);
                        servant.moveTo(villager.blockPosition(), villager.yHeadRot, 0);
                        servant.finalizeSpawn(level, level.getCurrentDifficultyAt(villager.blockPosition()),
                                MobSpawnType.CONVERSION);
                        level.addFreshEntity(servant);
                        break;
                    }
                    case 2: {
                        NihilisticBlaze blaze = new NihilisticBlaze(NoixmodAPIEntities.NIHILISTIC_BLAZE.get(), level);
                        blaze.moveTo(villager.blockPosition(), villager.yHeadRot, 0);
                        blaze.setOwner(this);
                        blaze.finalizeSpawn(level, level.getCurrentDifficultyAt(villager.blockPosition()),
                                MobSpawnType.CONVERSION);
                        level.addFreshEntity(blaze);
                        break;
                    }
                    default: {
                        throw new NumberFormatException("The number of Cultist is too big or small");
                    }
                }
            }
            villager.discard();
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.fireballTick > 0) {
            this.fireballTick--;
            if (this.fireballTick != 0 && this.fireballTick % 10 == 0) {
                if (this.getTarget() != null) {
                    int i = this.fireballTick / 10 > 2 ? this.fireballTick / 10 : 0;
                    int j = this.fireballTick / 10 <= 2 ? this.fireballTick / 10 : 0;
                    this.summonFireball(i, j, this.getTarget());
                }
            }
        }
    }

    public void summonFireball(int var1, int var2, LivingEntity target) {
        double[] doubles = this.ownerSummon.projectileDouble(target);
        NihilisticFireball ball = new NihilisticFireball(this.level(), this, doubles[0], doubles[1], doubles[2]);
        BlockPos pos = this.blockPosition().offset(var1, 2, var2);
        ball.moveTo(pos.getX(), pos.getY(), pos.getZ());
        this.level().addFreshEntity(ball);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new CastingSpellGoal());
        this.goalSelector.addGoal(1, new AttackGoal());
        this.goalSelector.addGoal(1, new HealSpellGoal());
        this.goalSelector.addGoal(1, new SummonSpellGoal());
        this.goalSelector.addGoal(1, new MissionaryGoal());
        this.goalSelector.addGoal(1, new TeleportSpellGoal());
        this.goalSelector.addGoal(4, new FloatGoal(this));
        this.goalSelector.addGoal(4, new OpenDoorGoal(this, false));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 0.5));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, LivingEntity.class, 10f));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(4, new NihilisticAvoidGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, Nihilist.class).setAlertOthers());
        this.targetSelector.addGoal(2, new MobUtils.HostileNearestAttackableTargetGoal(this, false));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createPathAttributes().add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.MAX_HEALTH, 50).add(Attributes.ATTACK_DAMAGE, 5)
                .add(Attributes.FOLLOW_RANGE, 120).add(Attributes.ARMOR, 0).add(Attributes
                        .KNOCKBACK_RESISTANCE, 0.5);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return NoixmodAPISounds.CULTIST_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_33034_) {
        return NoixmodAPISounds.CULTIST_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return NoixmodAPISounds.CULTIST_DEATH.get();
    }

    @Override
    protected SoundEvent getCastingSoundEvent() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    public void summon() {
        ZombieVindicator zombie = new ZombieVindicator(NoixmodAPIEntities.ZOMBIE_VINDICATOR.get(), this.level());
        this.ownerSummon.integerSummon(zombie, 4);
    }

    private abstract class CultistGoal
    extends UseSpellGoalA {

        @Override
        public boolean canUse() {
            LivingEntity target = Cultist.this.getTarget();
            boolean flag = target instanceof Villager;
            if (this.missionary()) return flag && super.canUse();
             else return !flag && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity target = Cultist.this.getTarget();
            boolean flag = target instanceof Villager;
            if (this.missionary()) return flag && super.canContinueToUse();
             else return !flag && super.canContinueToUse();
        }

        protected boolean missionary() {
            return false;
        }
    }

    private class AttackGoal
    extends CultistGoal {
        public AttackGoal() {
        }

        @Override
        protected void castSpell() {
            fireballTick = 40;
        }

        @Override
        protected int getCastingTime() {
            return 40;
        }

        @Override
        protected int getCastingInterval() {
            return 80;
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_ATTACK;
        }

        @Override
        protected ApiSpells.ApiSpell getSpell() {
            return ApiSpells.ApiSpell.DARK;
        }
    }

    private class MissionaryGoal
    extends CultistGoal {

        @Override
        protected void castSpell() {
            Cultist.this.convert();
        }

        @Override
        protected int getCastingTime() {
            return 40;
        }

        @Override
        protected int getCastingInterval() {
            return 300;
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EMPTY;
        }

        @Override
        protected ApiSpells.ApiSpell getSpell() {
            return ApiSpells.ApiSpell.DARK;
        }

        @Override
        protected boolean missionary() {
            return true;
        }
    }

    private class SummonSpellGoal
    extends CultistGoal {

        @Override
        protected void castSpell() {
            for (int i = 0; i < 3; ++i) {
                Cultist.this.summon();
            }
        }

        @Override
        protected int getCastingTime() {
            return 60;
        }

        @Override
        protected int getCastingInterval() {
            return 500;
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_SUMMON;
        }

        @Override
        protected ApiSpells.ApiSpell getSpell() {
            return ApiSpells.ApiSpell.ZOMBIE;
        }
    }

    private class HealSpellGoal
    extends UseSpellGoalA {

        @Override
        protected void castSpell() {
            Cultist.this.heal(10f);
        }

        @Override
        public boolean canUse() {
            if (Cultist.this.getHealth() == Cultist.this.getMaxHealth()) {
                return false;
            }
            return super.canUse();
        }

        @Override
        protected int getCastingTime() {
            return 50;
        }

        @Override
        protected int getCastingInterval() {
            return 500;
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.BELL_RESONATE;
        }

        @Override
        protected ApiSpells.ApiSpell getSpell() {
            return ApiSpells.ApiSpell.REGEN;
        }
    }

    private class TeleportSpellGoal
    extends CultistGoal {
        @Override
        public boolean canUse() {
            if (Cultist.this.getTarget() != null && Cultist.this.distanceToSqr(Cultist.this.getTarget()) > 6 * 6) {
                return false;
            }
            return super.canUse();
        }

        @Override
        protected void castSpell() {
            if (Cultist.this.level() instanceof ServerLevel level) {
                level.sendParticles(NoixmodAPIParticleTypes.DARK_SPELL.get(), Cultist.this.getX(), Cultist.this.getY() + 0.5, Cultist.this.getZ(), 100, 0, 0, 0, 0.25);
                level.sendParticles(ParticleTypes.LARGE_SMOKE, Cultist.this.getX(), Cultist.this.getY() + 0.5, Cultist.this.getZ(), 100, 0, 0, 0, 0.25);
            }
            MobUtils.rangeHurt(6, 6, 6, Cultist.this, Cultist.this.damageSources().indirectMagic(Cultist.this, Cultist.this), 10f);
            Cultist.this.randomTeleport(Cultist.this.getX() + Maths.randomInteger(4), Cultist.this.getY(), Cultist.this.getZ() + Maths.randomInteger(4), true);
        }

        @Override
        protected int getCastingTime() {
            return 40;
        }

        @Override
        protected int getCastingInterval() {
            return 400;
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.ILLUSIONER_PREPARE_BLINDNESS;
        }

        @Override
        protected ApiSpells.ApiSpell getSpell() {
            return ApiSpells.ApiSpell.DARK;
        }
    }
}
