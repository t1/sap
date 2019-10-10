package com.github.t1.sap;

import org.junit.jupiter.api.Test;

import javax.annotation.processing.Processor;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.joining;

public class SapAnnotationProcessorTest extends AbstractAnnotationProcessorTest {
    @Override Iterable<? extends Processor> getProcessors() { return singletonList(new SapAnnotationProcessor()); }

    @Test void shouldSimplyCompile() {
        compile(file("Simple.java", "" +
            "@Deprecated\n" +
            "public class Simple {\n" +
            "}"));

        expect(
            note("process [java.lang.Deprecated]: [Simple]"),
            note(" - Simple")
        );
    }
}
