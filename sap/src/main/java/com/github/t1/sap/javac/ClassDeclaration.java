package com.github.t1.sap.javac;

import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.lang.model.element.TypeElement;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class ClassDeclaration {
    private final @NonNull TypeResolver resolver;
    private final @NonNull TypeElement typeElement;
    private final @NonNull JCClassDecl classDecl;
    private Modifiers modifiers;

    public String getQualifiedName() { return classDecl.sym.toString(); }

    public TypeElement getElement() { return typeElement; }

    public String toSource() { return classDecl.toString(); }

    public List<AnnotationUsage> getAnnotations() { return getModifiers().getAnnotations(); }

    public Modifiers getModifiers() {
        if (modifiers == null)
            modifiers = new Modifiers(resolver, classDecl.mods);
        return modifiers;
    }

    @Override public String toString() {
        return "ClassDeclaration(" + classDecl.name + ")";
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ClassDeclaration that = (ClassDeclaration) o;
        return classDecl.equals(that.classDecl);
    }

    @Override public int hashCode() { return Objects.hash(classDecl); }
}
