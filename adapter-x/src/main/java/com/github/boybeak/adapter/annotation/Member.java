package com.github.boybeak.adapter.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
public @interface Member {
    String name();
    Class<?> type();
    Class<?>[] generics() default {};
}
