package com.github.t1.sap.javac;

import com.sun.tools.javac.model.JavacElements;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.util.Pair;
import lombok.NonNull;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

public class TypeResolver {
    final @NonNull JavacElements elements;

    public TypeResolver(Elements elements) { this.elements = (JavacElements) elements; }

    public CompilationUnit compilationUnit(Element element) {
        return new CompilationUnit(this, resolve(element).snd);
    }

    public ClassDeclaration classDeclaration(TypeElement typeElement) {
        return new ClassDeclaration(this, typeElement, (JCClassDecl) resolve(typeElement).fst);
    }

    private Pair<JCTree, JCCompilationUnit> resolve(Element element) {
        return elements.getTreeAndTopLevel(element, null, null);
    }
}
