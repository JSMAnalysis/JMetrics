package ground_truth;

import fr.ubordeaux.jmetrics.analysis.DependencyType;
import fr.ubordeaux.jmetrics.project.FileSystemExplorer;
import fr.ubordeaux.jmetrics.project.ProjectComponent;
import fr.ubordeaux.jmetrics.project.ProjectStructure;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides utils function to manage and access the Ground Truth repository.
 */
public class GroundTruthManager {

    /**
     * Path to the compiled GroundTruth projects.
     */
    static final String groundTruthPath = "out/test/ground_truth/";

    /**
     * List of the project inside the GroundTruth.
     */
    private List<Project> projects;

    public GroundTruthManager() {
        projects = new ArrayList<>();
        setupExample1();
        setupExample2();
    }

    /**
     * Load given example project in the Project Structure.
     * @param projectNumber The id of the project to load.
     */
    public void loadExample(int projectNumber) {
        FileSystemExplorer explorer = new FileSystemExplorer();
        Project p = getProject(projectNumber);
        String path = groundTruthPath + p.getDirectory();
        ProjectComponent rootComponent = explorer.generateStructure(path);
        ProjectStructure.getInstance().setStructure(rootComponent);
    }

    public Project getProject(int projectNumber) {
        return projects.get(projectNumber - 1);
    }

    private void setupExample1() {
        Project Example1 = new Project("example1/", 6, 0);

        Example1.addClass("Airplane.class",
                new ClassInformation(1, 0, 0, 1, 1, 0, 0)
        );
        Example1.addClass("Car.class",
                new ClassInformation(1, 0, 0, 1, 1, 0, 0)
        );
        Example1.addClass("Main.class",
                new ClassInformation(1, 0, 0, 0, 0, 0, 1)
        );
        Example1.addClass("Material.class",
                new ClassInformation(0, 0, 1, 0, 0, 0, 1)
        );
        Example1.addClass("Vehicle.class",
                new ClassInformation(1, 1, 2, 1, 0.33, 0.33, 0.33)
        );
        Example1.addClass("Wheel.class",
                new ClassInformation(2, 0, 1, 1, 0.5, 0, 0.5)
        );

        Example1.addDependency("Airplane.class",    "Vehicle.class",    DependencyType.Inheritance);
        Example1.addDependency("Car.class",         "Vehicle.class",    DependencyType.Inheritance);
        Example1.addDependency("Vehicle.class",     "Wheel.class",      DependencyType.Signature);
        Example1.addDependency("Wheel.class",       "Material.class",   DependencyType.Signature);
        Example1.addDependency("Wheel.class",       "Material.class",   DependencyType.Aggregation);
        // TODO: Dependency to add (Genericity) : ("Vehicle.class", "Wheel.class", DependencyType.Aggregation)

        projects.add(Example1);
    }

    private void setupExample2() {
        Project Example2 = new Project("example2/", 0, 0);
        projects.add(Example2);
    }

}
