package fr.ubordeaux.jmetrics.project;

/**
 * Represents node of a Project Structure (.class file or package)
 */
public abstract class ProjectComponent {

    private String name;

    public ProjectComponent(String name) {
        this.name = name;
    }


}
