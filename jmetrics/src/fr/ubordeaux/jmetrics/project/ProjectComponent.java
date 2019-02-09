package fr.ubordeaux.jmetrics.project;

import java.io.File;

/**
 * Represents node of a Project Structure (.class file or package)
 */
public abstract class ProjectComponent {

    protected String name;
    protected File file;
    protected String path;

    public ProjectComponent(File file) {
        this.file = file;
        this.path = file.getPath();
        this.name = file.getName();
    }

    public String getName() {
        return name;
    }

    public File getFile() {
        return file;
    }

    public String getPath() {
        return path;
    }

}
