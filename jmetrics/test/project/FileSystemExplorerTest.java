package project;

import fr.ubordeaux.jmetrics.project.FileSystemExplorer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FileSystemExplorerTest {
  FileSystemExplorer f;

  @Before
  public void setUp() throws Exception {
    f = new FileSystemExplorer();
  }

  @Test
  public void testImportFileFromPathWithCorrectPath() {
    f.generateStructure("out/test/projects");
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

}