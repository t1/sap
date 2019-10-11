package com.github.t1.sap.javac;

import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.util.Pair;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
public class AnnotationUsage {
    private final @NonNull TypeResolver resolver;
    private final @NonNull JCAnnotation jcAnnotation;
    private AtomicReference<ClassDeclaration> type;

    /** Only available if the type is being compiled as well! */
    public ClassDeclaration getType() {
        if (type == null)
            type = new AtomicReference<>(resolveType());
        return type.get(); // can be null
    }

    private ClassDeclaration resolveType() {
        JCIdent annotationType = (JCIdent) jcAnnotation.annotationType;
        Pair<JCTree, JCCompilationUnit> treeAndTopLevel = resolver.elements.getTreeAndTopLevel(annotationType.sym, null, null);
        return (treeAndTopLevel == null) ? null :
            new ClassDeclaration(resolver, (ClassSymbol) annotationType.sym, (JCClassDecl) treeAndTopLevel.fst);
    }
}
