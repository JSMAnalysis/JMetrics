package ground_truth;

import fr.ubordeaux.jmetrics.analysis.Dependency;
import fr.ubordeaux.jmetrics.analysis.DependencyType;
import fr.ubordeaux.jmetrics.metrics.ClassCategory;
import fr.ubordeaux.jmetrics.metrics.ElementaryCategory;
import fr.ubordeaux.jmetrics.project.ClassFile;

import java.io.File;
import java.util.*;

/**
 * Represents an example project in the ground truth.
 */
public class Project {

    private String directory;
    private int numberOfClasses;
    private int numberOfPackages;

    /**
     * Map that stores classes of the project and their associated information.
     */
    private Map<ClassFile, ClassInformation> classes;

    /**
     * List of dependencies of the project.
     */
    private List<Dependency> dependencies;

    Project (String directory, int numberOfClasses, int numberOfPackages) {
        this.directory = directory;
        this.numberOfClasses = numberOfClasses;
        this.numberOfPackages = numberOfPackages;
        this.classes = new HashMap<>();
        this.dependencies = new ArrayList<>();
    }

    public String getDirectory() { return directory; }

    public int getNumberOfClasses() { return this.numberOfClasses; }

    public int getNumberOfPackages() { return this.numberOfPackages; }

    /**
     * Get the relative path to the project (concatenate groundTruthPath with the directory name).
     * @return The path to the project.
     */
    public String getPath() {
        return GroundTruthManager.groundTruthPath + directory;
    }

    public List<Dependency> getDependencies() {
        return new ArrayList<>(dependencies);
    }

    public HashMap<ClassFile, ClassInformation> getClasses() {
        return new HashMap<>(classes);
    }



    public void addClass(String className, ClassInformation classInfo) {
        ClassFile file = getClassFile(className);
        classes.put(file, classInfo);
    }

    public void addDependency(String srcName, String dstName, DependencyType type) {
        dependencies.add(new Dependency(getClassCategory(srcName), getClassCategory(dstName), type));
    }



    public ClassFile getClassFile(String name) {
        String path = getPath() + directory + name;
        return new ClassFile(new File(path));
    }

    public ClassCategory getClassCategory(String className) {
        return new ElementaryCategory(getClassFile(className));
    }

}
