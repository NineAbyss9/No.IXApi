
package com.github.NineAbyss9.ix_api.api;

@FunctionalInterface
public interface Both<A, B> {
    void accept(A var1, B var2);

    default Both<A, B> andThen(Both<? super A, ? super B> after) {
        return (a, b) -> {
            accept(a, b);
            after.accept(a, b);
        };
    }
}
