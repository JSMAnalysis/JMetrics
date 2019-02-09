package fr.ubordeaux.jmetrics.project;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Service that traverses directories to generate Project Structure.
 */
public class FileSystemExplorer {
    private final static String CLASS_EXTENSION = ".class";
    private final static String JAVA_EXTENSION = ".java";

    public ProjectComponent generateStructure(String path) {
        if (!isValidPath(path)) {
            throw new IllegalArgumentException("Path not valid !");
        }
        PackageDirectory project = new PackageDirectory(new File(path));
        project.setContent(getRecursiveStructure(new File(path)));
        if (!isValidJavaProject(project)) {
            throw new WrongProjectFormatException("Not a Java Project");
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
     *
     * @param name A filename.
     * @return True if the filename starts with a dot, False otherwise.
     */
    private boolean validPackageName(String name) {
        return !name.startsWith(".");
    }

    /**
     *
     * @param name A filename.
     * @return True if the filename ends with the .class extension, False otherwise.
     */
    private boolean isClassFile(String name) {
        return name.endsWith(CLASS_EXTENSION);
    }

    /**
     *
     * @param path Path of a file.
     * @return true if the path is valid (lead to a file) and if that is a directory (package), False otherwise.
     */
    private boolean isValidPath(String path) {
        File file = new File(path);
        return file.exists() && file.isDirectory();
    }

    private boolean isValidJavaProject(PackageDirectory project) {
        List<ProjectComponent> content = project.getContent();
        for (ProjectComponent component : content) {
            if (!(component.getName().endsWith(CLASS_EXTENSION) || component.getName().endsWith(JAVA_EXTENSION)))
                return false;
        }
        return true;
    }
}
