package ground_truth;

import fr.ubordeaux.jmetrics.project.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Provides utils function to manage and access the Ground Truth repository.
 */
public class GroundTruthManager {

    /**
     * Path to the compiled GroundTruth projects.
     */
    static final String GROUND_TRUTH_BYTECODE_PATH = "out/test/ground_truth/";
    /**
     * Path to the GroundTruth projects sources.
     */
    static final String GROUND_TRUTH_SOURCE_PATH = "test/ground_truth/";

    /**
     * Path to the compiled tests root.
     */
    private static final String TEST_BYTECODE_PATH = "out/test";
    /**
     * Path to the tests sources root.
     */
    private static final String TEST_SOURCE_PATH = "test";

    /**
     * Number of projects in the Ground Truth.
     */
    public static final int groundTruthSize = 3;

    /**
     * List of projects in the GroundTruth.
     */
    private List<Project> projects;

    public GroundTruthManager() {
        projects = new ArrayList<>();
        setupExample1();
        setupExample2();
        setupExample3();
    }

    /**
     * Load given example project in the Project Structure using a provided explorer, with a given rootPath.
     * @param projectNumber The id of the project to load.
     */
    private void loadExample(int projectNumber, FileSystemExplorer explorer, String rootPath) {
        Project p = getProject(projectNumber);
        ProjectComponent rootComponent = explorer.generateStructure(
                rootPath,
                rootPath.equals(TEST_SOURCE_PATH) ? p.getSourcePath() : p.getBytecodePath()
        );
        ProjectStructure.getInstance().setStructure(rootComponent);
    }

    /**
     * Load given example project in the Project Structure from bytecode.
     * @param projectNumber The id of the project to load.
     */
    public void loadExampleBytecode(int projectNumber) {
        loadExample(projectNumber, new BytecodeFileSystemExplorer(), TEST_BYTECODE_PATH);
    }

    /**
     * Load given example project in the Project Structure from source code.
     * @param projectNumber The id of the project to load.
     */
    public void loadExampleSourcecode(int projectNumber) {
        loadExample(projectNumber, new SourceFileSystemExplorer(), TEST_SOURCE_PATH);
    }

    public Project getProject(int projectNumber) {
        ProjectStructure.getInstance().cacheClear();
        return projects.get(projectNumber - 1);
    }

    /**
     * Reads the {@link ClassInfo} annotations on a test project and adds the corresponding data in the project object.
     * @param classNames An array containing the name of all project's classes.
     * @param project The project which the classes are located in.
     */
    private void readClassinfoAnnotations(String[] classNames, Project project) {
        for (String className : classNames) {
            try {
                Class<?> c = Class.forName(className);
                ClassInfo info = c.getAnnotation(ClassInfo.class);
                if(info != null) {
                    project.addClass(className,
                            new ClassInformation(info.numberOfMethod(), info.numberOfAbstractMethod(),
                                    info.Ca(), info.Ce(), info.I(), info.A(), info.Dn())
                    );
                }
            } catch(ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }

    }

    /**
     * Reads the {@link Dependency} annotations on a test project and adds the corresponding data in the project object.
     * @param classNames An array containing the name of all project's classes.
     * @param project The project which the classes are located in.
     */
    private void readDependencyAnnotations(String[] classNames, Project project) {
        for (String className : classNames) {
            try {
                Class<?> c = Class.forName(className);
                List<Dependency> annotations = new ArrayList<>(Arrays.asList(c.getAnnotationsByType(Dependency.class)));
                // Get all annotations on declared fields
                for (Field f : c.getDeclaredFields()) {
                    annotations.addAll(Arrays.asList(f.getAnnotationsByType(Dependency.class)));
                }
                // Get all annotations on declared methods
                for (Method m : c.getDeclaredMethods()) {
                    annotations.addAll(Arrays.asList(m.getAnnotationsByType(Dependency.class)));
                }
                for (Dependency d : annotations) {
                    project.addDependency(className, d.dependencyTo().getName(), d.type(), d.sourceOnly());
                }
            } catch(ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }

    }

    private void setupExample1() {
        Project Example1 = new Project("example1/", 6, 1);
        String[] classNames = new String[]{
                "ground_truth.example1.Airplane",
                "ground_truth.example1.Car",
                "ground_truth.example1.Main",
                "ground_truth.example1.Material",
                "ground_truth.example1.Vehicle",
                "ground_truth.example1.Wheel"
        };
        readClassinfoAnnotations(classNames, Example1);
        readDependencyAnnotations(classNames, Example1);
        projects.add(Example1);
    }

    private void setupExample2() {
        Project Example2 = new Project("example2/", 11, 4);
        String[] classNames = new String[]{
                "ground_truth.example2.kitchen.BasePizza",
                "ground_truth.example2.kitchen.PastaType",
                "ground_truth.example2.kitchen.PizzaSize",
                "ground_truth.example2.kitchen.Pizza",
                "ground_truth.example2.Main",
                "ground_truth.example2.kitchen.ingredients.Ingredient",
                "ground_truth.example2.kitchen.ingredients.Tomato",
                "ground_truth.example2.kitchen.ingredients.Pickles",
                "ground_truth.example2.store.Customer",
                "ground_truth.example2.store.Pizzaiolo",
                "ground_truth.example2.store.Pizzeria"
        };
        readClassinfoAnnotations(classNames, Example2);
        readDependencyAnnotations(classNames, Example2);
        projects.add(Example2);
    }

    private void setupExample3() {
        Project Example3 = new Project("example3/", 7, 1);
        String[] classNames = new String[]{
                "ground_truth.example3.A",
                "ground_truth.example3.B",
                "ground_truth.example3.C",
                "ground_truth.example3.D",
                "ground_truth.example3.E",
                "ground_truth.example3.F",
                "ground_truth.example3.G"
        };
        readClassinfoAnnotations(classNames, Example3);
        readDependencyAnnotations(classNames, Example3);
        projects.add(Example3);
    }

}
