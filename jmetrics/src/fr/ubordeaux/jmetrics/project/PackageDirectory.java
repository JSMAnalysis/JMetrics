package fr.ubordeaux.jmetrics.project;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a package directory (set of files and sub-directories).
 */
public class PackageDirectory extends ProjectComponent {

    private List<ProjectComponent> content;

    public PackageDirectory(File file) {
        super(file);
        content = new ArrayList<>();
    }

    public List<ProjectComponent> getContent() {
        return new ArrayList<>(content);
    }

    public void setContent(List<ProjectComponent> content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PackageDirectory that = (PackageDirectory) o;
        return content.equals(that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), content);
    }
}
