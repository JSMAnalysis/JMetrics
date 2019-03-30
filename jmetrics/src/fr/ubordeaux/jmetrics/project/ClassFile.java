package fr.ubordeaux.jmetrics.project;

import java.io.File;

/**
 * Represents a Java class file (containing ByteCode or SourceCode).
 */
public class ClassFile extends ProjectComponent {

    public ClassFile(File file, String name) {
        super(file, name);
    }

}
