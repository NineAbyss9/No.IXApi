
package com.github.NineAbyss9.ix_api.api.mobs;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Mob;

public class IntimacyData {
    private final Mob mob;
    private int intimacy;
    public IntimacyData(Mob pMob) {
        this.mob = pMob;
        intimacy = 1;
    }

    public int getIntimacy() {
        return intimacy;
    }

    public void setIntimacy(int pIntimacy) {
        this.intimacy = pIntimacy;
    }

    public void increase() {
        ++this.intimacy;
    }

    public void reset() {
        this.setIntimacy(0);
    }

    public void addAdditionalData(CompoundTag tag) {
        tag.putInt("Intimacy", this.getIntimacy());
    }

    public void readAdditionalData(CompoundTag tag) {
        if (tag.contains("Intimacy")) {
            this.setIntimacy(tag.getInt("Intimacy"));
        }
    }
}
