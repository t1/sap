package com.github.t1.sap.javac;

import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.NONE;

@RequiredArgsConstructor
public class CompilationUnit {
    private final @NonNull TypeResolver resolver;
    private final @NonNull JCCompilationUnit jcCompilationUnit;

    public String toSource() { return jcCompilationUnit.toString(); }
}
