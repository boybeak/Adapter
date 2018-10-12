package com.github.boybeak.adapter.annotation;

public @interface Constructor {
    boolean useId() default false;
    Member[] members() default {};
}