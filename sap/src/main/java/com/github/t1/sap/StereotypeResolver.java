package com.github.t1.sap;

import com.github.t1.sap.javac.Annotation;
import com.github.t1.sap.javac.ClassDeclaration;
import com.github.t1.sap.javac.CompilationUnit;
import com.github.t1.sap.javac.Import;
import com.github.t1.sap.javac.TypeResolver;
import lombok.NonNull;
import lombok.Value;

import javax.annotation.processing.Messager;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import java.util.Collection;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static javax.tools.Diagnostic.Kind.NOTE;
import static javax.tools.Diagnostic.Kind.OTHER;

@Value class StereotypeResolver {
    private final @NonNull Messager messager;
    private final @NonNull TypeResolver resolver;
    private final @NonNull CompilationUnit compilationUnit;
    private final @NonNull ClassDeclaration target;
    private final @NonNull ClassDeclaration stereotype;

    StereotypeResolver(Messager messager, Elements elements, TypeElement target, TypeElement stereotype) {
        this.messager = messager;
        this.resolver = new TypeResolver(elements);
        this.compilationUnit = resolver.compilationUnit(target);
        this.target = resolver.classDeclaration(target);
        this.stereotype = resolver.classDeclaration(stereotype);
    }

    void resolve() {
        messager.printMessage(NOTE, "resolve stereotype: " + stereotype.getQualifiedName(), target.getElement());
        List<Annotation> annotations = target.getAnnotations();
        annotations.addAll(stereotypeAnnotations());
        annotations.removeIf(annotation -> stereotype.equals(annotation.getType()));
        target.addImports(stereotype.getImports().filter(StereotypeResolver::relevant));
        messager.printMessage(OTHER, "[DEBUG] out:\n" + compilationUnit.toSource());
    }

    private Collection<Annotation> stereotypeAnnotations() {
        return stereotype.getAnnotations().stream()
            .filter(usage -> !DONT_COPY.contains(usage.getQualifiedTypeName()))
            .map(annotation -> annotation.copy(target))
            .collect(toList());
    }

    private static boolean relevant(Import i) {
        return !IRRELEVANT_IMPORTS.contains(i.getName());
    }

    /** The annotations that don't get copied to the target */
    private static final List<String> DONT_COPY = asList(
        "javax.enterprise.inject.Stereotype",
        "java.lang.annotation.Retention");
    /** The imports that don't get copied to the target */
    public static final List<String> IRRELEVANT_IMPORTS = asList(
        "javax.enterprise.inject.Stereotype",
        "java.lang.annotation.Retention",
        "java.lang.annotation.RetentionPolicy.RUNTIME");
}
