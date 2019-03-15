package fr.ubordeaux.jmetrics.project;

/**
 * Service that traverses directories to generate a Project Structure containing bytecode class files.
 */
public class BytecodeFileSystemExplorer extends SimpleFileSystemExplorer {

    private static final String CLASS_EXTENSION = ".class";

    public BytecodeFileSystemExplorer() {
        super(CLASS_EXTENSION);
    }

}
