
package com.bilibili.player_ix.noixmod_api.entities.monster.horror;

import com.github.NineAbyss9.ix_api.api.ApiPose;
import com.github.NineAbyss9.ix_api.api.mobs.ApiPoseMob;
import com.bilibili.player_ix.noixmod_api.entities.monster.abstract_monster.AbstractHorrorMob;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

//the zombie
public class ScaringZombie
extends AbstractHorrorMob
implements ApiPoseMob {
    public ScaringZombie(EntityType<? extends ScaringZombie> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    public ApiPose getPoses() {
        if (isAggressive())
            return ApiPose.ZOMBIE_ATTACKING;
        return ApiPose.NATURAL;
    }

    protected void playStepSound(BlockPos pPos, BlockState pState) {
    }

    public Component getDisplayName() {
        return getName();
    }

    public Component getName() {
        return Component.translatable("entity.minecraft.zombie");
    }

    public float getVoicePitch() {
        return 0.01F;
    }
}
