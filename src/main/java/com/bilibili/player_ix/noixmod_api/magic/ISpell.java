
package com.bilibili.player_ix.noixmod_api.magic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;

public interface ISpell {
    Spell.Type getSpellType();

    float spellPower();

    void castSpell(ServerLevel pLevel, LivingEntity pCaster);

    @Nullable
    default CompoundTag getTag() {
        return null;
    }
}
