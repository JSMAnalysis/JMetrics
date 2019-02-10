package ground_truth;

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

    public static final String groundTruthPath = "out/test/classes/ground_truth/";
    public final String invalidGroundTruthPath = "pompadour/42/";


    public GroundTruthManager() {
        projects = new ArrayList<>();
        setUpProjects();
    }

    private void setUpProjects() {
        Project Example1 = new Project("example1/");
        Example1.setNumberOfClass(6);
        Example1.setNumberOfPackage(0);
        projects.add(Example1);
        // Add Example2; etc.
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
        Project p = projects.get(projectNumber - 1);
        String path = groundTruthPath + p.getDirectory();
        ProjectComponent rootComponent = explorer.generateStructure(path);
        ProjectStructure.getInstance().setStructure(rootComponent);
    }

}
