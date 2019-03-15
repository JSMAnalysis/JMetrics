package fr.ubordeaux.jmetrics;

import fr.ubordeaux.jmetrics.analysis.*;
import fr.ubordeaux.jmetrics.datastructure.*;
import fr.ubordeaux.jmetrics.metrics.*;
import fr.ubordeaux.jmetrics.presentation.FileGenerator;
import fr.ubordeaux.jmetrics.project.*;
import org.apache.commons.cli.*;

import java.io.File;
import java.io.IOException;
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

    private static final String PROGRAM_USAGE = "jmetrics -t <source|bytecode> -p <project_root> [OPTIONS]";

    private static String path;
    private static String subdirectory;
    private static FileSystemExplorer explorer;
    private static ParserFactory parserFactory;
    private static String outputPath = "report/";

    public static void main(String[] args) {

        //Initialization
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

        FileGenerator.generateCSVFile(outputPath, "ClassScale", new HashSet<>(classNodes));
        FileGenerator.generateCSVFile(outputPath, "PackageScale", new HashSet<>(packageNodes));
        FileGenerator.generateDOTFile(outputPath, "ClassScale", classGraph);
        FileGenerator.generateDOTFile(outputPath, "PackageScale", packageGraph);

    }



    private static void projectExploration(String path, String subdirectory) {
        try {
            ProjectStructure.getInstance().setStructure(explorer.generateStructure(path, subdirectory));
        } catch (InvalidProjectPathException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    private static Map<ClassFile, AbstractnessData> abstractnessAnalysis(List<ClassFile> classes) {
        Map<ClassFile, AbstractnessData> aData = new HashMap<>();
        AbstractnessParser aParser = parserFactory.getAbstractnessParser();
        for (ClassFile c: classes) {
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

    private static void createOutputPath(String path){
        File outputFile = new File(path);
        if(!outputFile.exists() && ! outputFile.mkdirs()){
            System.out.println("Unable to create the output directory : " + path);
            System.exit(1);
        }
    }

    private static void parseCommandLine(String[] args) {
        CommandLineParser parser = new DefaultParser();
        Options options = buildOptions();
        CommandLine line = null;
        try {
            line = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println("Parsing failed : " + e.getMessage());
            printHelpAndExit(options, 1);
        }

        assert line != null;
        if (line.hasOption(HELP_OPTION)) {
            printHelpAndExit(options, 0);
        }

        path = line.getOptionValue(PATH_OPTION);
        subdirectory = line.hasOption(SUBDIR_OPTION) ? line.getOptionValue(SUBDIR_OPTION) : path;
        if(line.hasOption(OUTDIR_OPTION)) {
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

        options.addOption(path);
        options.addOption(parsingType);
        options.addOption(help);
        options.addOption(subdir);
        options.addOption(outputDir);
        return options;
    }

}
