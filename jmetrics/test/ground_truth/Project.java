package ground_truth;

import fr.ubordeaux.jmetrics.analysis.Dependency;
import fr.ubordeaux.jmetrics.analysis.DependencyType;
import fr.ubordeaux.jmetrics.metrics.ClassCategory;
import fr.ubordeaux.jmetrics.metrics.ElementaryCategory;
import fr.ubordeaux.jmetrics.project.ClassFile;

import java.io.File;
import java.util.*;

public class Project {

    private String directory;
    private int numberOfClasses;
    private int numberOfPackages;

    /**
     * Map that stores classes of the example project associated to a tuple (nbMethod, nbAbstractMethod)
     */
    private Map<ClassCategory, MethodCounter> classes;
    private List<Dependency> dependencies;

    Project (String directory) {
        this.directory = directory;
        this.classes = new HashMap<>();
        this.dependencies = new ArrayList<>();
    }

    public void setNumberOfClass(int numberOfClasses) {
        this.numberOfClasses = numberOfClasses;
    }

    public void setNumberOfPackage(int numberOfPackages) {
        this.numberOfPackages = numberOfPackages;
    }

    public String getDirectory() {
        return directory;
    }

    public int getNumberOfClasses() {
        return this.numberOfClasses;
    }

    public int getNumberOfPackages() {
        return this.numberOfPackages;
    }

    public String getPath() {
        return GroundTruthManager.groundTruthPath + directory;
    }

    public HashMap<ClassCategory, MethodCounter> getClasses() {
        return new HashMap<>(classes);
    }

    public void addClass(String className, int numberOfMethod, int numberOfAbstractMethod) {
        ClassCategory category = getClassCategory(className);
        MethodCounter counter = new MethodCounter(numberOfMethod, numberOfAbstractMethod);
        classes.put(category, counter);
    }

    public List<Dependency> getDependencies() {
        return new ArrayList<>(dependencies);
    }

    public void addDependency(String srcName, String dstName, DependencyType type) {
        dependencies.add(new Dependency(getClassCategory(srcName), getClassCategory(dstName), type));
    }

    class MethodCounter {
        private int numberOfMethod;
        private int numberOfAbstractMethod;
        MethodCounter(int numberOfMethod, int numberOfAbstractMethod) {
            this.numberOfMethod = numberOfMethod;
            this.numberOfAbstractMethod = numberOfAbstractMethod;
        }
        public int getNumberOfMethod() { return numberOfMethod; }
        public int getNumberOfAbstractMethod() { return numberOfAbstractMethod; }
    }

    private ClassCategory getClassCategory(String className) {
        String path = GroundTruthManager.groundTruthPath + directory + className;
        ClassFile file = new ClassFile(new File(path));
        return new ElementaryCategory(file);
    }

}
