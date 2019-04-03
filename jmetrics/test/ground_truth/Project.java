package ground_truth;

import fr.ubordeaux.jmetrics.analysis.Dependency;
import fr.ubordeaux.jmetrics.analysis.DependencyType;
import fr.ubordeaux.jmetrics.metrics.ClassGranule;
import fr.ubordeaux.jmetrics.metrics.Granule;
import fr.ubordeaux.jmetrics.project.ClassFile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private List<Dependency> bytecodeDependencies;
    private List<Dependency> sourceDependencies;

    Project (String directory, int numberOfClasses, int numberOfPackages) {
        this.directory = directory;
        this.numberOfClasses = numberOfClasses;
        this.numberOfPackages = numberOfPackages;
        this.classes = new HashMap<>();
        this.sourceDependencies = new ArrayList<>();
        this.bytecodeDependencies = new ArrayList<>();
    }

    public int getNumberOfClasses() {
        return this.numberOfClasses;
    }

    public int getNumberOfPackages() {
        return this.numberOfPackages;
    }

    /**
     * Get the relative path to the project's source code (concatenate GROUND_TRUTH_SOURCE_PATH with the directory name).
     * @return The path to the project' source code.
     */
    public String getSourcePath() {
        return GroundTruthManager.GROUND_TRUTH_SOURCE_PATH + directory;
    }

    /**
     * Get the relative path to the project's bytecode (concatenate GROUND_TRUTH_BYTECODE_PATH with the directory name).
     * @return The path to the project's bytecode.
     */
    public String getBytecodePath() {
        return GroundTruthManager.GROUND_TRUTH_BYTECODE_PATH + directory;
    }

    public List<Dependency> getBytecodeDependencies() {
        return new ArrayList<>(bytecodeDependencies);
    }

    public List<Dependency> getSourceDependencies(){
        return new ArrayList<>(sourceDependencies);
    }

    public HashMap<ClassFile, ClassInformation> getClasses() {
        return new HashMap<>(classes);
    }

    void addClass(String className, ClassInformation classInfo) {
        ClassFile file = getClassFile(className);
        classes.put(file, classInfo);
    }

    void addDependency(String srcName, String dstName, DependencyType type, boolean sourceOnly) {
        sourceDependencies.add(new Dependency(getClassGranule(srcName), getClassGranule(dstName), type));
        if(!sourceOnly){
            bytecodeDependencies.add(new Dependency(getClassGranule(srcName), getClassGranule(dstName), type));
        }
    }

    private ClassFile getClassFile(String name) {
        String path = getSourcePath() + directory + name;
        return new ClassFile(new File(path), name);
    }

    private Granule getClassGranule(String className) {
        return new ClassGranule(getClassFile(className));
    }

}
