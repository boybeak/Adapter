package com.github.boybeak.adapter.compiler;

import com.github.boybeak.adapter.annotation.Constructor;
import com.github.boybeak.adapter.annotation.LayoutInfo;
import com.github.boybeak.adapter.annotation.Member;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeMirror;

public class LayoutGenerator {

    private static final String SOURCE = "source", ID = "id";

    public static TypeSpec generateLayoutImpl(int layoutId, LayoutInfo info, String comment) {

        TypeName source = getSource(info);
        ParameterizedTypeName superClz = ParameterizedTypeName.get(
                ClassName.get("com.github.boybeak.adapter", "AbsLayoutImpl"),
                source
        );
        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(info.name())
                .addJavadoc(comment)
                .addModifiers(Modifier.PUBLIC)
                .superclass(superClz);

        fillConstructors(typeBuilder, source, info.constructors());

        MethodSpec layoutOM = MethodSpec.methodBuilder("getLayout")
                .addAnnotation(AnnotationSpec.builder(Override.class).build())
                .addModifiers(Modifier.PUBLIC)
                .returns(int.class)
                .addCode("return $L;\n", layoutId)
                .build();
        typeBuilder.addMethod(layoutOM);

        if (info.supportSelect()) {
            MethodSpec supportSelect = MethodSpec.methodBuilder("supportSelect")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(boolean.class)
                    .addCode("return true;\n")
                    .build();
            typeBuilder.addMethod(supportSelect);

            if (info.selectable()) {
                MethodSpec selectable = MethodSpec.methodBuilder("isSelectable")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(boolean.class)
                        .addCode("return true;\n")
                        .build();
                typeBuilder.addMethod(selectable);
            }

        }

        return typeBuilder.build();
    }

    private static void fillConstructors(TypeSpec.Builder builder, TypeName source, Constructor[] constructors) {
        if (constructors.length == 0) {
            MethodSpec defaultConstructor = MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(ParameterSpec.builder(source, SOURCE).build())
                    .addStatement("super($L)", SOURCE)
                    .build();
            builder.addMethod(defaultConstructor);
        } else {
            for (Constructor c : constructors) {
                MethodSpec.Builder mb = MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(ParameterSpec.builder(source, SOURCE).build());
                if (c.useId()) {
                    mb.addParameter(ParameterSpec.builder(String.class, ID).build())
                            .addStatement("super($L, $L)", SOURCE, ID);
                } else {
                    mb.addStatement("super($L)", SOURCE);
                }
                for (Member member : c.members()) {
                    TypeName memberType = getMemberType(member);
                    String memberName = member.name();
                    /*if (member.isField()) {

                    }*/
                    builder.addField(memberType, memberName, Modifier.PUBLIC);
                    mb.addParameter(memberType, member.name());

                    mb.addStatement("this.$L = $L", memberName, memberName);
                }
                builder.addMethod(mb.build());
            }
        }
    }

    private static TypeName getSource(LayoutInfo info) {
        TypeMirror clazzType;
        try {
            return TypeName.get(info.source());
        } catch (MirroredTypeException mte) {
            clazzType = mte.getTypeMirror();
        }
        return TypeName.get(clazzType);
    }

    private static TypeName getMemberType(Member member) {
//        TypeMirror clazzType;
//        try {
            return ParameterizedTypeName.get(getMemberTypeClassName(member), getTypes(member));
//        } catch (MirroredTypeException mte) {
//            clazzType = mte.getTypeMirror();
//        }
//        return ParameterizedTypeName.get(getMemberTypeClassName(member), getTypes(member));
    }

    private static ClassName getMemberTypeClassName(Member member) {
        TypeMirror clazzType;
        try {
            return ClassName.get(member.type());
        } catch (MirroredTypeException mte) {
            clazzType = mte.getTypeMirror();
        }
        return Helper.getClassName(clazzType.toString());
    }

    private static TypeName[] getTypes(Member member) {
        List<? extends TypeMirror> mirrors;
        TypeName[] typeNames;
        try {
            Class<?>[] types = member.generics();
            typeNames = new TypeName[types.length];
            for (int i = 0; i < typeNames.length; i++) {
                typeNames[i] = TypeName.get(types[i]);
            }
        } catch (MirroredTypesException mte) {
            mirrors = mte.getTypeMirrors();
            typeNames = new TypeName[mirrors.size()];
            for (int i = 0; i < mirrors.size(); i++) {
                typeNames[i] = TypeName.get(mirrors.get(i));
            }
        }
        return typeNames;
    }

}
