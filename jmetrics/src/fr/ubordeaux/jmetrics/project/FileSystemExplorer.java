package fr.ubordeaux.jmetrics.project;

import java.io.File;

/**
 * Service that traverses directories to generate Project Structure.
 */
public class FileSystemExplorer {
    public final static String CLASS_EXTENSION = ".class";

    public void generateStructure(String path) {
        File file = new File(path); // new File(".");  // repertoire courant
        getFileFrom(file);
    }

    private void getFileFrom(File path) {
        if (! isValidPath(path.getPath())) {
            throw new IllegalArgumentException("Path not valid !!");
        }

        String currentPath = path.getAbsoluteFile().getPath();
        if (validPackageName(currentPath)) {
            currentPath = path.getPath() + "";
        }
        File[] files = path.listFiles();
        assert files != null;
        for (File f: files) {
            if (f.isDirectory() && validPackageName(f.getName())) {
                //System.out.println("Package : "  + f.getName() ); // DEBUG
                new PackageDirectory(f.getName());
                getFileFrom(f);
            }
            if (f.isFile() && validClassName(f.getName())) {
                new ClassFile(currentPath + "/"+ f.getName().substring(0, f.getName().lastIndexOf(".")));
                //System.out.println("Class : " + currentPath + "/"+ f.getName().substring(0, f.getName().lastIndexOf(".")));
            }
        }
    }

    private boolean validPackageName(String name) {
        return !name.startsWith(".");
    }

    private boolean validClassName(String name) {
        return name.endsWith(CLASS_EXTENSION);
    }

    private boolean isValidPath(String path) {
        File file = new File(path);
        return file.exists() && file.isDirectory();
    }


}
