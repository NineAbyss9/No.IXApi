
package com.org.NineAbyss9.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(value = ElementType.TYPE)
public @interface Prototype {
    String prototype();
}
