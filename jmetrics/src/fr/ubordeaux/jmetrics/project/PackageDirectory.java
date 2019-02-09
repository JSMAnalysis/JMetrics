package fr.ubordeaux.jmetrics.project;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Represent a package directory (set of files and sub-directories).
 */
public class PackageDirectory extends ProjectComponent {

    private List<ProjectComponent> content;

    public PackageDirectory(File file) {
        super(file);
    }

    public List<ProjectComponent> getContent() {
        return new ArrayList<>(content);
    }

    public void setContent(List<ProjectComponent> content) {
        this.content = content;
    }

}
