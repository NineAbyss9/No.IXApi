
package com.github.NineAbyss9.ix_api.ix_api.api.crafting;

import net.minecraft.world.inventory.DataSlot;

public class SubDataSlot
extends DataSlot {
    private int value;
    private final int maxValue;
    public SubDataSlot(int pMaxValue) {
        this.maxValue = pMaxValue;
    }

    public int get() {
        return value;
    }

    public void set(int pValue) {
        value = Math.min(maxValue, pValue);
    }
}
