package fr.ubordeaux.jmetrics.project;

import java.io.File;
import java.util.Objects;

/**
 * Represents a node of a Project Structure (.class file or package)
 */
public abstract class ProjectComponent {

    private String fullyQualifiedName;
    private String displayName;
    private File file;
    private String path;

    public ProjectComponent(File file, String fullyQualifiedName) {
        this.file = file;
        this.path = file.getPath();
        this.fullyQualifiedName = fullyQualifiedName;
        this.displayName = fullyQualifiedName;
    }

    public String getFullyQualifiedName() {
        return fullyQualifiedName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public File getFile() {
        return file;
    }

    public String getPath() {
        return path;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int computeDepth(){
        return fullyQualifiedName.split(".").length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectComponent that = (ProjectComponent) o;
        return fullyQualifiedName.equals(that.fullyQualifiedName) &&
                file.equals(that.file) &&
                path.equals(that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullyQualifiedName, file, path);
    }

}
