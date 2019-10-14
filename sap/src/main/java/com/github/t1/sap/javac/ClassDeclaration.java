package com.github.t1.sap.javac;

import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCModifiers;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.lang.model.element.TypeElement;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static lombok.AccessLevel.PACKAGE;

@RequiredArgsConstructor(access = PACKAGE)
public class ClassDeclaration {
    private final @NonNull TypeResolver resolver;
    /** The classDecl is null, if the class is, e.g., from a library */
    @Getter private final CompilationUnit compilationUnit;
    private final @NonNull TypeElement typeElement;
    private final JCClassDecl classDecl;
    private List<Annotation> annotations;

    public String getQualifiedName() { return classDecl.sym.toString(); }

    public TypeElement getElement() { return typeElement; }

    public List<Annotation> getAnnotations() {
        if (annotations == null) {
            JCModifiers mods = classDecl.mods;
            annotations = new JavacList<>(
                mods.annotations,
                jcAnnotation -> new Annotation(resolver, compilationUnit, jcAnnotation),
                annotation -> annotation.jcAnnotation,
                l -> mods.annotations = l
            );
        }
        return annotations;
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

    public Stream<Import> getImports() {
        return getCompilationUnit().getImports();
    }

    public void addImports(Stream<Import> imports) {
        getCompilationUnit().addImports(imports);
    }
}
