package fr.ubordeaux.jmetrics.project;

public class SourceFileSystemExplorer extends SimpleFileSystemExplorer{

    private static final String SOURCE_EXTENSION = ".java";

    public SourceFileSystemExplorer() {
        super(SOURCE_EXTENSION);
    }

}
