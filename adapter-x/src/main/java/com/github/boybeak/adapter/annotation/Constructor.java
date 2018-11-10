package com.github.boybeak.adapter.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
public @interface Constructor {
    boolean useId() default false;
    Member[] members() default {};
}