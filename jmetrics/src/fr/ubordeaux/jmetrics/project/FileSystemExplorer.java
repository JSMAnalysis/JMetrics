package fr.ubordeaux.jmetrics.project;

import java.io.File;
import java.io.FileFilter;
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
        PackageDirectory project = new PackageDirectory(new File(path));
        project.setContent(getRecursiveStructure(new File(path), new ArrayList<>()));
        if (!isValidJavaProject(project)) {
            throw new BadProjectFormatException("Path does not lead to a Compiled Java Project.");
        }
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
        if(node.isFile() && isClassFile(node.getName())){
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
     * Verify that a filename correspond to a class file.
     * @param filename The filename to verify.
     * @return true if the filename is a class file, false otherwise.
     */
    private boolean isClassFile(String filename) {
        return filename.endsWith(CLASS_EXTENSION);
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

    private boolean isValidJavaProject(PackageDirectory project) {
        // TODO: Check that there is at least 1 class file in the project.
        return true;
    }

}
