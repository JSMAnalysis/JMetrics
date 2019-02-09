package project;

import fr.ubordeaux.jmetrics.project.FileSystemExplorer;
import fr.ubordeaux.jmetrics.project.ProjectComponent;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

public class FileSystemExplorerTest {
    FileSystemExplorer f;
    File file;
    public final static String pathName = "out/test/ground_truth";

    @Before
    public void setUp() throws Exception {
        f = new FileSystemExplorer();
        file = new File(pathName);
    }

    @Test
    public void testImportFileFromPathWithCorrectPath() {
        f.generateStructure(pathName);
        assertTrue(true);
    }

    @Test
    public void testImportFileFromPathWithInvalidPath() {
        try {
            f.generateStructure("out/test/toto");
            assertTrue(false);
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testValidPackageName() {
        List<ProjectComponent> pcList = f.getRecursiveStructure(file);
        for (ProjectComponent l : pcList) {
            if (l.getName().equals("example1")) {
                assertTrue(true);
                return;
            }
            fail();
        }
    }

    @Test
    public void testCountOfFile() {
        int i = 0;
        List<ProjectComponent> list = f.getRecursiveStructure(file);
        for (ProjectComponent ignored : list) {
            i++;
        }
        assertEquals(i, list.size());
    }

}