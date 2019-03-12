package project;

import fr.ubordeaux.jmetrics.project.*;

import ground_truth.GroundTruthManager;
import ground_truth.Project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class BytecodeFileSystemExplorerTest {

    private BytecodeFileSystemExplorer explorer;
    private GroundTruthManager GT;

    @BeforeEach
    void setUp() {
        explorer = new BytecodeFileSystemExplorer();
        GT = new GroundTruthManager();
    }

    @Test
    void testImportProjectWithCorrectPath() {
        try {
            String validProjectPath = GT.getProject(1).getPath();
            ProjectComponent rootComponent = explorer.generateStructure(validProjectPath);
        } catch(InvalidProjectPathException e) {
            fail("The given path lead to existing directory but InvalidProjectPathException have been thrown.");
        }
    }

    @Test
    void testImportProjectWithWrongPath() {
        assertThrows(InvalidProjectPathException.class, ()->{
            String invalidProjectPath = "pompadour/42/";
            ProjectComponent rootComponent = explorer.generateStructure(invalidProjectPath);
        });
    }

    @Test
    void testClassFileCount() {
        Project example1 = GT.getProject(1);
        ProjectComponent rootComponent = explorer.generateStructure(example1.getPath());
        ProjectStructure.getInstance().setStructure(rootComponent);
        int numberOfClassesInGeneratedStructure = ProjectStructure.getInstance().getClasses().size();
        assertEquals(example1.getNumberOfClasses(), numberOfClassesInGeneratedStructure);
    }

    @Test
    void testPackageDirectoryCount() {
        Project example2 = GT.getProject(2);
        ProjectComponent rootComponent = explorer.generateStructure(example2.getPath());
        ProjectStructure.getInstance().setStructure(rootComponent);
        int numberOfPackagesInGeneratedStructure = ProjectStructure.getInstance().getPackages().size();
        assertEquals(example2.getNumberOfPackages(), numberOfPackagesInGeneratedStructure);
    }

    @Test
    void testValidityOfProjectStructure() {
        GT.loadExample(2);
        ProjectStructure PS = ProjectStructure.getInstance();
        List<String> classesContent, packagesContent;
        PackageDirectory directory;

        directory = PS.getPackageDirectory("example2");
        classesContent = new ArrayList<String>() {{ add("example2.Main"); }};
        packagesContent = new ArrayList<String>() {{ add("example2.kitchen"); add("example2.store"); }};
        assertTrue(isInPackageDirectory(directory, classesContent, packagesContent));

        directory = PS.getPackageDirectory("example2.store");
        classesContent = new ArrayList<String>() {{
            add("example2.store.Customer"); add("example2.store.Pizzaiolo"); add("example2.store.Pizzeria");
        }};
        packagesContent = new ArrayList<>();
        assertTrue(isInPackageDirectory(directory, classesContent, packagesContent));

        directory = PS.getPackageDirectory("example2.kitchen.ingredients");
        classesContent = new ArrayList<String>() {{
            add("example2.kitchen.ingredients.Ingredient"); add("example2.kitchen.ingredients.Pickles"); add("example2.kitchen.ingredients.Tomato");
        }};
        packagesContent = new ArrayList<>();
        assertTrue(isInPackageDirectory(directory, classesContent, packagesContent));

        directory = PS.getPackageDirectory("example2.kitchen");
        packagesContent = new ArrayList<String>() {{ add("example2.kitchen.ingredients"); }};
        classesContent = new ArrayList<String>() {{
            add("example2.kitchen.BasePizza"); add("example2.kitchen.PastaType"); add("example2.kitchen.Pizza"); add("example2.kitchen.PizzaSize");
        }};
        assertTrue(isInPackageDirectory(directory, classesContent, packagesContent));
    }

    /**
     * Verify that a list of file and a list of directories is in a given PackageDirectory.
     * @param directory The PackageDirectory to analyze.
     * @param files The list of files names to verify the presence.
     * @param packages The list of directories names to verify the presence.
     * @return true if all files and directories are sons of the given PackageDirectory, false otherwise.
     */
    private boolean isInPackageDirectory(PackageDirectory directory, List<String> files, List<String> packages) {
        int numberOfProjectComponent = files.size() + packages.size();
        if (numberOfProjectComponent == 0) return true;
        int validComponent = 0;

        boolean valid;
        for (ProjectComponent component: directory.getContent()) {
            valid = component instanceof ClassFile && files.contains(component.getName()) ||
                    component instanceof PackageDirectory && packages.contains(component.getName());
            validComponent += (valid) ? 1 : 0;
        }

        return validComponent == numberOfProjectComponent;
    }

}