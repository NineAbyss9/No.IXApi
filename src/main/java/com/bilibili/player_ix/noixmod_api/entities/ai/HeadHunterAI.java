
package com.bilibili.player_ix.noixmod_api.entities.ai;

import com.bilibili.player_ix.noixmod_api.entities.boss.HeadHunter;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class HeadHunterAI extends AbstractAI {
    private final HeadHunter hunter;
    public HeadHunterAI(HeadHunter finder) {
        super(finder);
        this.hunter = finder;
    }

    public boolean needTeleport() {
        return false;
    }

    public boolean isInDanger(@Nullable LivingEntity target) {
        return false;
    }

    public boolean canUpdate() {
        return !this.hunter.isRemoved();
    }

    public void update() {
    }
}
