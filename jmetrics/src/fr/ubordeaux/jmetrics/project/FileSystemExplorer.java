package fr.ubordeaux.jmetrics.project;

import java.io.File;

/**
 * Service that traverses directories to generate Project Structure.
 */
public class FileSystemExplorer {
    public final static String CLASS_EXTENSION = ".class";


    public void generateStructure(String path) {
        File file = new File(path);
        getFileFrom(file);
    }

    /**
     * Retrieve name of all the packages and classes based on the path
     * @param path Relative Path of the file
     */
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
                System.out.println("Package : "  + f.getName() ); // DEBUG
                new PackageDirectory(f.getName());
                getFileFrom(f);
            }
            if (f.isFile() && validClassName(f.getName())) {
                String fileName = f.getName().substring(0, f.getName().lastIndexOf("."));
                new ClassFile(currentPath + "/"+ fileName, f);
                System.out.println("Class : " + currentPath + "/"+ fileName); // DEBUG
            }
        }
    }

    /**
     *
     * @param name A filename.
     * @return True if the filename starts with a dot, False otherwise.
     */
    private boolean validPackageName(String name) {
        return !name.startsWith(".");
    }

    /**
     *
     * @param name A filename.
     * @return True if the filename ends with the .class extension, False otherwise.
     */
    private boolean validClassName(String name) {
        return name.endsWith(CLASS_EXTENSION);
    }

    /**
     *
     * @param path Path of a file.
     * @return true if the path is valid (lead to a file) and if that is a directory (package), False otherwise.
     */
    private boolean isValidPath(String path) {
        File file = new File(path);
        return file.exists() && file.isDirectory();
    }

}
