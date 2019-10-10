package com.github.t1.sap;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@SupportedAnnotationTypes("*")
public class SapAnnotationProcessor extends AbstractAnnotationProcessor {
    @Override public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver())
            return false;
        note("process " + annotations + ": " + roundEnv.getRootElements());
        for (Element element : roundEnv.getRootElements()) {
            note(" - " + element);
        }
        return true;
    }
}
