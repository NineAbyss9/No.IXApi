
package com.github.NineAbyss9.ix_api.ix_api.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(value = ElementType.TYPE)
public @interface Prototype {
    String prototype();
}
