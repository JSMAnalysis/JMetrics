package fr.ubordeaux.jmetrics.metrics;

import fr.ubordeaux.jmetrics.project.PackageDirectory;

import java.util.ArrayList;
import java.util.List;

public class PackageGranule extends Granule {

    private List<Granule> content;
    private int depth;

    public PackageGranule(PackageDirectory directory) {
        this.relatedComponent = directory;
        this.name = directory.getName();
        this.depth = directory.getDepth();
        this.content = new ArrayList<>();
    }

    public void addContent(Granule granule) {
        content.add(granule);
    }

    public int getDepth() {
        return depth;
    }

    public List<Granule> getContent() {
        return content;
    }

}
