package fr.ubordeaux.jmetrics.metrics;

import fr.ubordeaux.jmetrics.project.ClassFile;
import fr.ubordeaux.jmetrics.project.PackageDirectory;
import fr.ubordeaux.jmetrics.project.ProjectComponent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Provides util function to manage the granules of the project.
 */
public class GranuleManager {

    private Set<Granule> classGranules;
    private Set<Granule> packageGranules;

    public GranuleManager(List<ClassFile> classes, List<PackageDirectory> packages) {
        setGranulesFromComponents(classes, packages);
    }

    public Granule getParentGranule(Granule g) {
        for (Granule packageG: packageGranules) {
            for (Granule innerG: ((PackageGranule)packageG).getContent()) {
                if (innerG.equals(g)) {
                    return packageG;
                }
            }
        }
        return null;
    }

    private void setGranulesFromComponents(List<ClassFile> classes, List<PackageDirectory> packages) {
        // Create granule instance
        classGranules = new HashSet<>();
        for (ClassFile c: classes) classGranules.add(new ClassGranule(c));
        packageGranules = new HashSet<>();
        for (PackageDirectory p: packages) packageGranules.add(new PackageGranule(p));

        // Set content of PackageGranule
        Granule packageGranule, innerGranule;
        Stream<Granule> stream;
        for (PackageDirectory p: packages) {
            packageGranule = packageGranules.stream().filter(e -> e.getRelatedComponent().equals(p)).findFirst().orElse(null);
            for (ProjectComponent c: p.getContent()) {
                stream = (c instanceof ClassFile) ? classGranules.stream() : packageGranules.stream();
                innerGranule = stream.filter(e -> e.getRelatedComponent().equals(c)).findFirst().orElse(null);
                if (innerGranule != null && packageGranule != null) {
                    ((PackageGranule)packageGranule).addContent(innerGranule);
                }
            }
        }
    }

    public Set<Granule> getClassGranules() {
        return classGranules;
    }

    public Set<Granule> getPackageGranules() {
        return packageGranules;
    }

}
