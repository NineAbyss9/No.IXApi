
package com.bilibili.player_ix.noixmod_api.api.entity;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

public interface IVex {
    default SoundEvent getChargeSound() {
        return SoundEvents.VEX_CHARGE;
    }
}
