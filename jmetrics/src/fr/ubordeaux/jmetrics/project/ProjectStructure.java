package fr.ubordeaux.jmetrics.project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
        structure = rootComponent;
        pruneStructure();
    }

    /**
     * Delete the cache attributes.
     */
    public void cacheClear() {
        cacheClasses = null;
        cachePackages = null;
    }

    /**
     * Return the entire set of class files of the project.
     * @return List of class files that compose the project.
     */
    public List<ClassFile> getClasses() {
        if (cacheClasses == null)
            cacheClasses = recursiveEnumerateClasses(structure, new ArrayList<>());
        return cacheClasses;
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
        if (cachePackages == null)
            cachePackages = recursiveEnumeratePackages(structure, new ArrayList<>());
        return cachePackages;
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

    /**
     * Prune the ProjectStructure from superfluous packages and reduce components name.
     */
    private void pruneStructure() {

        // Remove empty packages (packageList is sorted by depth desc)
        List<PackageDirectory> packageList = recursiveEnumeratePackages(structure, new ArrayList<>());
        packageList = packageList.stream()
                .sorted(Comparator.comparing(PackageDirectory::getDepth).reversed())
                .collect(Collectors.toList());
        for (PackageDirectory dir: packageList) {
            if (dir.getContent().isEmpty()) {
                PackageDirectory parent = getParentComponent(packageList, dir);
                if (parent != null) { parent.removeContent(dir); }
            }
        }

        // Remove top tree useless directories (packageList is now sorted by depth asc)
        Collections.reverse(packageList);
        int removedDepth = 0;
        ProjectComponent newRootComponent = null;
        List<ProjectComponent> content;
        for (PackageDirectory dir: packageList) {
            content = dir.getContent();
            if (content.size() == 1 && content.get(0) instanceof PackageDirectory) {
                removedDepth++;
                newRootComponent = content.get(0);
            }
        }
        if (newRootComponent != null) {
            structure = newRootComponent;
            for (PackageDirectory dir: packageList) {
                dir.setDepth(dir.getDepth() - removedDepth);
            }
        }

        // Rename Components
        /*List<ClassFile> classes = recursiveEnumerateClasses(structure, new ArrayList<>());
        List<PackageDirectory> packages = recursiveEnumeratePackages(structure, new ArrayList<>());
        ArrayList<ProjectComponent> componentsList = new ArrayList<>(classes);
        componentsList.addAll(packages);
        ArrayList<String> componentNames = componentsList.stream()
                .map(ProjectComponent::getName)
                .collect(Collectors.toCollection(ArrayList::new));
        String first = componentNames.get(0);
        String[] splitFirst = first.split("\\.");
        StringBuilder prefixBuild = new StringBuilder();
        for (int i = 0; i < removedDepth; i++) {
            prefixBuild.append(splitFirst[i]);
            prefixBuild.append(".");
        }
        String prefix = prefixBuild.toString();
        if (!prefix.equals("")) {
            for (ProjectComponent c: componentsList) {
                c.setName(c.getName().split(prefix)[1]);
            }
        }*/

    }

    private PackageDirectory getParentComponent(List<PackageDirectory> packages, ProjectComponent comp) {
        for (PackageDirectory packageG: packages) {
            for (ProjectComponent innerG: packageG.getContent()) {
                if (innerG.equals(comp)) {
                    return packageG;
                }
            }
        }
        return null;
    }

}
