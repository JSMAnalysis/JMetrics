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

    private List<Project> projects;

    public static final String groundTruthPath = "out/test/ground_truth/";
    public final String invalidGroundTruthPath = "pompadour/42/";


    public GroundTruthManager() {
        projects = new ArrayList<>();
        setupProjects();
    }

    private void setupProjects() {
        setupExample1();
    }

    public Project getProject(int index) {
        return projects.get(index);
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

    private void setupExample1() {
        Project Example1 = new Project("example1/");
        Example1.setNumberOfClass(6);
        Example1.setNumberOfPackage(0);

        Example1.addClass("Airplane.class", 1,  0);
        Example1.addClass("Car.class",      1,  0);
        Example1.addClass("Main.class",     1,  0);
        Example1.addClass("Material.class", 0,  0);
        Example1.addClass("Vehicle.class",  1,  1);
        Example1.addClass("Wheel.class",    2,  0);

        Example1.addDependency("Airplane.class",    "Vehicle.class",    DependencyType.Inheritance);
        Example1.addDependency("Car.class",         "Vehicle.class",    DependencyType.Inheritance);
        Example1.addDependency("Vehicle.class",     "Wheel.class",      DependencyType.Signature);
        Example1.addDependency("Wheel.class",       "Material.class",   DependencyType.Signature);
        Example1.addDependency("Wheel.class",       "Material.class",   DependencyType.Aggregation);
        // TODO: Dependency to add (Genericity) : ("Vehicle.class", "Wheel.class", DependencyType.Aggregation)

        projects.add(Example1);
    }

}
