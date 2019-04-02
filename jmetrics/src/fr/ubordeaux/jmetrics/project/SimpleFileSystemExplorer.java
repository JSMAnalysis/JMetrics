package fr.ubordeaux.jmetrics.project;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

/**
 * An abstraction to factorize code used by {@link FileSystemExplorer} implementations.
 */
public abstract class SimpleFileSystemExplorer implements FileSystemExplorer {

    private final String CODE_FILE_EXTENSION;

    private final String[] IGNORE_FILE = new String[] { "package-info", "module-info" };

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
        if (!isValidPath(path)) {
            throw new InvalidProjectPathException("Path \"" + path + "\" does not exist or does not lead to a directory.");
        }
        else if(!isValidPath(subdirPath)){
            throw new InvalidProjectPathException("Path \"" + subdirPath + "\" does not exist or does not lead to a directory.");
        }

        File subdir = new File(subdirPath);
        File rootDir = new File(path);
        try {
            subdir = subdir.getCanonicalFile();
            rootDir = rootDir.getCanonicalFile();

        } catch (IOException e){
            throw new InvalidProjectPathException("Unable to resolve the directory or subdirectory path", e);
        }

        if (!subdir.getAbsolutePath().startsWith(rootDir.getAbsolutePath())) {
            throw new InvalidProjectPathException(
                    "The subdirectory \"" + subdirPath + "\" is not located inside \"" + path + "\""
            );
        }
        PackageDirectory project = new PackageDirectory(rootDir, "");
        exploreToSubdir(rootDir, project, 0, "", subdir);
        return project.getContent().get(0);
    }

    /**
     * Explores recursively a project's structure to find a subdir from a given file node. This method is used to
     * build package names from the project's root until it find the subdirectory we want to explore.
     * When the targeted subdirectory is found, it performs the recursive exploration on its structure.
     * @param node The {@link File} to explore.
     * @param parent The current root node of the structure to explore.
     * @param depth The depth level of exploration/
     * @param currentName The fully qualified name of the package currently explored.
     * @param targetSubdir The subdirectory to find.
     */
    private void exploreToSubdir(File node, PackageDirectory parent, int depth, String currentName, File targetSubdir){
        if (node.isDirectory()) {
            if (node.equals(targetSubdir)) {
                setRecursiveStructure(node, parent, depth, currentName);
            }
            else {
                String newFileName = nextStepToDirectory(node, targetSubdir);
                File newNode = new File(node, newFileName);
                String packageName = generateComponentName(currentName, node.getName(), depth);
                PackageDirectory dir = new PackageDirectory(node, packageName);
                parent.addContent(dir);
                exploreToSubdir(newNode, dir, depth+1, packageName, targetSubdir);
            }
        }
        else {
            throw new InvalidProjectPathException("The path " + node.getPath() + " leads to a file.");
        }
    }

    /**
     * Retrieves the name of the directory to explore from the current directory.
     * For example, if the current directory is foo and the target directory is foo/bar/foobar, it will return bar.
     * This function assumes that the files are canonical files.
     * @param currentDir The current directory.
     * @param targetDir The target directory.
     * @return The name of the directory to explore from the current directory to get to the target directory.
     */
    private String nextStepToDirectory(File currentDir, File targetDir) {
        String fileName = targetDir.getAbsolutePath();
        // Removes the current directory path from the target's path
        fileName = fileName.replace(currentDir.getAbsolutePath(), "");
        if (fileName.startsWith(File.separator)) {
            fileName = fileName.substring(1);
        }
        if (fileName.contains(File.separator)) {
            // Keeps only the first directory's name in the path
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
            if (isIgnoredFile(className)) return;
            parent.addContent(new ClassFile(node, className));
        } else if (node.isDirectory()) {
            String packageName = generateComponentName(currentName, node.getName(), depth);
            PackageDirectory dir = new PackageDirectory(node, packageName);
            try(DirectoryStream<Path> dirStream = Files.newDirectoryStream(node.toPath())){
                for(Path childPath : dirStream){
                    File child = childPath.toFile();
                    setRecursiveStructure(child, dir, depth+1, packageName);
                }
            }
            catch (IOException e){
                throw new UncheckedIOException(e);
            }
            parent.addContent(dir);
        }
    }

    /**
     * Checks if a filename corresponds to an ignored one.
     * @param filename The filename to check.
     * @return true if it is an ignored filename, false otherwise.
     */
    private boolean isIgnoredFile(String filename) {
        String[] splitClassName = filename.split("\\.");
        return Arrays.asList(IGNORE_FILE).contains(splitClassName[splitClassName.length - 1]);
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
     * @param path The Path to verify.
     * @return true if the path exists and leads to a directory, false otherwise.
     */
    private boolean isValidPath(String path) {
        File file = new File(path);
        return file.exists() && file.isDirectory();
    }

    /**
     * Remove file extension of a filename.
     * @param filename The filename on which remove extension.
     * @return The filename without its extension or filename if the extension was not found.
     */
    private String removeFileExtension(String filename) {
        int extensionIndex = filename.indexOf(CODE_FILE_EXTENSION);
        if (extensionIndex != -1) {
            return filename.substring(0, extensionIndex);
        }
        return filename;
    }

    /**
     * Generates a component's fully qualified name.
     * @param currentName The current fully qualified name's prefix.
     * @param fileName The name of the file containing the component.
     * @param depth The depth currently reached by the exploration.
     * @return The fully qualified name of the component.
     */
    private String generateComponentName(String currentName, String fileName, int depth){
        if (depth == 0) return "";
        if (currentName.isEmpty()) return fileName;
        return currentName + "." + fileName;
    }

}
