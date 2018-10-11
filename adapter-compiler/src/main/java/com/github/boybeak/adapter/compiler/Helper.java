package com.github.boybeak.adapter.compiler;

import com.github.boybeak.adapter.annotation.LayoutInfo;
import com.squareup.javapoet.ClassName;

class Helper {
    private static String getPackage(String name) {
        return name.substring(0, name.lastIndexOf('.'));
    }
    private static String getSimpleName(String name) {
        return name.substring(name.lastIndexOf('.') + 1);
    }
    public static ClassName getClassName(String name) {
        return ClassName.get(getPackage(name), getSimpleName(name));
    }
    public static boolean isNullLayoutInfo(LayoutInfo info) {
        return "".equals(info.name());
    }
}
