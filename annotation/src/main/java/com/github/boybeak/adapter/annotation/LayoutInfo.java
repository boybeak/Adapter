package com.github.boybeak.adapter.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
public @interface LayoutInfo {
    String name();
    Class<?> source();
    boolean supportSelect() default false;
    boolean selectable() default false;
}