
package com.github.NineAbyss9.ix_api.util;

import javax.annotation.Nonnull;

public class Vector9f {
    private final float x;
    private final float y;
    private final float z;
    Vector9f(float pX, float pY, float pZ) {
        this.x = pX;
        this.y = pY;
        this.z = pZ;
    }

    @Nonnull
    public static Vector9f create(float x, float y, float z) {
        return new Vector9f(x, y, z);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }
}
