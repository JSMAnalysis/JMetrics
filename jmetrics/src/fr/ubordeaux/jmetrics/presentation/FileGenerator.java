package fr.ubordeaux.jmetrics.presentation;

import fr.ubordeaux.jmetrics.datastructure.DependencyEdge;
import fr.ubordeaux.jmetrics.datastructure.DirectedGraph;
import fr.ubordeaux.jmetrics.datastructure.GraphConstructor;
import fr.ubordeaux.jmetrics.metrics.Granule;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public abstract class FileGenerator {

    private final static String REPORT_DIRECTORY = "report/";

    public static void generateDOTFile(String fileInfo, DirectedGraph<Granule, DependencyEdge> graph) {
        String strDOT = GraphConstructor.getDOTRepresentation(graph);
        writeFile(getFilename(fileInfo, ".dot"), strDOT);
    }

    public static void generateCSVFile(String fileInfo, HashSet<CSVRepresentable> data) {
        if (data.size() == 0) throw new IllegalStateException("Trying to generate CSV file with empty data.");
        List<String> caption = data.iterator().next().getCSVCaption();
        String strCSV = CSVBuilder.buildContent(caption, data);
        writeFile(getFilename(fileInfo, ".csv"), strCSV);
    }

    private static String getFilename(String fileInfo, String extension) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-YY-hh-mm-ss");
        String formattedDate = dateFormat.format(new Date());
        return REPORT_DIRECTORY + fileInfo + "_" + formattedDate + extension;
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
                assert br != null;
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
