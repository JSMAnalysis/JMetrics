package fr.ubordeaux.jmetrics.project;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * An abstraction to factorize code used by {@link FileSystemExplorer} implementations.
 */
public abstract class SimpleFileSystemExplorer implements FileSystemExplorer {

    private final String CODE_FILE_EXTENSION;

    SimpleFileSystemExplorer(String codeFileExtension) {
        CODE_FILE_EXTENSION = codeFileExtension;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProjectComponent generateStructure(String path) {
        return generateStructure(path, path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProjectComponent generateStructure(String path, String subdirPath) throws InvalidProjectPathException {
        if (!isValidPath(path) || !isValidPath(subdirPath)) {
            throw new InvalidProjectPathException("Path \"" + path + "\" does not exist or does not lead to a directory.");
        }
        File subdir = new File(subdirPath);
        File rootDir = new File(path);
        if(!subdir.getAbsolutePath().startsWith(rootDir.getAbsolutePath())){
            throw new InvalidProjectPathException(
                    "The subdirectory \"" + subdirPath + "\" is not located inside \"" + path + "\""
            );
        }
        PackageDirectory project = new PackageDirectory(rootDir, "", 0);
        exploreToSubdir(rootDir, project, 0, "", subdir);
        return project.getContent().get(0);
    }

    /**
     * Explores recursively a project's structure to find a subdir from a given file node.
     * This method is used to build package names from the project's root until it find the subdirectory
     * we want to explore.
     * When the targeted subdirectory is found, it performs the recursive exploration on its structure.
     * @param node The {@link File} to explore.
     * @param parent The current root node of the structure to explore.
     * @param depth The depth level of exploration/
     * @param currentName The fully qualified name of the package currently explored.
     * @param targetSubdir The subdirectory to find.
     */
    private void exploreToSubdir(File node, PackageDirectory parent, int depth, String currentName, File targetSubdir){
        if(node.isDirectory()){
            if(node.equals(targetSubdir)){
                setRecursiveStructure(node, parent, depth, currentName);
            }
            else {
                String newFileName = nextStepToDirectory(node, targetSubdir);
                File newNode = new File(node, newFileName);
                String packageName = generateComponentName(currentName, node.getName(), depth);
                PackageDirectory dir = new PackageDirectory(node, packageName, depth++);
                parent.addContent(dir);
                exploreToSubdir(newNode, dir, depth, packageName, targetSubdir);
            }
        }
        else{
            throw new InvalidProjectPathException(
                    "The path " + node.getPath() + " leads to a file."
            );
        }
    }

    /**
     * Retrieves the name of the directory to explore from the current directory.
     * For example, if the current directory is foo and the target directory is foo/bar/foobar, it will return
     * bar.
     * @param currentDir The current directory.
     * @param targetDir The target directory.
     * @return The name of the directory to explore from the current directory to get to the target directory.
     */
    private String nextStepToDirectory(File currentDir, File targetDir){
        String fileName = targetDir.getAbsolutePath();
        //removes the current directory path from the target's path
        fileName = fileName.replace(currentDir.getAbsolutePath() + File.separator, "");
        if(fileName.contains(File.separator)) {
            //keeps only the first directory's name in the path
            fileName = fileName.substring(0, fileName.indexOf(File.separator));
        }
        return fileName;
    }

    /**
     * Explores recursively a project's structure from a given file node.
     * Set the explored structure in the content of the given PackageDirectory.
     * @param node The {@link File} to explore.
     * @param parent The root node of the structure to explore.
     * @param depth The depth level of exploration.
     * @param currentName The fully qualified name of the package currently explored.
     */
    private void setRecursiveStructure(File node, PackageDirectory parent, int depth, String currentName) {
        if (isCodeFile(node)) {
            String className = generateComponentName(currentName, removeFileExtension(node.getName()), depth);
            parent.addContent(new ClassFile(node, className));
        } else if (node.isDirectory()) {
            String packageName = generateComponentName(currentName, node.getName(), depth);
            PackageDirectory dir = new PackageDirectory(node, packageName, depth++);
            File[] files = node.listFiles();
            if (files == null) {
                throw new UncheckedIOException(new IOException("A problem has occurred while inspecting the given directory."));
            }
            for (File file: files) {
                setRecursiveStructure(file, dir, depth, packageName);
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

    /**
     * Remove file extension of a filename.
     * @param filename The filename on which remove extension.
     * @return The file fullyQualifiedName without its extension or filename if the extension was not found.
     */
    private String removeFileExtension(String filename) {
        int extensionIndex = filename.indexOf(CODE_FILE_EXTENSION);
        if(extensionIndex != -1) {
            return filename.substring(0, extensionIndex);
        }
        return filename;
    }

    private String generateComponentName(String currentName, String fileName, int depth){
        if(depth == 0) return "";
        if (currentName.isEmpty()) return fileName;
        return currentName + "." + fileName;
    }

}
