package project;

import fr.ubordeaux.jmetrics.project.*;
import ground_truth.GroundTruthManager;
import ground_truth.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileSystemExplorerTest {

    private FileSystemExplorer byteExplorer, srcExplorer;
    private GroundTruthManager GT;

    private static final int BYTECODE = 1;
    private static final int SOURCE = 2;

    @BeforeEach
    void setUp() {
        GT = new GroundTruthManager();
        byteExplorer = new BytecodeFileSystemExplorer();
        srcExplorer = new SourceFileSystemExplorer();
    }

    @Test
    void testImportProjectWithCorrectPath() {
        try {
            String validProjectPath = GT.getProject(1).getBytecodePath();
            ProjectComponent byteComponent = byteExplorer.generateStructure(validProjectPath);
            ProjectComponent srcComponent = srcExplorer.generateStructure(validProjectPath);
        } catch(InvalidProjectPathException e) {
            fail("The given path lead to existing directory but InvalidProjectPathException have been thrown.");
        }
    }

    @Test
    void testImportProjectWithWrongPath() {
        assertThrows(InvalidProjectPathException.class, ()->{
            String invalidProjectPath = "pompadour/42/";
            ProjectComponent byteComponent = byteExplorer.generateStructure(invalidProjectPath);
            ProjectComponent srcComponent = srcExplorer.generateStructure(invalidProjectPath);
        });
    }

    @Test
    void testClassFileCount() {
        Project srcExample1 = setProjectExplorer(srcExplorer,1);
        Project byteExample1 = setProjectExplorer(byteExplorer,1);

        assertEquals(srcExample1.getNumberOfClasses(), ProjectStructure.getInstance().getClasses().size());
        assertEquals(byteExample1.getNumberOfClasses(), ProjectStructure.getInstance().getClasses().size());
        assertEquals(srcExample1.getNumberOfClasses(), byteExample1.getNumberOfClasses());
    }

    @Test
    void testPackageDirectoryCount() {
        Project srcExample2 = setProjectExplorer(srcExplorer,2);
        Project byteExample2 = setProjectExplorer(byteExplorer,2);

        assertEquals(srcExample2.getNumberOfPackages(), ProjectStructure.getInstance().getPackages().size());
        assertEquals(byteExample2.getNumberOfPackages(), ProjectStructure.getInstance().getPackages().size());
        assertEquals(srcExample2.getNumberOfPackages(), byteExample2.getNumberOfPackages());
    }

    @Test
    void testValidityOfProjectStructure() {
        List<String> classesContent, packagesContent;
        String dirName;

        dirName = "ground_truth.example2";
        classesContent = new ArrayList<String>() {{ add("ground_truth.example2.Main"); }};
        packagesContent = new ArrayList<String>() {{ add("ground_truth.example2.kitchen"); add("ground_truth.example2.store"); }};
        assertTrue(isInDirectory(BYTECODE,dirName,classesContent,packagesContent));
        assertTrue(isInDirectory(SOURCE,dirName,classesContent,packagesContent));

        dirName = "ground_truth.example2.store";
        classesContent = new ArrayList<String>() {{
            add("ground_truth.example2.store.Customer"); add("ground_truth.example2.store.Pizzaiolo"); add("ground_truth.example2.store.Pizzeria");
        }};
        packagesContent = new ArrayList<>();
        assertTrue(isInDirectory(BYTECODE,dirName, classesContent, packagesContent));
        assertTrue(isInDirectory(SOURCE,dirName, classesContent, packagesContent));

        dirName = "ground_truth.example2.kitchen.ingredients";
        classesContent = new ArrayList<String>() {{
            add("ground_truth.example2.kitchen.ingredients.Ingredient"); add("ground_truth.example2.kitchen.ingredients.Pickles"); add("ground_truth.example2.kitchen.ingredients.Tomato");
        }};
        packagesContent = new ArrayList<>();
        assertTrue(isInDirectory(BYTECODE,dirName, classesContent, packagesContent));
        assertTrue(isInDirectory(SOURCE,dirName, classesContent, packagesContent));

        dirName = "ground_truth.example2.kitchen";
        packagesContent = new ArrayList<String>() {{ add("ground_truth.example2.kitchen.ingredients"); }};
        classesContent = new ArrayList<String>() {{
            add("ground_truth.example2.kitchen.BasePizza"); add("ground_truth.example2.kitchen.PastaType"); add("ground_truth.example2.kitchen.Pizza"); add("ground_truth.example2.kitchen.PizzaSize");
        }};
        assertTrue(isInDirectory(BYTECODE,dirName, classesContent, packagesContent));
        assertTrue(isInDirectory(SOURCE,dirName, classesContent, packagesContent));
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
            valid = component instanceof ClassFile && files.contains(component.getFullyQualifiedName()) ||
                    component instanceof PackageDirectory && packages.contains(component.getFullyQualifiedName());
            validComponent += (valid) ? 1 : 0;
        }

        return validComponent == numberOfProjectComponent;
    }

    /**
     * Get Package Directory by fullyQualifiedName.
     * @param name The fullyQualifiedName of the searched PackageDirectory.
     * @return The searched Package Directory if exists, null otherwise.
     */
    private PackageDirectory getPackageDirectory(String name) {
        for (PackageDirectory directory: ProjectStructure.getInstance().getPackages()) {
            if (directory.getFullyQualifiedName().equals(name)) {
                return directory;
            }
        }
        return null;
    }

    /**
     * Generate the project structure based on the FileSystemExplorer type
     * @param explorer The type of the explorer, either ByteCode or SourceCode
     * @param projectNumber The id of the project to load
     * @return the project to analyze
     */
    private Project setProjectExplorer(FileSystemExplorer explorer, int projectNumber) {
        Project project = GT.getProject(projectNumber);
        ProjectComponent component = null;

        if (explorer instanceof BytecodeFileSystemExplorer)
            component = explorer.generateStructure(project.getBytecodePath());

        else if (explorer instanceof SourceFileSystemExplorer)
            component = explorer.generateStructure(project.getSourcePath());

        ProjectStructure.getInstance().setStructure(component);
        return project;
    }

    /**
     * Based on the FileSystemExplorer type, verify that a list of file and a list of directories is in a given PackageDirectory
     * @param explorer Value of the explorer either ByteCode or SourceCode
     * @param dirName Name of the directory
     * @param classesContent Name of the classes within the directory
     * @param packagesContent Name of the packages within the directory
     * @return true if all classes and packages are sons of the given directory, false otherwise.
     */
    private boolean isInDirectory(int explorer, String dirName, List<String> classesContent, List<String> packagesContent) {
        if (explorer == BYTECODE) GT.loadExampleBytecode(2);
        else if (explorer == SOURCE) GT.loadExampleSourcecode(2);

        return isInPackageDirectory(getPackageDirectory(dirName),classesContent,packagesContent);
    }

}