package fr.ubordeaux.jmetrics.metrics;

import fr.ubordeaux.jmetrics.project.PackageDirectory;

import java.util.List;

public class PackageGranule extends Granule {

    private List<ClassGranule> content;

    private int depth;

    public PackageGranule(PackageDirectory directory) {
        super(directory.getName());
        depth = directory.getDepth();
        // Manage content
    }

    public int getDepth() {
        return depth;
    }

    public List<ClassGranule> getContent() {
        return content;
    }

}
