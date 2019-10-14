package com.github.t1.sap.javac;

import com.sun.tools.javac.tree.JCTree.JCImport;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Import {
    final @NonNull JCImport jcImport;

    public String getName() {
        return jcImport.getQualifiedIdentifier().toString();
    }
}
