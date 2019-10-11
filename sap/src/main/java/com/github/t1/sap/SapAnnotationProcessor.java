package com.github.t1.sap;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@SupportedAnnotationTypes("*")
public class SapAnnotationProcessor extends AbstractAnnotationProcessor {
    public static final String DEBUG_PROPERTY = SapAnnotationProcessor.class.getCanonicalName() + "#DEBUG";

    @Override public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver())
            return false;
        for (TypeElement annotation : annotations) {
            debug(() -> "process " + annotation);
            for (Element element : roundEnv.getRootElements()) {
                debug(() -> " - " + element);
            }
        }
        return true;
    }
}
