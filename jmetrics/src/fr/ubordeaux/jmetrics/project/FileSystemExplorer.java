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
            throw new IllegalArgumentException("Path not valid");
        }
        PackageDirectory project = new PackageDirectory(new File(path));
        project.setContent(getRecursiveStructure(new File(path)));
        if (!isValidJavaProject(project)) {
            throw new BadProjectFormatException("Not a Compiled Java Project");
        }
        return project;
    }

    public List<ProjectComponent> getRecursiveStructure(File node) {
        List<ProjectComponent> components = new ArrayList<>();
        File[] files = node.listFiles();
        if (files == null) return components;
        for (File file: files) {
            if (file.isFile() && isClassFile(file.getName())) {
                ClassFile c = new ClassFile(file);
                components.add(c);
            } else if (file.isDirectory()) {
                PackageDirectory p = new PackageDirectory(file);
                p.setContent(getRecursiveStructure(file));
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
        // TODO: Check that there is at least 2 class file in the project.
        return true;
    }

}
