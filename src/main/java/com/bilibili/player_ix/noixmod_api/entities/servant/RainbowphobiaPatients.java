
package com.bilibili.player_ix.noixmod_api.entities.servant;

import com.github.NineAbyss9.ix_api.api.mobs.NihilitySummonedMobs;
import com.bilibili.player_ix.noixmod_api.entities.ai.goal.ApiMeleeAttackGoal;
import com.bilibili.player_ix.noixmod_api.register.NoixmodAPIEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PlayMessages;

public class RainbowphobiaPatients
extends NihilitySummonedMobs {
    public RainbowphobiaPatients(EntityType<? extends RainbowphobiaPatients> e, Level l) {
        super(e, l);
    }

    public RainbowphobiaPatients(PlayMessages.SpawnEntity packet, Level world) {
        this(NoixmodAPIEntities.RAINBOWPHOBIA_PATIENTS.get(), world);
        packet.getEntity();
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new ApiMeleeAttackGoal(this, 1, false, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this,
                Player.class, false));
        this.targetSelector.addGoal(4, new HurtByTargetGoal(this, Raider.class));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this,
                Animal.class, false));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this,
                AbstractGolem.class, false));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this,
                AbstractVillager.class, false));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMobAttributes().add(Attributes.ARMOR, 2)
                .add(Attributes.MAX_HEALTH, 30)
                .add(Attributes.FOLLOW_RANGE, 100)
                .add(Attributes.ATTACK_DAMAGE, 7)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.75)
                .add(Attributes.ATTACK_KNOCKBACK, 1)
                .add(Attributes.MOVEMENT_SPEED, 0.3).add(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
    }
}
