package fr.ubordeaux.jmetrics.metrics;

import fr.ubordeaux.jmetrics.project.PackageDirectory;

import java.util.List;

public class PackageGranularity extends GranularityScale {

    private List<ClassGranularity> content;

    private int depth;

    public PackageGranularity(PackageDirectory directory) {
        super(directory.getName());
        depth = directory.getDepth();
        // Manage content
    }

    public int getDepth() {
        return depth;
    }

    public List<ClassGranularity> getContent() {
        return content;
    }

}
