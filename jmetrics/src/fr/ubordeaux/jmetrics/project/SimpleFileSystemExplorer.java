package fr.ubordeaux.jmetrics.project;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * An abstraction to factorize code used by {@link FileSystemExplorer} implementations.
 */
public abstract class SimpleFileSystemExplorer implements FileSystemExplorer {

    private final String CODE_FILE_EXTENSION;

    public SimpleFileSystemExplorer(String codeFileExtension) {
        CODE_FILE_EXTENSION = codeFileExtension;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProjectComponent generateStructure(String path) {
        if (!isValidPath(path)) {
            throw new InvalidProjectPathException("Path \"" + path + "\" does not exist or does not lead to a directory.");
        }
        File rootDir = new File(path);
        PackageDirectory project = new PackageDirectory(rootDir, 0);
        setRecursiveStructure(rootDir, project, 0);
        return project.getContent().get(0);
    }

    /**
     * Explore recursively a project's structure from a given file node.
     * Set the explored structure in the content of the given PackageDirectory.
     * @param node The {@link File} to explore.
     * @param parent The root node of the structure to explore
     * @param depth The depth level of exploration.
     */
    private void setRecursiveStructure(File node, PackageDirectory parent, int depth) {
        if (isCodeFile(node)) {
            parent.addContent(new ClassFile(node));
        } else if (node.isDirectory()) {
            PackageDirectory dir = new PackageDirectory(node, depth++);
            File[] files = node.listFiles();
            if (files == null) {
                throw new UncheckedIOException(new IOException("A problem has occurred while inspecting the given directory."));
            }
            for (File file: files) {
                if (isCodeFile(file)) dir.addContent(new ClassFile(file));
                else setRecursiveStructure(file, dir, depth++);
            }
            parent.addContent(dir);
        }
    }

    /**
     * Verify that a file is a class file.
     * @param file The file to verify.
     * @return true if the file is a class file, false otherwise.
     */
    private boolean isCodeFile(File file) {
        return file.isFile() && file.getName().endsWith(CODE_FILE_EXTENSION);
    }

    /**
     * Verify that a given path exists and correspond to a directory.
     * @param path The Path of to verify.
     * @return true if the path exists and leads to a directory, false otherwise.
     */
    private boolean isValidPath(String path) {
        File file = new File(path);
        return file.exists() && file.isDirectory();
    }

}
