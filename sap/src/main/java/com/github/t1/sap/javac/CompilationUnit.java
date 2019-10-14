package com.github.t1.sap.javac;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.tree.JCTree.JCImport;
import com.sun.tools.javac.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

import static com.sun.tools.javac.tree.JCTree.Tag.IMPORT;
import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PACKAGE;

@RequiredArgsConstructor(access = PACKAGE)
public class CompilationUnit {
    private final @NonNull JCCompilationUnit jcCompilationUnit;

    public String toSource() { return jcCompilationUnit.toString(); }

    public void addImports(Stream<Import> imports) {
        List<JCTree> out = List.nil();
        boolean importsCopied = false;
        for (JCTree def : jcCompilationUnit.defs) {
            if (def.getTag() == IMPORT)
                if (!importsCopied) {
                    importsCopied = true;
                    for (Import i : imports
                        .filter(i -> !jcCompilationUnit.defs.contains(i.jcImport))
                        .collect(toList()))
                        out = out.append(i.jcImport);
                }
            out = out.append(def);
        }

        jcCompilationUnit.defs = out;
    }

    public Stream<Import> getImports() {
        return jcCompilationUnit.defs.stream()
            .filter(tree -> tree.hasTag(IMPORT))
            .map(tree -> new Import((JCImport) tree));
    }
}
