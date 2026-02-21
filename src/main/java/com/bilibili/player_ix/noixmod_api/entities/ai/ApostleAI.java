
package com.bilibili.player_ix.noixmod_api.entities.ai;

import com.bilibili.player_ix.noixmod_api.entities.boss.Apostle;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class ApostleAI extends AbstractAI {
    protected final Apostle apostle;

    public ApostleAI(Apostle pApostle) {
        super(pApostle);
        this.apostle = pApostle;
    }

    @Override
    public boolean needTeleport() {
        return false;
    }

    @Override
    public boolean isInDanger(@Nullable LivingEntity target) {
        return this.apostle.isInDanger();
    }

    @Override
    public boolean isInDanger() {
        return this.apostle.isInDanger();
    }

    @Override
    public boolean canUpdate() {
        if (this.apostle.isRemoved()) {
            return false;
        }
        return this.apostle.isAlive();
    }

    @Override
    public void update() {
    }
}
