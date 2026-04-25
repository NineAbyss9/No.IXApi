
package com.bilibili.player_ix.noixmod_api.entities.servant;

import com.bilibili.player_ix.noixmod_api.config.NoixmodAPIMainConfig;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiMeleeAttackGoal;
import com.bilibili.player_ix.noixmod_api.entities.mod.APIMonster;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.AbstractGhost;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import com.bilibili.player_ix.noixmod_api.util.MobUtils;
import com.github.NineAbyss9.ix_api.api.ApiPose;
import com.github.NineAbyss9.ix_api.api.mobs.OwnableMob;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;

public class GirlGhost
extends AbstractGhost {
    public GirlGhost(EntityType<? extends GirlGhost> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.xpReward = 2;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(2, new FollowOwnerGoal<>(this, 1,
                20F, 5F, false));
        this.goalSelector.addGoal(3, new ApiMeleeAttackGoal(this, 1, false, false));
        OwnableMob.addBehaviorGoals(this, 5, 0.8, 10F, true, true);
        this.targetSelector.addGoal(1, new OwnableHurtByTargetGoal(this, GirlGhost.class)
                .setAlertOthers());
        this.targetSelector.addGoal(1, new OwnerHurtTargetGoal<>(this));
        this.targetSelector.addGoal(1, new OwnableTargetGoal<>(this, false));
        super.registerGoals();
    }

    public void aiStep() {
        super.aiStep();
        MobUtils.burnInTheSun(this.isUnowned(), this, 4);
    }

    public ApiPose getPoses() {
        if (this.isAggressive()) {
            return ApiPose.ATTACKING;
        } else {
            return ApiPose.CROSSED;
        }
    }

    protected void dropAllDeathLoot(DamageSource p_21192_) {
        if (this.isUnowned()) {
            super.dropAllDeathLoot(p_21192_);
        }
    }

    public int getExperienceReward() {
        if (this.isHostile()) {
            return super.getExperienceReward();
        } else {
            return 0;
        }
    }

    protected InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        if (pPlayer.equals(this.getOwner())) {
            if (this.canAccept(pPlayer.getItemInHand(pHand))) {
                this.setArmors(pPlayer.getMainHandItem().copyWithCount(1));
            }
        }
        return super.mobInteract(pPlayer, pHand);
    }

    public boolean canAccept(ItemStack stack) {
        return !stack.isEmpty();
    }

    public void setArmors(ItemStack stack) {
        if (stack.getItem() instanceof SwordItem) {
            this.setItemInHand(InteractionHand.MAIN_HAND, stack);
        } else if (stack.getItem() instanceof ShieldItem) {
            this.setItemInHand(InteractionHand.OFF_HAND, stack);
        } else if (stack.getItem() instanceof ArmorItem item) {
            this.setItemSlot(item.getEquipmentSlot(), stack);
        }
    }

    public boolean isHostile() {
        return super.isHostile() || this.getSpawnType() == MobSpawnType.NATURAL;
    }

    public static void init() {
        MobUtils.registerSpawn(NoixmodAPIEntities.GIRL_GHOST.get(), SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (entityType, serverLevelAccessor, mobSpawnType,
                                                            blockPos, randomSource) ->
                        APIMonster.checkAPIMonsterSpawnRules(entityType, serverLevelAccessor, mobSpawnType, blockPos,
                                randomSource)
                && NoixmodAPIMainConfig.GirlGhostCanSummon.get());
    }
}
