package fr.ubordeaux.jmetrics.presentation;

import fr.ubordeaux.jmetrics.datastructure.DependencyEdge;
import fr.ubordeaux.jmetrics.datastructure.DirectedGraph;
import fr.ubordeaux.jmetrics.datastructure.GraphConstructor;
import fr.ubordeaux.jmetrics.metrics.Granule;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public abstract class FileGenerator {

    public static void generateDOTFile(String directory, String filePrefix, DirectedGraph<Granule, DependencyEdge> graph) {
        String strDOT = GraphConstructor.getDOTRepresentation(graph);
        writeFile(getFilename(directory, filePrefix, ".dot"), strDOT);
    }

    public static void generateCSVFile(String directory, String filePrefix, HashSet<CSVRepresentable> data) {
        if (data.size() == 0) throw new IllegalStateException("Trying to generate CSV file with empty data.");
        List<String> caption = data.iterator().next().getCSVCaption();
        String strCSV = CSVBuilder.buildContent(caption, data);
        writeFile(getFilename(directory, filePrefix, ".csv"), strCSV);
    }

    private static String getFilename(String directory, String filePrefix, String extension) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-YY-HH-mm-ss");
        String formattedDate = dateFormat.format(new Date());
        if(!directory.endsWith(File.separator)) directory += File.separator;
        return directory + filePrefix + "_" + formattedDate + extension;
    }

    private static void writeFile(String filename, String CSVFormattedData) {
        BufferedWriter br = null;
        try {
            br = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8));
            br.write(CSVFormattedData);
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try {
                if(br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
