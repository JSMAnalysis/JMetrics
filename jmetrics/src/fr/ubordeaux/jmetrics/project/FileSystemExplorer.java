package fr.ubordeaux.jmetrics.project;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Service that traverses directories to generate Project Structure.
 */
public class FileSystemExplorer {

    private final static String CLASS_EXTENSION = ".class";

    public ProjectComponent generateStructure(String path) {
        if (!isValidPath(path)) {
            throw new InvalidProjectPathException("Path does not exist or does not lead to a directory.");
        }
        File rootFile = new File(path);
        PackageDirectory project = new PackageDirectory(rootFile);
        project.setContent(getRecursiveStructure(rootFile, new ArrayList<>()));
        return project;
    }

    /**
     * Explore a project's structure recursively.
     * @param node The {@link File} to explore.
     * @param accumulator An accumulator representing the list built so far. Must be initialized as an empty list.
     * @return The structure of the given node.
     */
    private List<ProjectComponent> getRecursiveStructure(File node, List<ProjectComponent> accumulator) {
        List<ProjectComponent> components = new ArrayList<>(accumulator);
        if(isClassFile(node)){
            components.add(new ClassFile(node));
        }
        else if(node.isDirectory()) {
            File[] files = node.listFiles();
            if (files != null) {
                for (File file : files) {
                    components = getRecursiveStructure(file, components);
                }
            }
        }
        return components;
    }

    /**
     * Verify that a file is a class file.
     * @param file The file to verify.
     * @return true if the file is a class file, false otherwise.
     */
    private boolean isClassFile(File file) {
        return file.isFile() && file.getName().endsWith(CLASS_EXTENSION);
    }

    /**
     * Verify that a given path exists and correspond to a directory.
     * @param path The Path of to verify.
     * @return true if the path exists and leads to a directory, false otherwise.
     */
    private boolean isValidPath(String path) {
        File file = new File(path);
        return file.exists() && file.isDirectory();
    }
}
