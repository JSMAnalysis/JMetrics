package fr.ubordeaux.jmetrics.presentation;

import fr.ubordeaux.jmetrics.graph.DependencyEdge;
import fr.ubordeaux.jmetrics.graph.DirectedGraph;
import fr.ubordeaux.jmetrics.graph.GraphConstructor;
import fr.ubordeaux.jmetrics.metrics.Granule;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class FileGenerator {

    private String directory;

    private static final String DOT_EXTENSION = ".dot";
    private static final String CSV_EXTENSION = ".csv";

    public FileGenerator(String directory) {
        this.setDirectory(directory);
    }

    private void setDirectory(String directory) {
        if (!directory.endsWith(File.separator)) directory += File.separator;
        this.directory = directory;
    }

    public void generateDOTFile(String filePrefix, DirectedGraph<Granule, DependencyEdge> graph) {
        String strDOT = GraphConstructor.getDOTRepresentation(graph);
        writeFile(getFilename(filePrefix, DOT_EXTENSION), strDOT);
    }

    public void generateCSVFile(String filePrefix, HashSet<CSVRepresentable> data) {
        String strCSV = "";
        if (data.size() != 0) {
            List<String> caption = data.iterator().next().getCSVCaption();
            strCSV = CSVBuilder.buildContent(caption, data);
        }
        writeFile(getFilename(filePrefix, CSV_EXTENSION), strCSV);
    }

    public void generateCSVFile(String filePrefix, int[][] matrix, List<Granule> granules) {
        List<String> caption = granules.stream().map(Granule::getDisplayName).collect(Collectors.toList());
        String strCSV = CSVBuilder.buildContent(caption, matrix);
        writeFile(getFilename(filePrefix, CSV_EXTENSION), strCSV);
    }

    private String getFilename(String filePrefix, String extension) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-YY-HH-mm-ss");
        String formattedDate = dateFormat.format(new Date());
        return directory + filePrefix + "_" + formattedDate + extension;
    }

    private void writeFile(String filename, String CSVFormattedData) {
        BufferedWriter br = null;
        try {
            br = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8));
            br.write(CSVFormattedData);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
