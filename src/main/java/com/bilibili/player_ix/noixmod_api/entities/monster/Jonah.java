
package com.bilibili.player_ix.noixmod_api.entities.monster;

import com.github.NineAbyss9.ix_api.ix_api.api.mobs.ApiMobType;
import com.bilibili.player_ix.noixmod_api.entities.mod.APIMonster;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class Jonah extends APIMonster {
    public Jonah(EntityType<? extends Jonah> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.xpReward = 10;
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.IRON_AXE));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
    }

    @Override
    public void aiStep() {
        super.aiStep();
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public MobType getMobType() {
        return ApiMobType.NIHILISTIC;
    }
}
