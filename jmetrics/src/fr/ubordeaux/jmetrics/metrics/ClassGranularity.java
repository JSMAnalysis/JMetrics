package fr.ubordeaux.jmetrics.metrics;

import fr.ubordeaux.jmetrics.project.ClassFile;

public class ClassGranularity extends GranularityScale {

    public ClassGranularity(ClassFile file) {
        super(file.getName());
    }

}
