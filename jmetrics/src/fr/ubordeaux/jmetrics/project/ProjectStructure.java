package fr.ubordeaux.jmetrics.project;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides structure (as Singleton) to represent a Java Project.
 */
public class ProjectStructure {

    private ProjectComponent structure;

    private List<ClassFile> cacheClasses = null;

    private static ProjectStructure ourInstance = new ProjectStructure();

    public static ProjectStructure getInstance() {
        return ourInstance;
    }

    private ProjectStructure() {  }

    public void setStructure(ProjectComponent rootComponent) {
        this.structure = rootComponent;
    }

    /**
     * Get Class File by name.
     * @param name The name of the searched ClassFile.
     * @return The Class File searched if exists, null otherwise.
     */
    public ClassFile getClassFile(String name) {
        for (ClassFile file: getClasses()) {
            if (file.getName().equals(name)) {
                return file;
            }
        }
        return null;
    }

    /**
     * Return the entire set of class files of the project.
     * @return List of class files that compose the project.
     */
    public List<ClassFile> getClasses() {
        if (this.cacheClasses == null)
            this.cacheClasses = recursiveEnumerateClasses(structure, new ArrayList<>());
        return this.cacheClasses;
    }

    /**
     * Enumerate recursively classes inside the project.
     * @param tree The root of the explored sub-tree (initial call: the root component of the ProjectStructure).
     * @param accumulator The accumulator (initial call: Empty List).
     * @return Classes that compose the given tree.
     */
    private List<ClassFile> recursiveEnumerateClasses(ProjectComponent tree, List<ClassFile> accumulator) {
        List<ClassFile> classes = new ArrayList<>(accumulator);
        if (tree instanceof ClassFile) {
            ClassFile file = (ClassFile) tree;
            classes.add(file);
        } else {
            PackageDirectory directory = (PackageDirectory) tree;
            List<ProjectComponent> content = directory.getContent();
            for (ProjectComponent component : content) {
                classes = recursiveEnumerateClasses(component, classes);
            }
        }
        return classes;
    }

    /**
     * TODO: Not yet implemented.
     * @return List of package directory that compose the project.
     */
    public List<PackageDirectory> getPackages() {
        return null;
    }

}
