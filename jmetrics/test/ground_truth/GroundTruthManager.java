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

    static final String exampleTwoPath = "domain/kitchen/";

    /**
     * Number of projects in the Ground Truth.
     */
    public static final int groundTruthSize = 2;

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
        // RQ : Material is an Enum. JavaCompiler generate automatically 2 methods for Enum : values and valueOf
        Example1.addClass("Material.class",
                new ClassInformation(2, 0, 1, 0, 0, 0, 1)
        );
        Example1.addClass("Vehicle.class",
                new ClassInformation(1, 1, 2, 1, 0.33, 0.33, 0.33)
        );
        Example1.addClass("Wheel.class",
                new ClassInformation(2, 0, 1, 1, 0.5, 0, 0.5)
        );

        Example1.addDependency("Airplane.class",    "Vehicle.class",    DependencyType.Inheritance);
        Example1.addDependency("Car.class",         "Vehicle.class",    DependencyType.Inheritance);
        Example1.addDependency("Wheel.class",       "Material.class",   DependencyType.Signature);
        Example1.addDependency("Wheel.class",       "Material.class",   DependencyType.Aggregation);
        // TODO: Dependency (through Generic Types) to add :
        //  ("Vehicle.class", "Wheel.class", DependencyType.Aggregation)

        projects.add(Example1);
    }

    private void setupExample2() {
        Project Example2 = new Project("example2/", 11, 2);

        // kitchen package
        Example2.addClass(exampleTwoPath + "BasePizza.class",
                new ClassInformation(2, 0, 1, 0, 0, 0, 1)
        );
        Example2.addClass(exampleTwoPath + "PastaType.class",
                new ClassInformation(2, 0, 1, 0, 0, 0, 1)
        );
        Example2.addClass(exampleTwoPath + "PizzaSize.class",
                new ClassInformation(2, 0, 1, 0, 0, 0, 1)
        );

        Example2.addClass(exampleTwoPath + "Pizza.class",
                new ClassInformation(1, 0, 1, 1, 0.5, 0, 0.5)
        );
        Example2.addClass("domain/Main.class",
                new ClassInformation(1, 0, 0, 0, 0, 0, 1)
        );

        // kitchen/ingredients package
        Example2.addClass(exampleTwoPath + "ingredients/Ingredient.class",
                new ClassInformation(1, 1, 2, 1, 0.33, 0.33, 0.33)
        );
        Example2.addClass(exampleTwoPath + "ingredients/Tomato.class",
                new ClassInformation(1, 0, 0, 1, 1, 0, 0)
        );
        Example2.addClass(exampleTwoPath + "ingredients/Pickles.class",
                new ClassInformation(1, 0, 0, 1, 1, 0, 0)
        );

        // store package
        Example2.addClass("store/Customer.class",
                new ClassInformation(1, 0, 1, 1, 0.5, 0, 0.5)
        );
        Example2.addClass("store/Pizzaiolo.class",
                new ClassInformation(1, 0, 1, 1, 0.5, 0, 0.5)
        );
        Example2.addClass("store/Pizzaria.class",
                new ClassInformation(1, 0, 0, 2, 1, 0, 0)
        );

        Example2.addDependency(exampleTwoPath + "ingredients/Tomato.class",exampleTwoPath + "ingredients/Ingredient.class", DependencyType.Inheritance);
        Example2.addDependency(exampleTwoPath + "ingredients/Pickles.class",exampleTwoPath + "ingredients/Ingredient.class", DependencyType.Inheritance);
        Example2.addDependency(exampleTwoPath + "ingredients/Ingredient.class",exampleTwoPath + "Pizza.class",DependencyType.Aggregation);
        Example2.addDependency(exampleTwoPath + "Pizza.class","store/Customer.class", DependencyType.Aggregation);
        Example2.addDependency(exampleTwoPath + "Pizza.class","store/Customer.class", DependencyType.Signature);
        Example2.addDependency(exampleTwoPath + "Pizza.class","store/Pizzaiolo.class", DependencyType.Aggregation);
        Example2.addDependency(exampleTwoPath + "Pizza.class","store/Pizzaiolo.class", DependencyType.Signature);
        Example2.addDependency("store/Pizzaria.class","store/Pizzaiolo.class", DependencyType.Aggregation);
        Example2.addDependency("store/Pizzaria.class","store/Customer.class", DependencyType.Aggregation);
        Example2.addDependency(exampleTwoPath + "Pizza.class",exampleTwoPath + "BasePizza.class",   DependencyType.Signature);
        Example2.addDependency(exampleTwoPath + "Pizza.class",exampleTwoPath + "BasePizza.class",   DependencyType.Aggregation);
        Example2.addDependency(exampleTwoPath + "Pizza.class",exampleTwoPath + "PastaType.class",   DependencyType.Signature);
        Example2.addDependency(exampleTwoPath + "Pizza.class",exampleTwoPath + "PastaType.class",   DependencyType.Aggregation);
        Example2.addDependency(exampleTwoPath + "Pizza.class",exampleTwoPath + "PizzaSize.class",   DependencyType.Signature);
        Example2.addDependency(exampleTwoPath + "Pizza.class",exampleTwoPath + "PizzaSize.class",   DependencyType.Aggregation);

        projects.add(Example2);
    }

}
