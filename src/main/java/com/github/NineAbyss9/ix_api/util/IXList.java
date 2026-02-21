
package com.github.NineAbyss9.ix_api.util;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public final class IXList<I> extends ArrayList<I> {
    IXList(int initialCapacity) {
        super(initialCapacity);
    }

    IXList() {
        super();
    }

    @Nonnull
    public static <I> IXList<I> of(int initialCapacity) {
        return new IXList<>(initialCapacity);
    }

    @Nonnull
    public static <I> IXList<I> of() {
        return new IXList<>();
    }

    public I getFirst() {
        return this.get(0);
    }

    public I getLast() {
        return this.get(size() - 1);
    }

    @Nonnull
    public static int[] raidCount(@Nonnull List<? extends Integer> list) {
        return new int[]{
           list.get(0), list.get(1), list.get(2), list.get(3), list.get(4), list.get(5), list.get(6), list.get(7)
        };
    }
}
