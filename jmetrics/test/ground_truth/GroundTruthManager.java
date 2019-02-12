package ground_truth;

import fr.ubordeaux.jmetrics.analysis.DependencyType;
import fr.ubordeaux.jmetrics.project.FileSystemExplorer;
import fr.ubordeaux.jmetrics.project.ProjectComponent;
import fr.ubordeaux.jmetrics.project.ProjectStructure;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides utils function to manage and access the Ground Truth repository.
 * RQ : JavaCompiler generate automatically 2 methods for Enum : values and valueOf
 */
public class GroundTruthManager {

    /**
     * Path to the compiled GroundTruth projects.
     */
    static final String groundTruthPath = "out/test/ground_truth/";

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

        String CLASS_AIRPLANE = "Airplane.class";
        String CLASS_CAR = "Car.class";
        String CLASS_MAIN = "Main.class";
        String CLASS_MATERIAL = "Material.class";
        String CLASS_VEHICLE = "Vehicle.class";
        String CLASS_WHEEL = "Wheel.class";

        Example1.addClass(CLASS_AIRPLANE,
                new ClassInformation(1, 0, 0, 1, 1, 0, 0)
        );
        Example1.addClass(CLASS_CAR,
                new ClassInformation(1, 0, 0, 1, 1, 0, 0)
        );
        Example1.addClass(CLASS_MAIN,
                new ClassInformation(1, 0, 0, 0, 0, 0, 1)
        );
        Example1.addClass(CLASS_MATERIAL,
                new ClassInformation(2, 0, 1, 0, 0, 0, 1)
        );
        Example1.addClass(CLASS_VEHICLE,
                new ClassInformation(1, 1, 2, 1, 0.33, 0.33, 0.33)
        );
        Example1.addClass(CLASS_WHEEL,
                new ClassInformation(2, 0, 1, 1, 0.5, 0, 0.5)
        );

        Example1.addDependency(CLASS_AIRPLANE,    CLASS_VEHICLE,    DependencyType.Inheritance);
        Example1.addDependency(CLASS_CAR,         CLASS_VEHICLE,    DependencyType.Inheritance);
        Example1.addDependency(CLASS_WHEEL,       CLASS_MATERIAL,   DependencyType.Signature);
        Example1.addDependency(CLASS_WHEEL,       CLASS_MATERIAL,   DependencyType.Aggregation);
        // TODO: Dependency (through Generic Types) to add :
        //  (CLASS_VEHICLE, CLASS_WHEEL, DependencyType.Aggregation)

        projects.add(Example1);
    }

    private void setupExample2() {
        Project Example2 = new Project("example2/", 11, 2);

        String CLASS_BASE_PIZZA = "domain/kitchen/BasePizza.class";
        String CLASS_PASTA_TYPE = "domain/kitchen/PastaType.class";
        String CLASS_PIZZA_SIZE = "domain/kitchen/PizzaSize.class";
        String CLASS_PIZZA = "domain/kitchen/Pizza.class";
        String CLASS_MAIN = "Main.class";
        String CLASS_INGREDIENT = "domain/kitchen/ingredients/Ingredient.class";
        String CLASS_TOMATO = "domain/kitchen/ingredients/Tomato.class";
        String CLASS_PICKLES = "domain/kitchen/ingredients/Pickles.class";
        String CLASS_CUSTOMER = "store/Customer.class";
        String CLASS_PIZZAIOLO = "store/Pizzaiolo.class";
        String CLASS_PIZZERIA = "store/Pizzeria.class";

        Example2.addClass(CLASS_BASE_PIZZA,
                new ClassInformation(2, 0, 1, 0, 0, 0, 1)
        );
        Example2.addClass(CLASS_PASTA_TYPE,
                new ClassInformation(2, 0, 1, 0, 0, 0, 1)
        );
        Example2.addClass(CLASS_PIZZA_SIZE,
                new ClassInformation(2, 0, 1, 0, 0, 0, 1)
        );
        Example2.addClass(CLASS_PIZZA,
                new ClassInformation(7, 0, 1, 1, 0.5, 0, 0.5)
        );
        Example2.addClass(CLASS_MAIN,
                new ClassInformation(1, 0, 0, 0, 0, 0, 1)
        );
        Example2.addClass(CLASS_INGREDIENT,
                new ClassInformation(1, 1, 2, 1, 0.33, 0.33, 0.33)
        );
        Example2.addClass(CLASS_TOMATO,
                new ClassInformation(1, 0, 0, 1, 1, 0, 0)
        );
        Example2.addClass(CLASS_PICKLES,
                new ClassInformation(1, 0, 0, 1, 1, 0, 0)
        );
        Example2.addClass(CLASS_CUSTOMER,
                new ClassInformation(1, 0, 1, 1, 0.5, 0, 0.5)
        );
        Example2.addClass(CLASS_PIZZAIOLO,
                new ClassInformation(1, 0, 1, 1, 0.5, 0, 0.5)
        );
        Example2.addClass(CLASS_PIZZERIA,
                new ClassInformation(1, 0, 0, 2, 1, 0, 0)
        );

        Example2.addDependency(CLASS_TOMATO,        CLASS_INGREDIENT,       DependencyType.Inheritance);
        Example2.addDependency(CLASS_PICKLES,       CLASS_INGREDIENT,       DependencyType.Inheritance);
        Example2.addDependency(CLASS_PIZZA,         CLASS_INGREDIENT,       DependencyType.Signature);
        Example2.addDependency(CLASS_CUSTOMER,      CLASS_PIZZA,            DependencyType.Signature);
        Example2.addDependency(CLASS_PIZZAIOLO,     CLASS_PIZZA,           DependencyType.Signature);
        Example2.addDependency(CLASS_PIZZA,         CLASS_BASE_PIZZA,       DependencyType.Signature);
        Example2.addDependency(CLASS_PIZZA,         CLASS_BASE_PIZZA,       DependencyType.Aggregation);
        Example2.addDependency(CLASS_PIZZA,         CLASS_PASTA_TYPE,       DependencyType.Signature);
        Example2.addDependency(CLASS_PIZZA,         CLASS_PASTA_TYPE,       DependencyType.Aggregation);
        Example2.addDependency(CLASS_PIZZA,         CLASS_PIZZA_SIZE,       DependencyType.Signature);
        Example2.addDependency(CLASS_PIZZA,         CLASS_PIZZA_SIZE,       DependencyType.Aggregation);
        //Example2.addDependency(CLASS_PIZZA,         CLASS_CUSTOMER,         DependencyType.Aggregation);
        //Example2.addDependency(CLASS_PIZZA,         CLASS_CUSTOMER,         DependencyType.Signature);
        //Example2.addDependency(CLASS_PIZZA,         CLASS_PIZZAIOLO,        DependencyType.Aggregation);
        //Example2.addDependency(CLASS_PIZZA,         CLASS_PIZZAIOLO,        DependencyType.Signature);
        //Example2.addDependency(CLASS_PIZZERIA,      CLASS_PIZZAIOLO,        DependencyType.Aggregation);
        //Example2.addDependency(CLASS_PIZZERIA,      CLASS_CUSTOMER,         DependencyType.Aggregation);
        //Example2.addDependency(CLASS_INGREDIENT,    CLASS_PIZZA,            DependencyType.Aggregation);

        projects.add(Example2);
    }

}
