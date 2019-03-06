package fr.ubordeaux.jmetrics.project;

/**
 * Service that traverses directories to generate a Project Structure containing source code files.
 */
public class SourceFileSystemExplorer extends SimpleFileSystemExplorer{

    private static final String SOURCE_EXTENSION = ".java";

    public SourceFileSystemExplorer() {
        super(SOURCE_EXTENSION);
    }

}
