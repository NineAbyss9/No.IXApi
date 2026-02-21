
package com.bilibili.player_ix.noixmod_api.entities.servant;

import com.github.NineAbyss9.ix_api.ix_api.util.Maths;
import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import com.bilibili.player_ix.noixmod_api.entities.mod.APIMonster;
import com.bilibili.player_ix.noixmod_api.entities.projectile.WindEntity;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIParticleTypes;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;

import javax.annotation.Nullable;

public class WindZombie
extends AbstractZombieServant {
    public WindZombie(EntityType<? extends WindZombie> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
        this.xpReward = 6;
        this.populateDefaultEquipment();
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new AttackGoal(this, 1));
        this.addTargetGoal();
    }

    public void aiStep() {
        super.aiStep();
        if (this.tickCount % 100 == 0) {
            this.summonWind();
        }
    }

    protected boolean shouldDropLoot() {
        return this.isHostile();
    }

    public boolean isInvulnerableTo(DamageSource pSource) {
        if (pSource.is(DamageTypeTags.IS_FALL)) {
            return true;
        }
        return super.isInvulnerableTo(pSource);
    }

    private void summonWind() {
        if (!this.level().isClientSide) {
            ServerLevel serverLevel = this.serverLevel();
            WindEntity windEntity = new WindEntity(NoixmodAPIEntities.WIND_ENTITY.get(), serverLevel);
            windEntity.moveTo(this.position().add(Maths.randomInt(1), 1, Maths.randomInt(1)));
            windEntity.setOwner(this);
            serverLevel.addFreshEntity(windEntity);
        }
    }

    protected void populateDefaultEquipmentEnchantments(RandomSource pRandom, DifficultyInstance pDifficulty) {
    }

    @Nullable
    public ParticleOptions getAmbientParticle() {
        return NoixmodAPIParticleTypes.WIND.get();
    }

    public boolean shouldBurn() {
        return false;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createPathAttributes().add(Attributes.MOVEMENT_SPEED, 0.2500012312)
                .add(Attributes.FOLLOW_RANGE, 42).add(Attributes.MAX_HEALTH, 26)
                .add(Attributes.ARMOR, 4).add(Attributes.ATTACK_DAMAGE, 4);
    }

    public static void init() {
        MobUtils.registerSpawn(NoixmodAPIEntities.WIND_ZOMBIE.get(), SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (entityType, serverLevelAccessor, mobSpawnType,
                                                            blockPos, randomSource) ->
                        APIMonster.checkAPIMonsterSpawnRules(entityType, serverLevelAccessor, mobSpawnType, blockPos,
                                randomSource)
                                && NoixmodAPIMainConfig.WindZombieCanSpawn.get());
    }
}
