package com.github.boybeak.adapter.compiler;

import com.github.boybeak.adapter.annotation.AdapterConfig;
import com.github.boybeak.adapter.annotation.Constant;
import com.github.boybeak.adapter.annotation.LayoutInfo;
import com.github.boybeak.adapter.annotation.HolderInfo;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
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
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

@SupportedAnnotationTypes({
        "com.github.boybeak.adapter.annotation.AdapterConfig",
        "com.github.boybeak.adapter.annotation.HolderInfo"})
public class ViewHolderProcessor extends AbstractProcessor {

    private static final String SOURCE = "source", VIEW_TYPE = "viewType", BINDING = "binding";

    private String packageName = null;
    private Map<Integer, String> viewTypeHolders = new HashMap<>();
    private Map<Integer, String> viewTypeBindings = new HashMap<>();

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        Set<? extends Element> configs = roundEnvironment.getElementsAnnotatedWith(AdapterConfig.class);
        for (Element e : configs) {
            AdapterConfig config = e.getAnnotation(AdapterConfig.class);
            packageName = config.packageName();
        }

        Set<? extends Element> elementSet = roundEnvironment.getElementsAnnotatedWith(HolderInfo.class);
        List<? extends TypeElement> typeElements = new ArrayList<>(ElementFilter.typesIn(elementSet));

        for (TypeElement e : typeElements) {
            processHolderInfo(e, "total count is " + typeElements.size());
        }

        generateFactory();

        return false;
    }

    private void processHolderInfo(TypeElement element, String log) {
        HolderInfo holder = element.getAnnotation(HolderInfo.class);
        int layout = holder.layoutId();
        if (!viewTypeHolders.containsKey(layout)) {
            viewTypeHolders.put(layout, getHolderName(element));
        }
        if (!viewTypeBindings.containsKey(layout)) {
            viewTypeBindings.put(layout, getViewBinding(holder));
        }

        if (!Helper.isNullLayoutInfo(holder.layoutInfo())) {
            generateLayoutImpl(holder.layoutId(), holder.layoutInfo(), log);
        }
    }

    private void generateLayoutImpl(int layoutId, LayoutInfo info, String comment) {

        TypeName source = getSource(info);

        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(source, SOURCE).build())
                .addStatement("super($L)", SOURCE)
                .build();

        MethodSpec layoutOM = MethodSpec.methodBuilder("getLayout")
                .addAnnotation(AnnotationSpec.builder(Override.class).build())
                .addModifiers(Modifier.PUBLIC)
                .returns(int.class)
                .addCode("return $L;\n", layoutId)
                .build();

        ParameterizedTypeName superClz = ParameterizedTypeName.get(
                ClassName.get("com.github.boybeak.adapter", "BaseLayoutImpl"),
                source
        );

        TypeSpec typeSpec = TypeSpec.classBuilder(info.name())
                .addJavadoc(comment)
                .addModifiers(Modifier.PUBLIC)
                .superclass(superClz)
                .addMethod(constructor)
                .addMethod(layoutOM)
                .build();

        JavaFile javaFile = JavaFile.builder(Constant.PACKAGE, typeSpec).build();
        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private TypeName getSource(LayoutInfo info) {
        TypeMirror clazzType = null;
        try {
            return TypeName.get(info.source());
        } catch (MirroredTypeException mte) {
            clazzType = mte.getTypeMirror();
        }
        return TypeName.get(clazzType);
    }

    private void generateFactory() {
        MethodSpec getHolderSpec = getHolderMethodSpec();

        TypeSpec typeSpec = TypeSpec.classBuilder(Constant.FACTORY)
                .addJavadoc(viewTypeBindings.size() + "")
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
        if (!viewTypeBindings.isEmpty()) {
            builder.addCode("switch(viewType) {\n");
            Set<Integer> keys = viewTypeBindings.keySet();
            for (Integer key : keys) {

                String holderName = viewTypeHolders.get(key);
                ClassName holderClz = Helper.getClassName(holderName);
                String bindingName = viewTypeBindings.get(key);
                ClassName bindingClz = Helper.getClassName(bindingName);

                builder.addCode("\tcase $L:\n", key);
                builder.addCode("\treturn new $T(($T)$L);\n", holderClz, bindingClz, BINDING);

                builder.addComment(bindingName);
            }
            builder.addCode("}\n");
        }

        return builder.addCode("return null;\n").build();
    }

    private String getViewBinding(HolderInfo holder) {
        String name;
        try {
            name = holder.viewBindingClass().getName();
        } catch (MirroredTypeException mte) {
            name = mte.getTypeMirror().toString();
        }
        return packageName + ".databinding" + name.substring(name.lastIndexOf('.'));
    }

    private String getHolderName(TypeElement element) {
        String name = element.getQualifiedName().toString();
        ClassName cn = Helper.getClassName(name);
        return cn.toString();
    }

}