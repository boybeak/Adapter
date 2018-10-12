package com.github.boybeak.adapter.annotation;

public @interface Member {
    String name();
    Class<?> type();
    Class<?>[] generics() default {};
}
