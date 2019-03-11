package fr.ubordeaux.jmetrics.metrics;

import fr.ubordeaux.jmetrics.project.ClassFile;

public class ClassGranule extends Granule {

    public ClassGranule(ClassFile file) {
        this.relatedComponent = file;
        this.name = file.getName();
    }

}
