package fr.ubordeaux.jmetrics.project;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides structure (as Singleton) to represent a Java Project.
 */
public class ProjectStructure {

    private ProjectComponent structure;

    private List<ClassFile> cacheClasses = null;

    private List<PackageDirectory> cachePackages = null;

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
     * @return The searched Class File if exists, null otherwise.
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
     * Get Package Directory by name.
     * @param name The name of the searched PackageDirectory.
     * @return The searched Package Directory if exists, null otherwise.
     */
    public PackageDirectory getPackageDirectory(String name) {
        for (PackageDirectory directory: getPackages()) {
            if (directory.getName().equals(name)) {
                return directory;
            }
        }
        return null;
    }

    /**
     * Delete the cache attributes.
     */
    public void cacheClear() {
        this.cacheClasses = null;
        this.cachePackages = null;
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
        if (tree instanceof ClassFile) {
            ClassFile file = (ClassFile) tree;
            accumulator.add(file);
        } else {
            PackageDirectory directory = (PackageDirectory) tree;
            List<ProjectComponent> content = directory.getContent();
            for (ProjectComponent component : content) {
                accumulator = recursiveEnumerateClasses(component, accumulator);
            }
        }
        return accumulator;
    }

    /**
     * @return List of package directory that compose the project.
     */
    public List<PackageDirectory> getPackages() {
        if (this.cachePackages == null)
            this.cachePackages = recursiveEnumeratePackages(structure, new ArrayList<>());
        return this.cachePackages;
    }

    private List<PackageDirectory> recursiveEnumeratePackages(ProjectComponent tree, List<PackageDirectory> accumulator) {
        if (tree instanceof PackageDirectory) {
            PackageDirectory dir = (PackageDirectory) tree;
            accumulator.add(dir);
            for (ProjectComponent component : dir.getContent()) {
                accumulator = recursiveEnumeratePackages(component, accumulator);
            }
        }
        return accumulator;
    }

    public String getRootPath(){
        return (structure != null) ? structure.getPath() : "";
    }

}
