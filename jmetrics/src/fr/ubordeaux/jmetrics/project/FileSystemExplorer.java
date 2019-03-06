package fr.ubordeaux.jmetrics.project;

/**
 * Provides an interface allowing to explore a Java project.
 */
public interface FileSystemExplorer {

    /**
     * Explores the directory corresponding to a given path and generates a {@link ProjectComponent}'s tree
     * representing the structure of the Java project that it contains.
     * @param path The path leading to the project's root directory.
     * @return The root of the tree representing the project.
     * @throws InvalidProjectPathException If the path does not lead to a directory or leads to a non existent
     * directory.
     */
    ProjectComponent generateStructure(String path) throws InvalidProjectPathException;

}
