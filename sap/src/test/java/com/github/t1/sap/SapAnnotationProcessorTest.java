package com.github.t1.sap;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.annotation.processing.Processor;

import static java.util.Collections.singletonList;

public class SapAnnotationProcessorTest extends AbstractAnnotationProcessorTest {

    @Override Iterable<? extends Processor> getProcessors() { return singletonList(new SapAnnotationProcessor()); }

    @BeforeAll static void enableDebugLog() { System.setProperty(SapAnnotationProcessor.DEBUG_PROPERTY, "true"); }

    @Test void shouldCompileClassWithOneNonStereotype() {
        compile(file("Simple.java", "" +
            "@Deprecated\n" +
            "public class Simple {\n" +
            "}"));

        expect(
            debug("process java.lang.Deprecated"),
            debug(" - Simple")
        );
    }

    @Test void shouldCompileClassWithOneStereotype() {
        compile(
            file("mypackage/Boundary.java", "" +
                "package mypackage;\n" +
                "\n" +
                "import javax.ejb.Stateless;\n" +
                "import javax.enterprise.inject.Stereotype;\n" +
                "import java.lang.annotation.Retention;\n" +
                "\n" +
                "import static java.lang.annotation.RetentionPolicy.RUNTIME;\n" +
                "\n" +
                "@Stereotype\n" +
                "@Retention(RUNTIME)\n" +
                "@Stateless\n" +
                "public @interface Boundary {\n" +
                "}\n"),
            file("mypackage/MyBoundary.java", "" +
                "package mypackage;\n" +
                "\n" +
                "@Boundary\n" +
                "@Deprecated\n" +
                "public class MyBoundary {\n" +
                "}\n"));

        expect(
            //     debug("process [java.lang.Deprecated]: [Simple]"),
            //     debug(" - Simple")
        );
    }
}
