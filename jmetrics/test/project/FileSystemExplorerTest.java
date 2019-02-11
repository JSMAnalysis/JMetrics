package project;

import fr.ubordeaux.jmetrics.project.FileSystemExplorer;
import fr.ubordeaux.jmetrics.project.InvalidProjectPathException;
import fr.ubordeaux.jmetrics.project.ProjectComponent;

import fr.ubordeaux.jmetrics.project.ProjectStructure;

import ground_truth.GroundTruthManager;

import ground_truth.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
            Project example1 = GT.getProject(0);
            ProjectComponent rootComponent = explorer.generateStructure(example1.getPath());
        } catch(InvalidProjectPathException e) {
            fail("The given path lead to existing directory but InvalidProjectPathException have been thrown.");
        }
    }

    @Test
    void testImportProjectWithWrongPath() {
        assertThrows(InvalidProjectPathException.class, ()->{
            ProjectComponent rootComponent = explorer.generateStructure(GT.invalidGroundTruthPath);
        });
    }



    @Test
    void testComponentsName() {
        // Function not yet implemented.
    }



    @Test
    void testClassFileCount() {
        Project example1 = GT.getProject(0);
        ProjectComponent rootComponent = explorer.generateStructure(example1.getPath());
        ProjectStructure.getInstance().setStructure(rootComponent);
        int numberOfClassesInGeneratedStructure = ProjectStructure.getInstance().getClasses().size();
        assertEquals(example1.getNumberOfClasses(), numberOfClassesInGeneratedStructure);
    }

    @Test
    void testPackageDirectoryCount() {
        // Function not yet implemented.
    }

}