
package com.bilibili.player_ix.noixmod_api.entities.monster.horror;

import com.github.NineAbyss9.ix_api.ix_api.api.ApiPose;
import com.github.NineAbyss9.ix_api.ix_api.api.mobs.ApiPoseMob;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiMeleeAttackGoal;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.AbstractHorrorMob;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.level.Level;

//the villager
public class HuntedVillager
extends AbstractHorrorMob
implements ApiPoseMob {
    public HuntedVillager(EntityType<? extends HuntedVillager> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new ApiMeleeAttackGoal(this, 1.0));
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, LivingEntity.class, 30F));
    }

    public Component getDisplayName() {
        return Component.translatable("entity.minecraft.villager");
    }

    public Component getName() {
        return Component.translatable("entity.minecraft.villager");
    }

    public ApiPose getPoses() {
        if (this.isAggressive())
            return ApiPose.ZOMBIE_ATTACKING;
        return ApiPose.NATURAL;
    }

    public float getVoicePitch() {
        return 0.01F;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createPathAttributes().add(Attributes.MAX_HEALTH, 30).add(Attributes.FOLLOW_RANGE, 54)
                .add(Attributes.ATTACK_DAMAGE, 4).add(Attributes.ARMOR, 4)
                .add(Attributes.MOVEMENT_SPEED, 0.3);
    }
}
