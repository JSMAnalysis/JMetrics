package fr.ubordeaux.jmetrics.project;

import java.io.File;
import java.util.Objects;

/**
 * Represents a node of a Project Structure (.class file or package)
 */
public abstract class ProjectComponent {

    private String name;
    private File file;
    private String path;

    public ProjectComponent(File file, String name) {
        this.file = file;
        this.path = file.getPath();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getFile() {
        return file;
    }

    public String getPath() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectComponent that = (ProjectComponent) o;
        return name.equals(that.name) &&
                file.equals(that.file) &&
                path.equals(that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, file, path);
    }

}
