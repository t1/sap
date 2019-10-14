package com.github.t1.sap.javac;

import com.sun.tools.javac.main.JavaCompiler;
import com.sun.tools.javac.model.JavacElements;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Pair;
import lombok.NonNull;
import lombok.SneakyThrows;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import java.lang.reflect.Field;

public class TypeResolver {
    private final @NonNull JavacElements elements;

    public TypeResolver(Elements elements) { this.elements = (JavacElements) elements; }

    public CompilationUnit compilationUnit(Element element) {
        return compilationUnit(resolve(element));
    }

    private CompilationUnit compilationUnit(Pair<JCTree, JCCompilationUnit> resolved) {
        return new CompilationUnit(resolved.snd);
    }

    public ClassDeclaration classDeclaration(TypeElement typeElement) {
        Pair<JCTree, JCCompilationUnit> resolved = resolve(typeElement);
        JCClassDecl classDecl = (resolved == null) ? null : (JCClassDecl) resolved.fst;
        CompilationUnit compilationUnit = (resolved == null) ? null : compilationUnit(resolved);
        return new ClassDeclaration(this, compilationUnit, typeElement, classDecl);
    }

    private Pair<JCTree, JCCompilationUnit> resolve(Element element) {
        return elements.getTreeAndTopLevel(element, null, null);
    }

    JCAnnotation copyAnnotation(JCAnnotation jcAnnotation) {
        TreeMaker maker = getTreeMaker();
        JCAnnotation annotation = maker.Annotation(jcAnnotation.annotationType, jcAnnotation.args);
        annotation.attribute = jcAnnotation.attribute;
        return annotation;
    }

    private TreeMaker getTreeMaker() {
        JavaCompiler compiler = getJavaCompiler();
        return access(compiler, "make", TreeMaker.class);
    }

    private JavaCompiler getJavaCompiler() {
        return access(elements, "javaCompiler", JavaCompiler.class);
    }

    @SneakyThrows(ReflectiveOperationException.class)
    private static <T> T access(Object instance, String fieldName, Class<T> type) {
        Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return type.cast(field.get(instance));
    }
}
