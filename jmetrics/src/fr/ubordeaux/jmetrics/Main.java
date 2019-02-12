package fr.ubordeaux.jmetrics;

import fr.ubordeaux.jmetrics.analysis.*;
import fr.ubordeaux.jmetrics.datastructure.AbstractnessData;
import fr.ubordeaux.jmetrics.datastructure.DependencyEdge;
import fr.ubordeaux.jmetrics.datastructure.DirectedGraph;
import fr.ubordeaux.jmetrics.datastructure.GraphConstructor;
import fr.ubordeaux.jmetrics.metrics.ClassCategory;
import fr.ubordeaux.jmetrics.metrics.ElementaryCategory;
import fr.ubordeaux.jmetrics.metrics.components.*;
import fr.ubordeaux.jmetrics.project.ClassFile;
import fr.ubordeaux.jmetrics.project.FileSystemExplorer;
import fr.ubordeaux.jmetrics.project.InvalidProjectPathException;
import fr.ubordeaux.jmetrics.project.ProjectStructure;

import java.util.*;

public class Main {

    public static void main(String[] args) {

        if(args.length != 1){
            System.out.println("Invalid number of argument, one needed");
            System.exit(1);
        }
        String path = args[0];

        // Project's exploration
        FileSystemExplorer explorer = new FileSystemExplorer();
        try {
            explorer.generateStructure(path);
        }
        catch(InvalidProjectPathException e){
            System.out.println(e.getMessage());
            System.exit(1);
        }
        //Analysis
        List<ClassFile> classes = ProjectStructure.getInstance().getClasses();

        /// Abstractness analysis
        Map<ClassFile, AbstractnessData> aData = new HashMap<>();
        AbstractnessParser aParser = new IntrospectionAbstractnessParser();
        for (ClassFile c : classes) {
            aData.put(c, new AbstractnessData(aParser.getNumberOfMethods(c), aParser.getNumberOfAbstractMethods(c)));
        }

        /// Coupling analysis
        List<Dependency> dependencies = new ArrayList<>();
        CouplingParser cParser = new IntrospectionCouplingParser();
        for (ClassFile c: classes) {
            dependencies.addAll(cParser.getInheritanceDependencies(c));
            dependencies.addAll(cParser.getAggregationDependencies(c));
            dependencies.addAll(cParser.getSignatureDependencies(c));
            dependencies.addAll(cParser.getInternalDependencies(c));
        }

        // Graph construction
        Set<ClassCategory> nodes = new HashSet<>();
        for (ClassFile c : classes) {
            nodes.add(new ElementaryCategory(c));
        }
        DirectedGraph<ClassCategory, DependencyEdge> graph = (new GraphConstructor()).constructGraph(nodes, new HashSet<>(dependencies));

        // Metrics computation
        MetricsComponent A, CA, CE, I, Dn;
        for (ClassCategory c : nodes) {
            //A = new Abstractness()  TODO
            CA = new AfferentCoupling(graph, c);
            CE = new EfferentCoupling(graph, c);
            I = new Instability(CE.getValue(), CA.getValue());
            //Dn = new NormalizedDistance() TODO


            System.out.println(c.getName());
            //System.out.println("A : "); TODO
            System.out.println("Ca : " + CA.getValue());
            System.out.println("Ce : " + CE.getValue());
            System.out.println("I : " + I.getValue());
            //System.out.println("Dn : " + Dn.getValue()); TODO
        }

        System.out.println("The full execution pipeline is not currently implemented");
    }

}
