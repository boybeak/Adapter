package com.github.boybeak.adapter.compiler;

import com.github.boybeak.adapter.annotation.Constant;
import com.github.boybeak.adapter.annotation.LayoutInfo;
import com.github.boybeak.adapter.annotation.HolderInfo;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;

@SupportedAnnotationTypes({
        "com.github.boybeak.adapter.annotation.AdapterConfig",
        "com.github.boybeak.adapter.annotation.HolderInfo"})
public class ViewHolderProcessor extends AbstractProcessor {

    private static final String SOURCE = "source", VIEW_TYPE = "viewType", BINDING = "binding";

    private Map<Integer, TypeElement> viewTypeHolders = new HashMap<>();
//    private Map<Integer, String> viewTypeBindings = new HashMap<>();

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        Set<? extends Element> elementSet = roundEnvironment.getElementsAnnotatedWith(HolderInfo.class);
        List<? extends TypeElement> typeElements = new ArrayList<>(ElementFilter.typesIn(elementSet));

        for (TypeElement e : typeElements) {

            processHolderInfo(e, e.getSuperclass().toString());
        }

        generateFactory();

        return false;
    }

    private void processHolderInfo(TypeElement element, String log) {
        HolderInfo holder = element.getAnnotation(HolderInfo.class);
        int layout = holder.layoutId();
        if (!viewTypeHolders.containsKey(layout)) {
            viewTypeHolders.put(layout, element);
        }
        /*if (!viewTypeBindings.containsKey(layout)) {
            viewTypeBindings.put(layout, getViewBinding(holder));
        }*/

        if (!Helper.isNullLayoutInfo(holder.layoutInfo())) {
            generateLayoutImpl(holder.layoutId(), holder.layoutInfo(), log);
        }
    }

    private void generateLayoutImpl(int layoutId, LayoutInfo info, String comment) {
        JavaFile javaFile = JavaFile.builder(Constant.PACKAGE,
                LayoutGenerator.generateLayoutImpl(layoutId, info, comment)).build();
        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateFactory() {
        MethodSpec getHolderSpec = getHolderMethodSpec();

        TypeSpec typeSpec = TypeSpec.classBuilder(Constant.FACTORY)
//                .addJavadoc(viewTypeBindings.size() + "")
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ClassName.get("com.github.boybeak.adapter", "HolderFactory"))
                .addMethod(getHolderSpec)
                .build();

        JavaFile javaFile = JavaFile.builder(Constant.PACKAGE, typeSpec).build();
        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private MethodSpec getHolderMethodSpec() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("getHolder")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(int.class, VIEW_TYPE).build())
                .addParameter(ParameterSpec.builder(ClassName.get("android.databinding", "ViewDataBinding"), BINDING).build())
                .returns(ClassName.get("com.github.boybeak.adapter", "AbsDataBindingHolder"));
        if (!viewTypeHolders.isEmpty()) {
            builder.addCode("switch(viewType) {\n");
            Set<Integer> keys = viewTypeHolders.keySet();
            for (Integer key : keys) {
                TypeElement element = viewTypeHolders.get(key);
                String holderSupClzName = element.getSuperclass().toString();
                if (!holderSupClzName.contains("com.github.boybeak.adapter.AbsDataBindingHolder")) {
                    continue;
                }
                String holderName = getHolderName(element);
                ClassName holderClz = Helper.getClassName(holderName);
                String bindingName = getViewBindingClzName(holderSupClzName);
                ClassName bindingClz = Helper.getClassName(bindingName);

                builder.addCode("\tcase $L:\n", key);
                builder.addCode("\treturn new $T(($T)$L);\n", holderClz, bindingClz, BINDING);

//                builder.addComment(bindingName);
            }
            builder.addCode("}\n");
        }

        return builder.addCode("return null;\n").build();
    }

    private String getViewBindingClzName(String holderSupClzName) {
        return holderSupClzName.substring(
                holderSupClzName.lastIndexOf(",") + 1,
                holderSupClzName.lastIndexOf(">"));
    }

    private String getHolderName(TypeElement element) {
        String name = element.getQualifiedName().toString();
        ClassName cn = Helper.getClassName(name);
        return cn.toString();
    }

}