package com.github.t1.sap;

import com.github.t1.sap.javac.AnnotationUsage;
import com.github.t1.sap.javac.ClassDeclaration;
import com.github.t1.sap.javac.CompilationUnit;
import com.github.t1.sap.javac.TypeResolver;
import lombok.NonNull;
import lombok.Value;

import javax.annotation.processing.Messager;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

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
        messager.printMessage(OTHER, "------------ in:\n" + compilationUnit.toSource());
        target.getAnnotations().removeIf(this::isStereotype);
        messager.printMessage(OTHER, "------------ out:\n" + compilationUnit.toSource());
    }

    private boolean isStereotype(AnnotationUsage annotation) {
        return stereotype.equals(annotation.getType());
    }
}
