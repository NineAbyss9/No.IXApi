
package com.bilibili.player_ix.noixmod_api.entities.servant.worm;

import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiMeleeAttackGoal;
import com.bilibili.player_ix.noixmod_api.entities.mod.APIMonster;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import org.jetbrains.annotations.Nullable;

public class Worm
extends AbstractWorm {
    public Worm(EntityType<? extends AbstractWorm> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
        this.xpReward = 3;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new ApiMeleeAttackGoal(this, 1, false, false));
        this.addBehaviorGoal(3, 0.75, 10f);
        this.goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.75f));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this,
                LivingEntity.class, true, this::canAttack));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(1, new OwnerHurtTargetGoal<>(this));
    }

    public static void init() {
        MobUtils.registerSpawn(NoixmodAPIEntities.WORM.get(), SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (entityType, serverLevelAccessor, mobSpawnType,
                                                            blockPos, randomSource) ->
                randomSource.nextDouble() <= 0.05d && APIMonster.checkAPIMonsterSpawnRules(entityType,
                        serverLevelAccessor, mobSpawnType, blockPos, randomSource) &&
                        NoixmodAPIMainConfig.WormWillSpawn.get());
    }

    public static AttributeSupplier.Builder createBaseAttributes() {
        return Worm.createPathAttributes().add(Attributes.ATTACK_DAMAGE, 5)
                .add(Attributes.ARMOR, 2).add(Attributes.MOVEMENT_SPEED, 0.32);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Worm.createBaseAttributes().add(Attributes.MAX_HEALTH, 16);
    }

    @Nullable
    public AbstractWorm getBreedMob() {
        return new Worm(NoixmodAPIEntities.WORM.get(), this.level());
    }
}
