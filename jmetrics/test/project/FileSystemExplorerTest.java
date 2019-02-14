package project;

import fr.ubordeaux.jmetrics.project.*;

import ground_truth.GroundTruthManager;
import ground_truth.Project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class FileSystemExplorerTest {

    private FileSystemExplorer explorer;
    private GroundTruthManager GT;

    @BeforeEach
    void setUp() {
        explorer = new FileSystemExplorer();
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
        List<ClassFile> classesContent = new ArrayList<>();
        List<PackageDirectory> packagesContent = new ArrayList<>();
        PackageDirectory directory;


        directory = PS.getPackageDirectory("example2");
        classesContent.add(PS.getClassFile("Main.class"));
        packagesContent.add(PS.getPackageDirectory("kitchen"));
        packagesContent.add(PS.getPackageDirectory("store"));
        assertTrue(isInPackageDirectory(directory, classesContent, packagesContent));


        classesContent.clear(); packagesContent.clear();
        directory = PS.getPackageDirectory("store");
        classesContent.add(PS.getClassFile("Customer.class"));
        classesContent.add(PS.getClassFile("Pizzaiolo.class"));
        classesContent.add(PS.getClassFile("Pizzeria.class"));
        assertTrue(isInPackageDirectory(directory, classesContent, packagesContent));


        classesContent.clear(); packagesContent.clear();
        directory = PS.getPackageDirectory("ingredients");
        classesContent.add(PS.getClassFile("Ingredient.class"));
        classesContent.add(PS.getClassFile("Pickles.class"));
        classesContent.add(PS.getClassFile("Tomato.class"));
        assertTrue(isInPackageDirectory(directory, classesContent, packagesContent));


        classesContent.clear(); packagesContent.clear();
        directory = PS.getPackageDirectory("kitchen");
        packagesContent.add(PS.getPackageDirectory("ingredients"));
        classesContent.add(PS.getClassFile("BasePizza.class"));
        classesContent.add(PS.getClassFile("PastaType.class"));
        classesContent.add(PS.getClassFile("Pizza.class"));
        classesContent.add(PS.getClassFile("PizzaSize.class"));
        assertTrue(isInPackageDirectory(directory, classesContent, packagesContent));
    }

    /**
     * Verify that a list of file and a list of directories is in a given PackageDirectory.
     * @param directory The PackageDirectory to analyze.
     * @param files The list of files to verify the presence.
     * @param packages The list of directories to verify the presence.
     * @return true if all files and directories are sons of the given PackageDirectory, false otherwise.
     */
    private boolean isInPackageDirectory(PackageDirectory directory, List<ClassFile> files, List<PackageDirectory> packages) {
        int numberOfProjectComponent = files.size() + packages.size();
        if (numberOfProjectComponent == 0) return true;
        int validComponent = 0;

        List<String> strFiles = new ArrayList<>();
        for (ProjectComponent component: files) strFiles.add(component.getName());
        List<String> strPackages = new ArrayList<>();
        for (ProjectComponent component: packages) strPackages.add(component.getName());

        boolean valid;
        for (ProjectComponent component: directory.getContent()) {
            valid = component instanceof ClassFile && strFiles.contains(component.getName()) ||
                    component instanceof PackageDirectory && strPackages.contains(component.getName());
            validComponent += (valid) ? 1 : 0;
        }

        return validComponent == numberOfProjectComponent;
    }

}