package com.github.t1.sap;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.enterprise.inject.Stereotype;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

@SupportedAnnotationTypes("*")
public class SapAnnotationProcessor extends AbstractAnnotationProcessor {
    public static final String DEBUG_PROPERTY = SapAnnotationProcessor.class.getCanonicalName() + "#DEBUG";

    private final Set<Element> stereotypes = new HashSet<>();
    private final List<StereotypeTarget> targets = new ArrayList<>();

    @Override public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver())
            return false;
        findStereotypes(roundEnv);
        findTargets(roundEnv);
        targets.forEach(StereotypeTarget::resolve);
        return true;
    }

    private void findStereotypes(RoundEnvironment roundEnv) {
        rootElements(roundEnv).filter(this::isStereotype).forEach(stereotypes::add);
        debug(() -> "Stereotypes: " + stereotypes);
    }

    private Stream<? extends Element> rootElements(RoundEnvironment roundEnv) {
        return roundEnv.getRootElements().stream();
    }

    private boolean isStereotype(Element element) {
        return element.getAnnotation(Stereotype.class) != null;
    }

    private void findTargets(RoundEnvironment roundEnv) {
        rootElements(roundEnv).flatMap(this::createTargets).filter(Objects::nonNull).forEach(targets::add);
    }

    private Stream<StereotypeTarget> createTargets(Element element) {
        if (!(element instanceof TypeElement))
            return null;
        TypeElement type = (TypeElement) element;
        return type.getAnnotationMirrors().stream()
            .map(mirror -> mirror.getAnnotationType().asElement())
            .flatMap(annotation -> (annotation instanceof TypeElement) ? Stream.of((TypeElement) annotation) : Stream.of())
            .filter(stereotypes::contains)
            .map(stereotype -> new StereotypeTarget(processingEnv.getMessager(), type, stereotype));
    }
}
