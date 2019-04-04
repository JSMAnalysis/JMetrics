package fr.ubordeaux.jmetrics;

import fr.ubordeaux.jmetrics.analysis.*;
import fr.ubordeaux.jmetrics.graph.DependencyEdge;
import fr.ubordeaux.jmetrics.graph.DirectedGraph;
import fr.ubordeaux.jmetrics.graph.GraphConstructor;
import fr.ubordeaux.jmetrics.metrics.*;
import fr.ubordeaux.jmetrics.presentation.FileGenerator;
import fr.ubordeaux.jmetrics.project.*;
import org.apache.commons.cli.*;

import java.io.File;
import java.util.*;

public class Main {

    private static final String PATH_OPTION = "p";
    private static final String PATH_OPTION_LONG = "path";
    private static final String PATH_DESC = "Specifies the root of the project to analyze";

    private static final String TYPE_OPTION = "t";
    private static final String TYPE_OPTION_LONG = "type";
    private static final String TYPE_DESC = "Indicates whether the program should perform a bytecode or source analysis";

    private static final String HELP_OPTION = "h";
    private static final String HELP_OPTION_LONG = "help";
    private static final String HELP_DESC = "Display help message";

    private static final String SUBDIR_OPTION = "s";
    private static final String SUBDIR_OPTION_LONG = "subdir";
    private static final String SUBDIR_DESC = "Specifies a subdirectory to restrict the analyse";

    private static final String OUTDIR_OPTION = "o";
    private static final String OUTDIR_OPTION_LONG = "output";
    private static final String OUTDIR_DESC = "Specifies the directory in which to write the output files. Defaults to report/.";

    private static final String VERBOSE_OPTION = "v";
    private static final String VERBOSE_OPTION_LONG = "verbose";
    private static final String VERBOSE_DESC = "Enables verbose mode. The program will display some information about its actions";

    private static final String PROGRAM_USAGE = "jmetrics -t <source|bytecode> -p <project_root> [OPTIONS]";

    private static String path;
    private static String subdirectory;
    private static String outputPath = "report/";
    private static boolean verbose = false;
    private static FileSystemExplorer explorer;
    private static ParserFactory parserFactory;

    public static void main(String[] args) {

        // Initialization
        parseCommandLine(args);
        createOutputPath(outputPath);

        // Execute Pipeline
        projectExploration(path, subdirectory);
        ProjectStructure structure = ProjectStructure.getInstance();
        List<ClassFile> classes = structure.getClasses();
        List<PackageDirectory> packages = structure.getPackages();
        Map<ClassFile, AbstractnessData> aData = abstractnessAnalysis(classes);
        List<Dependency> classDependencies = couplingAnalysis(classes);

        GranuleManager gManager = new GranuleManager(classes, packages);
        Set<Granule> classNodes = gManager.getClassGranules();
        List<Granule> listClassGranules = new ArrayList<>(classNodes);
        Set<Granule> packageNodes = gManager.getPackageGranules();
        List<Granule> listPackageGranules = new ArrayList<>(packageNodes);

        DirectedGraph<Granule, DependencyEdge> classGraph, packageGraph;
        classGraph = GraphConstructor.constructGraph(classNodes, new HashSet<>(classDependencies));
        List<Dependency> packageDependencies = constructPackageDependencies(classDependencies, gManager);
        packageGraph = GraphConstructor.constructGraph(packageNodes, new HashSet<>(packageDependencies));
        int[][] classMatrix = GraphConstructor.getMatrixRepresentation(listClassGranules, classGraph);
        int[][] packageMatrix = GraphConstructor.getMatrixRepresentation(listPackageGranules, packageGraph);

        for (Granule g: classNodes) {
            ClassFile cf = (ClassFile)g.getRelatedComponent();
            MartinMetrics metrics = new MartinMetrics();
            metrics.computeClassMetrics((ClassGranule)g, aData.get(cf), classGraph);
            g.setMetrics(metrics);
        }
        for (Granule g: packageNodes) {
            MartinMetrics metrics = new MartinMetrics();
            metrics.computePackageMetrics((PackageGranule)g, packageGraph);
            g.setMetrics(metrics);
        }

        FileGenerator generator = new FileGenerator(outputPath);
        generator.generateCSVFile("ClassScale", new HashSet<>(classNodes));
        generator.generateCSVFile("PackageScale", new HashSet<>(packageNodes));
        generator.generateDOTFile("ClassScale", classGraph);
        generator.generateDOTFile("PackageScale", packageGraph);
        generator.generateCSVFile("ClassScaleDependencies", new HashSet<>(classDependencies));
        generator.generateCSVFile("PackageScaleDependencies", new HashSet<>(packageDependencies));
        generator.generateCSVFile("classScaleMatrix", classMatrix, listClassGranules);
        generator.generateCSVFile("packageScaleMatrix", packageMatrix, listPackageGranules);

    }



    private static void projectExploration(String path, String subdirectory) {
        if (verbose) {
            System.out.println("Exploring files tree.");
        }
        try {
            ProjectStructure structure = ProjectStructure.getInstance();
            structure.setStructure(explorer.generateStructure(path, subdirectory));
            if(structure.getClasses().size() == 0){
                System.out.println("Directory " + subdirectory + " does not contain any class");
                System.exit(1);
            }
        } catch (InvalidProjectPathException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    private static Map<ClassFile, AbstractnessData> abstractnessAnalysis(List<ClassFile> classes) {
        Map<ClassFile, AbstractnessData> aData = new HashMap<>();
        AbstractnessParser aParser = parserFactory.getAbstractnessParser();
        for (ClassFile c: classes) {
            if (verbose) {
                System.out.println("Parsing abstractness of " + c.getFullyQualifiedName());
            }
            aData.put(c, aParser.getAbstractnessData(c));
        }
        return aData;
    }

    private static List<Dependency> couplingAnalysis(List<ClassFile> classes) {
        List<Dependency> dependencies = new ArrayList<>();
        CouplingParser cParser = parserFactory.getCouplingParser();
        for (ClassFile c: classes) {
            if (verbose) {
                System.out.println("Parsing dependencies of " + c.getFullyQualifiedName());
            }
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
            if (!srcParent.equals(dstParent)) {
                packageDependencies.add(new Dependency(srcParent, dstParent, dep.getType()));
            }
        }
        return packageDependencies;
    }

    private static void createOutputPath(String path){
        File outputFile = new File(path);
        if (!outputFile.exists() && ! outputFile.mkdirs()) {
            System.out.println("Unable to create the output directory : " + path);
            System.exit(1);
        }
    }

    private static void parseCommandLine(String[] args) {
        Options options = buildOptions();

        // First checks if the command line contains the help option.
        if (hasHelp(args)) {
            printHelpAndExit(options, 0);
        }

        CommandLineParser parser = new DefaultParser();
        CommandLine line = null;
        try {
            line = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println("Parsing failed : " + e.getMessage());
            printHelpAndExit(options, 1);
        }

        assert line != null;
        path = line.getOptionValue(PATH_OPTION);
        subdirectory = line.hasOption(SUBDIR_OPTION) ? line.getOptionValue(SUBDIR_OPTION) : path;
        verbose = line.hasOption(VERBOSE_OPTION);
        if (line.hasOption(OUTDIR_OPTION)) {
            outputPath = line.getOptionValue(OUTDIR_OPTION);
        }

        if (line.getOptionValue(TYPE_OPTION).equals("source")) {
            explorer = new SourceFileSystemExplorer();
            parserFactory = new JDTParserFactory();
        } else if (line.getOptionValue(TYPE_OPTION).equals("bytecode")) {
            explorer = new BytecodeFileSystemExplorer();
            parserFactory = new IntrospectionParserFactory();
        } else {
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
        Option parsingType =  Option.builder(TYPE_OPTION).required().longOpt(TYPE_OPTION_LONG).numberOfArgs(1).desc(TYPE_DESC).build();
        Option help = Option.builder(HELP_OPTION).longOpt(HELP_OPTION_LONG).hasArg(false).desc(HELP_DESC).build();
        Option subdir = Option.builder(SUBDIR_OPTION).longOpt(SUBDIR_OPTION_LONG).hasArg().numberOfArgs(1).desc(SUBDIR_DESC).build();
        Option outputDir = Option.builder(OUTDIR_OPTION).longOpt(OUTDIR_OPTION_LONG).hasArg().numberOfArgs(1).desc(OUTDIR_DESC).build();
        Option verbose = Option.builder(VERBOSE_OPTION).longOpt(VERBOSE_OPTION_LONG).hasArg(false).desc(VERBOSE_DESC).build();

        options.addOption(path);
        options.addOption(parsingType);
        options.addOption(help);
        options.addOption(subdir);
        options.addOption(outputDir);
        options.addOption(verbose);
        return options;
    }

    private static boolean hasHelp(String[] args) {
        Options options = new Options();
        options.addOption(HELP_OPTION, HELP_OPTION_LONG, false, HELP_DESC);
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e){
            return false;
        }
        return cmd.hasOption(HELP_OPTION);
    }

}
