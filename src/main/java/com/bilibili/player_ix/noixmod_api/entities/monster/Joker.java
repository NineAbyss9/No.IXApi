
package com.bilibili.player_ix.noixmod_api.entities.monster;

import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.APISpellcaster;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class Joker extends APISpellcaster {
    public Joker(EntityType<? extends Joker> p_32105_, Level p_32106_) {
        super(p_32105_, p_32106_);
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BOW));
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
    }

    @Override
    public int getExperienceReward() {
        return 15;
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return SoundEvents.EVOKER_CELEBRATE;
    }


}
