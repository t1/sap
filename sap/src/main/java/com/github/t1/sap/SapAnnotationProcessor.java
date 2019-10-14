package com.github.t1.sap;

import com.github.t1.sap.tools.AbstractAnnotationProcessor;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.enterprise.inject.Stereotype;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@SupportedAnnotationTypes("*")
public class SapAnnotationProcessor extends AbstractAnnotationProcessor {
    static final String DEBUG_PROPERTY = SapAnnotationProcessor.class.getCanonicalName() + "#DEBUG";

    private final Map<Element, String> stereotypesMap = new IdentityHashMap<>();
    private final Set<Element> stereotypes = stereotypesMap.keySet();
    private int round = 0;

    @Override public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        debug(() -> "round " + round++ + "; over=" + roundEnv.processingOver());
        if (roundEnv.processingOver())
            return false;

        findStereotypes(roundEnv);

        List<StereotypeResolver> targets = findTargets(roundEnv);
        debug(() -> "found " + stereotypes.size() + " stereotypes for " + targets.size() + " targets");
        for (StereotypeResolver target : targets)
            target.resolve();

        return true;
    }

    private void findStereotypes(RoundEnvironment roundEnv) {
        rootElements(roundEnv).filter(this::isStereotype).forEach(element -> stereotypesMap.put(element, ""));
        debug(() -> "Stereotypes: " + stereotypes);
    }

    private Stream<? extends Element> rootElements(RoundEnvironment roundEnv) {
        return roundEnv.getRootElements().stream();
    }

    private boolean isStereotype(Element element) {
        return element.getAnnotation(Stereotype.class) != null;
    }

    private List<StereotypeResolver> findTargets(RoundEnvironment roundEnv) {
        return rootElements(roundEnv).flatMap(this::createTargets).filter(Objects::nonNull).collect(toList());
    }

    private Stream<StereotypeResolver> createTargets(Element element) {
        if (!(element instanceof TypeElement))
            return null;
        TypeElement type = (TypeElement) element;
        return type.getAnnotationMirrors().stream()
            .map(mirror -> mirror.getAnnotationType().asElement())
            .flatMap(annotation -> (annotation instanceof TypeElement) ? Stream.of((TypeElement) annotation) : Stream.of())
            .filter(stereotypes::contains)
            .map(stereotype -> new StereotypeResolver(processingEnv.getMessager(), processingEnv.getElementUtils(), type, stereotype));
    }
}
