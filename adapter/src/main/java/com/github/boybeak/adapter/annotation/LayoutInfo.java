package com.github.boybeak.adapter.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
public @interface LayoutInfo {
    String name() default "";
    Class<?> source() default void.class;
    boolean supportSelect() default false;
    boolean selectable() default false;
}