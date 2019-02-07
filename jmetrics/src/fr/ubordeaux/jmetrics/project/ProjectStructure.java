package fr.ubordeaux.jmetrics.project;

import java.util.List;

/**
 * Provides structure to represent a Java Project.
 */
public class ProjectStructure {

    ProjectComponent structure;

    private static ProjectStructure ourInstance = new ProjectStructure();

    public static ProjectStructure getInstance() {
        return ourInstance;
    }

    private ProjectStructure() { }

    public ClassFile getClass(String name) {
        return null;
    }

    public PackageDirectory getPackage(String name) {
        return null;
    }

    public List<ClassFile> getClasses() { return null; }

    public List<PackageDirectory> getPackages() { return null; }

}
