package com.github.t1.sap.javac;

import com.sun.tools.javac.tree.JCTree.JCModifiers;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class Modifiers {
    private final @NonNull TypeResolver resolver;
    private final @NonNull JCModifiers jcModifiers;
    private List<AnnotationUsage> annotations;

    public List<AnnotationUsage> getAnnotations() {
        if (annotations == null)
            annotations = new JavacList<>(jcModifiers.annotations, jcAnnotation -> new AnnotationUsage(resolver, jcAnnotation));
        return annotations;
    }
}
