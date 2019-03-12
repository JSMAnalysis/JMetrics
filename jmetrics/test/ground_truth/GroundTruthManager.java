package ground_truth;

import fr.ubordeaux.jmetrics.analysis.DependencyType;
import fr.ubordeaux.jmetrics.project.BytecodeFileSystemExplorer;
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
     * Number of projects in the Ground Truth.
     */
    public static final int groundTruthSize = 2;

    /**
     * List of projects in the GroundTruth.
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
        BytecodeFileSystemExplorer explorer = new BytecodeFileSystemExplorer();
        Project p = getProject(projectNumber);
        ProjectComponent rootComponent = explorer.generateStructure(p.getPath());
        ProjectStructure.getInstance().setStructure(rootComponent);
    }

    public Project getProject(int projectNumber) {
        ProjectStructure.getInstance().cacheClear();
        return projects.get(projectNumber - 1);
    }

    private void setupExample1() {
        Project Example1 = new Project("example1/", 6, 1);

        String CLASS_AIRPLANE = "example1.Airplane";
        String CLASS_CAR = "example1.Car";
        String CLASS_MAIN = "example1.Main";
        String CLASS_MATERIAL = "example1.Material";
        String CLASS_VEHICLE = "example1.Vehicle";
        String CLASS_WHEEL = "example1.Wheel";

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
        Example1.addDependency(CLASS_WHEEL,       CLASS_MATERIAL,   DependencyType.UseLink);
        Example1.addDependency(CLASS_WHEEL,       CLASS_MATERIAL,   DependencyType.Aggregation);
        // TODO: Dependency (through Generic Types) to add :
        //  (CLASS_VEHICLE, CLASS_WHEEL, DependencyType.Aggregation)

        projects.add(Example1);
    }

    private void setupExample2() {
        Project Example2 = new Project("example2/", 11, 4);

        String CLASS_BASE_PIZZA = "example2.kitchen.BasePizza";
        String CLASS_PASTA_TYPE = "example2.kitchen.PastaType";
        String CLASS_PIZZA_SIZE = "example2.kitchen.PizzaSize";
        String CLASS_PIZZA = "example2.kitchen.Pizza";
        String CLASS_MAIN = "example2.Main";
        String CLASS_INGREDIENT = "example2.kitchen.ingredients.Ingredient";
        String CLASS_TOMATO = "example2.kitchen.ingredients.Tomato";
        String CLASS_PICKLES = "example2.kitchen.ingredients.Pickles";
        String CLASS_CUSTOMER = "example2.store.Customer";
        String CLASS_PIZZAIOLO = "example2.store.Pizzaiolo";
        String CLASS_PIZZERIA = "example2.store.Pizzeria";

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
        Example2.addDependency(CLASS_PIZZA,         CLASS_INGREDIENT,       DependencyType.UseLink);
        Example2.addDependency(CLASS_CUSTOMER,      CLASS_PIZZA,            DependencyType.UseLink);
        Example2.addDependency(CLASS_PIZZAIOLO,     CLASS_PIZZA,            DependencyType.UseLink);
        Example2.addDependency(CLASS_PIZZA,         CLASS_BASE_PIZZA,       DependencyType.UseLink);
        Example2.addDependency(CLASS_PIZZA,         CLASS_BASE_PIZZA,       DependencyType.Aggregation);
        Example2.addDependency(CLASS_PIZZA,         CLASS_PASTA_TYPE,       DependencyType.UseLink);
        Example2.addDependency(CLASS_PIZZA,         CLASS_PASTA_TYPE,       DependencyType.Aggregation);
        Example2.addDependency(CLASS_PIZZA,         CLASS_PIZZA_SIZE,       DependencyType.UseLink);
        Example2.addDependency(CLASS_PIZZA,         CLASS_PIZZA_SIZE,       DependencyType.Aggregation);
        // TODO: Dependency (through Generic Types) to add :
        //  (CLASS_PIZZAIOLO,   CLASS_PIZZA,        DependencyType.Aggregation)
        //  (CLASS_PIZZA,       CLASS_INGREDIENT,   DependencyType.Aggregation)
        //  (CLASS_CUSTOMER,    CLASS_PIZZA,        DependencyType.Aggregation)
        //  (CLASS_PIZZERIA,    CLASS_CUSTOMER,     DependencyType.Aggregation)
        //  (CLASS_PIZZERIA,    CLASS_PIZZAIOLO,    DependencyType.Aggregation)

        projects.add(Example2);
    }

}
