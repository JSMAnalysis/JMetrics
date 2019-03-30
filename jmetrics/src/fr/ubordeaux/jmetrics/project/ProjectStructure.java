package fr.ubordeaux.jmetrics.project;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides structure (as Singleton) to represent a Java Project.
 */
public class ProjectStructure {

    private ProjectComponent structure;

    private String projectRootPath = "";

    private List<ClassFile> cacheClasses = null;

    private List<PackageDirectory> cachePackages = null;

    private static ProjectStructure ourInstance = new ProjectStructure();

    public static ProjectStructure getInstance() {
        return ourInstance;
    }

    private ProjectStructure() {  }

    public void setStructure(ProjectComponent rootComponent) {
        structure = pruneStructure(rootComponent);
        projectRootPath = rootComponent.getPath();
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

    /**
     * Recursively builds a list of the {@link PackageDirectory} contained in the project.
     * @param tree The current explored component.
     * @param accumulator The list built by previous calls.
     * @return A list containing all the package directories.
     */
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

    /**
     * @return The path to the project's root.
     */
    public String getRootPath() {
        return projectRootPath;
    }

    /**
     * Prune the ProjectComponent's tree from superfluous packages and set component's displayableName.
     * @param rootComponent The root of the tree to clean.
     * @return The cleaned structure.
     */
    private ProjectComponent pruneStructure(ProjectComponent rootComponent) {
        return recursivePruneRoot(rootComponent, "");
    }

    /**
     * Prune the root of a project's component tree from packages that only contains one other package
     * and build the prefix to remove from all its children's displayName.
     * @param root The root of the current tree.
     * @param prefixToRemove The current prefix that needs to be removed from all the current root's children name.
     * @return The cleaned structure.
     */
    private ProjectComponent recursivePruneRoot(ProjectComponent root, String prefixToRemove) {
        if (root instanceof PackageDirectory){
            PackageDirectory rootPackage = (PackageDirectory) root;
            if (rootPackage.getContent().size() > 1) {
                return recursivePruneStructure(rootPackage, prefixToRemove);
            }
            else if (rootPackage.getContent().size() == 1) {
                return recursivePruneRoot(rootPackage.getContent().get(0), rootPackage.getFullyQualifiedName());
            }
        }
        return root;
    }

    /**
     * Prune a ProjectComponent's tree to remove a prefix from all the children's displayName.
     * @param node The root of the current tree.
     * @param prefixToRemove The prefix to remove from the displayName.
     * @return The cleaned structure.
     */
    private ProjectComponent recursivePruneStructure(ProjectComponent node, String prefixToRemove) {
        if (node instanceof PackageDirectory) {
            PackageDirectory dir = (PackageDirectory) node;
            for (ProjectComponent component : dir.getContent()) {
                dir.removeContent(component);
                dir.addContent(recursivePruneStructure(component, prefixToRemove));
            }
        }
        node.setDisplayName(removePrefix(node.getFullyQualifiedName(), prefixToRemove));
        return node;
    }

    /**
     * Remove a prefix from a component's name.
     * @param name The name from which to remove the prefix.
     * @param prefix The prefix to remove from the name.
     * @return The name without the provided prefix.
     */
    private String removePrefix(String name, String prefix) {
        if (prefix.length() != 0 && name.startsWith(prefix)) {
            name = name.substring(prefix.length());
            if (name.startsWith(".")) {
                name = name.substring(1);
            }
        }
        return name;
    }

}
