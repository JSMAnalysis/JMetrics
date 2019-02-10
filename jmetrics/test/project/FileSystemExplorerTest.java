package project;

import fr.ubordeaux.jmetrics.project.FileSystemExplorer;
import fr.ubordeaux.jmetrics.project.InvalidProjectPathException;
import fr.ubordeaux.jmetrics.project.ProjectComponent;

import fr.ubordeaux.jmetrics.project.ProjectStructure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileSystemExplorerTest {

    private FileSystemExplorer explorer;
    private final static String groundTruthPath = "out/test/classes/ground_truth/";
    private final static String projectExample1Path = groundTruthPath + "example1/";
    private final static int projectExample1NumberOfClasses = 6;

    @BeforeEach
    void setUp() {
        explorer = new FileSystemExplorer();
    }



    @Test
    void testImportProjectWithCorrectPath() {
        try {
            ProjectComponent rootComponent = explorer.generateStructure(projectExample1Path);
        } catch(InvalidProjectPathException e) {
            fail("The given path lead to existing directory but InvalidProjectPathException have been thrown.");
        }
    }

    @Test
    void testImportProjectWithWrongPath() {
        assertThrows(InvalidProjectPathException.class, ()->{
            String invalidPath = "pompadour/42/";
            ProjectComponent rootComponent = explorer.generateStructure(invalidPath);
        });
    }



    @Test
    void testComponentsName() {

    }



    @Test
    void testClassFileCount() {
        ProjectComponent rootComponent = explorer.generateStructure(projectExample1Path);
        ProjectStructure.getInstance().setStructure(rootComponent);
        int numberOfClassesInGeneratedStructure = ProjectStructure.getInstance().getClasses().size();
        assertEquals(projectExample1NumberOfClasses, numberOfClassesInGeneratedStructure);
    }

    @Test
    void testPackageDirectoryCount() {
        // Function not yet implemented.
    }

}