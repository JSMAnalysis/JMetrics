package fr.ubordeaux.jmetrics;

import fr.ubordeaux.jmetrics.analysis.*;
import fr.ubordeaux.jmetrics.datastructure.*;
import fr.ubordeaux.jmetrics.metrics.*;
import fr.ubordeaux.jmetrics.project.*;
import org.apache.commons.cli.*;

import java.util.*;

public class Main {

    private static String path;
    private static boolean dotonly;

    public static void main(String[] args) {

        // Check Arguments
        // TODO: Add an optional command : [--scale=class|package]
        parseCommandLine(args);



        // Execute Pipeline
        projectExploration(path);
        ProjectStructure structure = ProjectStructure.getInstance();
        List<ClassFile> classes = structure.getClasses();
        List<PackageDirectory> packages = structure.getPackages();
        Map<ClassFile, AbstractnessData> aData = abstractnessAnalysis(classes);
        List<Dependency> classDependencies = couplingAnalysis(classes);

        GranuleManager gManager = new GranuleManager(classes, packages);
        Set<Granule> classNodes = gManager.getClassGranules();
        Set<Granule> packageNodes = gManager.getPackageGranules();

        DirectedGraph<Granule, DependencyEdge> classGraph, packageGraph;
        classGraph = GraphConstructor.constructGraph(classNodes, new HashSet<>(classDependencies));
        List<Dependency> packageDependencies = constructPackageDependencies(classDependencies, gManager);
        packageGraph = GraphConstructor.constructGraph(packageNodes, new HashSet<>(packageDependencies));

        for (Granule g: classNodes) {
            ClassFile cf = (ClassFile)g.getRelatedComponent();
            Metrics.computeClassMetrics(g, aData.get(cf), classGraph);
        }
        for (Granule g: packageNodes) {
            Metrics.computePackageMetrics(g, packageGraph);
        }

        if (!dotonly) {
            System.out.println("Metrics values by class :");
            for (Granule g: classNodes) displayMetrics(g);
            System.out.println("Metrics values by packages :");
            for (Granule g: packageNodes) displayMetrics(g);
        }
        System.out.println("DOT dependency graph (Class scale):");
        System.out.println(GraphConstructor.getDOTRepresentation(classGraph));
        System.out.println("DOT dependency graph (Package scale):");
        System.out.println(GraphConstructor.getDOTRepresentation(packageGraph));
    }



    private static void projectExploration(String path) {
        // TODO 1: Determine whether the given project is composed by SourceCode or ByteCode.
        //  => (Setup analysis environment) Instantiate corresponding FileSystemExplorer / analysis classes.
        // TODO 2: The useless package should be removed.
        //  In class graph, each node is a used class.
        //  It's not the case in package graph: a node can be a useless directory.
        FileSystemExplorer explorer = new BytecodeFileSystemExplorer();
        try {
            ProjectStructure.getInstance().setStructure(explorer.generateStructure(path));
        } catch(InvalidProjectPathException e){
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    private static Map<ClassFile, AbstractnessData> abstractnessAnalysis(List<ClassFile> classes) {
        Map<ClassFile, AbstractnessData> aData = new HashMap<>();
        AbstractnessParser aParser = new IntrospectionAbstractnessParser();
        for (ClassFile c : classes) {
            aData.put(c, aParser.getAbstractnessData(c));
        }
        return aData;
    }

    private static List<Dependency> couplingAnalysis(List<ClassFile> classes) {
        // TODO: (Refactor) Coupling analysis should return Set (instead of List).
        //  (Or rather) Shouldn't if we consider multiple dependencies (which is not actually the case).
        List<Dependency> dependencies = new ArrayList<>();
        CouplingParser cParser = new IntrospectionCouplingParser();
        for (ClassFile c: classes) {
            dependencies.addAll(cParser.getDependencies(c));
        }
        return dependencies;
    }

    private static List<Dependency> constructPackageDependencies(List<Dependency> classDependencies, GranuleManager manager) {
        List<Dependency> packageDependencies = new ArrayList<>();
        Granule srcParent, dstParent;
        for (Dependency dep: classDependencies) {
            srcParent = manager.getParentGranule(dep.getSource());
            dstParent = manager.getParentGranule(dep.getDestination());
            if (!srcParent.equals(dstParent))
                packageDependencies.add(new Dependency(srcParent, dstParent, dep.getType()));
        }
        return packageDependencies;
    }

    private static void displayMetrics(Granule g) {
        Metrics metrics = g.getMetrics();
        System.out.println(g.getName());
        System.out.println("\tA : "     + metrics.getAbstractness());
        System.out.println("\tCa : "    + metrics.getAfferentCoupling());
        System.out.println("\tCe : "    + metrics.getEfferentCoupling());
        System.out.println("\tI : "     + metrics.getInstability());
        System.out.println("\tDn : "    + metrics.getNormalizedDistance());
    }

    private static void parseCommandLine(String[] args){
        CommandLineParser parser = new DefaultParser();
        CommandLine line = null;
        try{
            line = parser.parse(buildOptions(), args);
        }
        catch(ParseException e){
            System.out.println("Parsing failed : " + e.getMessage());
            System.exit(1);
        }

        dotonly = line.hasOption("dotonly");
        path = line.getOptionValue("p");
    }

    private static Options buildOptions(){
        Options options = new Options();
        Option dotonly = new Option("dotonly", "dotonly", false
                , "outputs only the dot formated graph");
        Option path = Option.builder("p")
                            .required()
                            .longOpt("path")
                            .numberOfArgs(1)
                            .build();

        /*Option = new Option().;
        Option = new Option();
        Option = new Option();
        Option = new Option();
        Option = new Option();
        Option = new Option()*/











        options.addOption(dotonly);
        options.addOption(path);
        return options;
    }

}
