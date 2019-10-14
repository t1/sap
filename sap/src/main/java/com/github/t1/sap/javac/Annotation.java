package com.github.t1.sap.javac;

import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.atomic.AtomicReference;

import static lombok.AccessLevel.PACKAGE;

@RequiredArgsConstructor(access = PACKAGE)
public class Annotation {
    private final @NonNull TypeResolver resolver;
    private final @NonNull CompilationUnit compilationUnit;
    final @NonNull JCAnnotation jcAnnotation;
    private AtomicReference<ClassDeclaration> type;

    public String getQualifiedTypeName() { return identifier().sym.toString(); }

    /** Only available if the type is being compiled as well! */
    public ClassDeclaration getType() {
        if (type == null)
            type = new AtomicReference<>(resolveType());
        return type.get(); // can be null
    }

    private ClassDeclaration resolveType() {
        return resolver.classDeclaration(classSymbol());
    }

    private ClassSymbol classSymbol() { return (ClassSymbol) identifier().sym; }

    private JCIdent identifier() { return (JCIdent) jcAnnotation.annotationType; }

    public Annotation copy(ClassDeclaration target) {
        JCAnnotation copy = resolver.copyAnnotation(jcAnnotation);
        return new Annotation(resolver, target.getCompilationUnit(), copy);
    }

    @Override public String toString() { return "Annotation(" + jcAnnotation + ")"; }
}
