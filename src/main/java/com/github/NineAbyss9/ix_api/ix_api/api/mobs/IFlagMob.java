
package com.github.NineAbyss9.ix_api.ix_api.api.mobs;

public interface IFlagMob {
    int getFlag();

    void setFlag(int flag);

    default int getAttackTick() {
        return 0;
    }

    default void setAttackTick(int tick) {
    }

    default boolean isFlag(int flag) {
        return this.getFlag() == flag;
    }

    default void resetAttackTick() {
        this.setAttackTick(0);
    }

    default void plusAttackTick() {
        this.setAttackTick(this.getAttackTick() + 1);
    }

    default void resetFlag() {
        this.setFlag(0);
    }

    default void resetState() {
        this.resetFlag();
        this.resetAttackTick();
    }

    default boolean attackTickEquals(int pAttackTick) {
        return this.getAttackTick() == pAttackTick;
    }

    default boolean attackTickMoreThan(int pAttackTick) {
        return this.getAttackTick() >= pAttackTick;
    }
}
