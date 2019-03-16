package fr.ubordeaux.jmetrics.metrics;

import fr.ubordeaux.jmetrics.project.PackageDirectory;

import java.util.ArrayList;
import java.util.List;

public class PackageGranule extends Granule {

    private List<Granule> content;

    public PackageGranule(PackageDirectory directory) {
        super(directory, directory.getFullyQualifiedName(), directory.getDisplayName());
        this.content = new ArrayList<>();
    }

    public void addContent(Granule granule) {
        content.add(granule);
    }

    public List<Granule> getContent() {
        return content;
    }

}
