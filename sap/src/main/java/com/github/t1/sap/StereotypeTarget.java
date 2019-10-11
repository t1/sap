package com.github.t1.sap;

import lombok.Value;

import javax.annotation.processing.Messager;
import javax.lang.model.element.TypeElement;

import static javax.tools.Diagnostic.Kind.NOTE;

@Value class StereotypeTarget {
    private final Messager messager;
    public final TypeElement target;
    public final TypeElement stereotype;

    void resolve() {
        messager.printMessage(NOTE, "resolve stereotype: " + stereotype.getQualifiedName(), target);
    }
}
