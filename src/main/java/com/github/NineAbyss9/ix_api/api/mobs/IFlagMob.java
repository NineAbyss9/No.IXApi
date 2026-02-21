
package com.github.NineAbyss9.ix_api.api.mobs;

public interface IFlagMob {
    int getFlag();

    void setFlag(int flag);

    default int getAniTick() {
        return 0;
    }

    default void setAniTick(int tick) {
    }

    default boolean isFlag(int flag) {
        return this.getFlag() == flag;
    }

    default void resetAniTick() {
        this.setAniTick(0);
    }

    default void increaseAniTick() {
        this.setAniTick(this.getAniTick() + 1);
    }

    default void resetFlag() {
        this.setFlag(0);
    }

    default void resetState() {
        this.resetFlag();
        this.resetAniTick();
    }

    default boolean aniTickEquals(int pAttackTick) {
        return this.getAniTick() == pAttackTick;
    }

    default boolean aniTick(int pAttackTick) {
        return this.getAniTick() >= pAttackTick;
    }
}
