package fr.ubordeaux.jmetrics.metrics;

import fr.ubordeaux.jmetrics.project.PackageDirectory;

public class PackageCategory extends ClassCategory {

    public PackageCategory(PackageDirectory directory) {
        this.category = directory;
        this.name = directory.getName();
    }

}
