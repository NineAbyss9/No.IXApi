
package com.bilibili.player_ix.noixmod_api.entities.villager;

import com.bilibili.player_ix.noixmod_api.entities.villager.trades.ApiVillagerTrades;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class VillagerDoctor
extends VillagerFighter {
    public VillagerDoctor(EntityType<? extends VillagerDoctor> type, Level level) {
        super(type, level);
    }

    protected void registerGoals() {
        super.registerGoals();
    }

    protected SoundEvent getCastingSoundEvent() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    @Nullable
    protected VillagerTrades.ItemListing[] getTradeLists() {
        return ApiVillagerTrades.DOCTOR_TRADES;
    }

    @Nullable
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }
}
