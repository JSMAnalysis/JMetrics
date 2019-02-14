package fr.ubordeaux.jmetrics.metrics;

import fr.ubordeaux.jmetrics.project.PackageDirectory;

public class PackageGranularity extends GranularityScale {

    public PackageGranularity(PackageDirectory directory) {
        super(directory.getName());
    }

}
