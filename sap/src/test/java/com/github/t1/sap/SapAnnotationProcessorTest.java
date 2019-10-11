package com.github.t1.sap;

import com.github.t1.apctt.AbstractAnnotationProcessorTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;

import javax.annotation.processing.Processor;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.BDDAssertions.then;

public class SapAnnotationProcessorTest extends AbstractAnnotationProcessorTest {

    @Override protected Iterable<? extends Processor> getProcessors() { return singletonList(new SapAnnotationProcessor()); }

    @BeforeAll static void enableDebugLog() { System.setProperty(SapAnnotationProcessor.DEBUG_PROPERTY, "true"); }

    @Test void shouldCompileClassWithOneNonStereotype() {
        compile(file("Simple.java", "" +
            "@Deprecated\n" +
            "public class Simple {\n" +
            "}"));

        expect(
            debug("Stereotypes: []")
        );
        ClassNode classNode = classNode("Simple.class");
        then(classNode.name).isEqualTo("Simple");
        then(classNode.visibleAnnotations).hasSize(1);
        AnnotationNode annotation = classNode.visibleAnnotations.get(0);
        then(annotation.desc).isEqualTo("Ljava/lang/Deprecated;");
        then(annotation.values).isNull();
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
                "}\n"),
            file("mypackage/OtherClass.java", "" +
                "package mypackage;\n" +
                "\n" +
                "@Deprecated\n" +
                "public class OtherClass {\n" +
                "    @Deprecated public void foo() {}\n" +
                "}\n"));

        expect(
            debug("Stereotypes: [mypackage.Boundary]"),
            note("/mypackage/MyBoundary.java", 49, 20, 69, 5, 8, "compiler.note.proc.messager",
                "resolve stereotype: mypackage.Boundary")
        );
        ClassNode classNode = classNode("mypackage/MyBoundary.class");
        then(classNode.name).isEqualTo("mypackage/MyBoundary");

        then(classNode.visibleAnnotations).hasSize(1);

        AnnotationNode annotation = classNode.visibleAnnotations.get(0);
        then(annotation.desc).isEqualTo("Ljava/lang/Deprecated;");
        then(annotation.values).isNull();
    }
}
