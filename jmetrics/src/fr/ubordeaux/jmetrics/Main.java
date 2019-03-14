package fr.ubordeaux.jmetrics;

import fr.ubordeaux.jmetrics.analysis.*;
import fr.ubordeaux.jmetrics.datastructure.*;
import fr.ubordeaux.jmetrics.metrics.*;
import fr.ubordeaux.jmetrics.presentation.CSVGenerator;
import fr.ubordeaux.jmetrics.presentation.CSVRepresentable;
import fr.ubordeaux.jmetrics.project.*;
import org.apache.commons.cli.*;

import java.util.*;

public class Main {

    private static final String DOTONLY_OPTION = "dotonly";
    private static final String DOTONLY_DESC = "Only prints the dot-formated graphs";
    private static final String PATH_OPTION = "p";
    private static final String PATH_OPTION_LONG = "path";
    private static final String PATH_DESC = "Specifies the root of the project to analyze";
    private static final String TYPE_OPTION = "t";
    private static final String TYPE_OPTION_LONG = "type";
    private static final String TYPE_DESC = "Indicates whether the program should perform a bytecode or source analysis";
    private static final String HELP_OPTION = "h";
    private static final String HELP_OPTION_LONG = "help";
    private static final String HELP_DESC = "Prints help";

    private static final String PROGRAM_USAGE = "jmetrics -t <source|bytecode> -p <project_root> [OPTIONS]";

    private static String path;
    private static boolean dotonly;
    private static FileSystemExplorer explorer;
    private static ParserFactory parserFactory;

    public static void main(String[] args) {

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

        CSVGenerator.generateCSVFile("ClassScale", new HashSet<>(classNodes));
        CSVGenerator.generateCSVFile("PackageScale", new HashSet<>(packageNodes));

        System.out.println("DOT dependency graph (Class scale):");
        System.out.println(GraphConstructor.getDOTRepresentation(classGraph));
        System.out.println("DOT dependency graph (Package scale):");
        System.out.println(GraphConstructor.getDOTRepresentation(packageGraph));

    }



    private static void projectExploration(String path) {
        try {
            ProjectStructure.getInstance().setStructure(explorer.generateStructure(path));
        } catch(InvalidProjectPathException e){
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    private static Map<ClassFile, AbstractnessData> abstractnessAnalysis(List<ClassFile> classes) {
        Map<ClassFile, AbstractnessData> aData = new HashMap<>();
        AbstractnessParser aParser = parserFactory.getAbstractnessParser();
        for (ClassFile c : classes) {
            aData.put(c, aParser.getAbstractnessData(c));
        }
        return aData;
    }

    private static List<Dependency> couplingAnalysis(List<ClassFile> classes) {
        // TODO: (Refactor) Coupling analysis should return Set (instead of List).
        //  (Or rather) Shouldn't if we consider multiple dependencies (which is not actually the case).
        List<Dependency> dependencies = new ArrayList<>();
        CouplingParser cParser = parserFactory.getCouplingParser();
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
        System.out.println(g.getDisplayName());
        System.out.println("\tA : "     + metrics.getAbstractness());
        System.out.println("\tCa : "    + metrics.getAfferentCoupling());
        System.out.println("\tCe : "    + metrics.getEfferentCoupling());
        System.out.println("\tI : "     + metrics.getInstability());
        System.out.println("\tDn : "    + metrics.getNormalizedDistance());
    }

    private static void parseCommandLine(String[] args) {
        CommandLineParser parser = new DefaultParser();
        Options options = buildOptions();
        CommandLine line = null;
        try {
            line = parser.parse(options, args);
        }
        catch (ParseException e) {
            System.out.println("Parsing failed : " + e.getMessage());
            printHelpAndExit(options, 1);
        }

        if(line.hasOption(HELP_OPTION)){
            printHelpAndExit(options, 0);
        }
        dotonly = line.hasOption(DOTONLY_OPTION);
        path = line.getOptionValue(PATH_OPTION);
        if (line.getOptionValue(TYPE_OPTION).equals("source")) {
            explorer = new SourceFileSystemExplorer();
            parserFactory = new JDTParserFactory();
        } else if (line.getOptionValue(TYPE_OPTION).equals("bytecode")) {
            explorer = new BytecodeFileSystemExplorer();
            parserFactory = new IntrospectionParserFactory();
        } else{
            System.out.println("Type option can only be either source or bytecode");
            System.exit(1);
        }
    }

    private static void printHelpAndExit(Options options, int status){
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(PROGRAM_USAGE, options);
        System.exit(status);
    }

    private static Options buildOptions(){
        Options options = new Options();

        Option path = Option.builder(PATH_OPTION).required().longOpt(PATH_OPTION_LONG).numberOfArgs(1).desc(PATH_DESC).build();
        Option dotonly =  Option.builder().longOpt(DOTONLY_OPTION).hasArg(false).desc(DOTONLY_DESC).build();
        Option parsingType =  Option.builder(TYPE_OPTION).required().longOpt(TYPE_OPTION_LONG).numberOfArgs(1).desc(TYPE_DESC).build();
        Option help = Option.builder(HELP_OPTION).longOpt(HELP_OPTION_LONG).hasArg(false).desc(HELP_DESC).build();

        options.addOption(dotonly);
        options.addOption(path);
        options.addOption(parsingType);
        options.addOption(help);
        return options;
    }

}
