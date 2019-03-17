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

public class FileGenerator {

    private String directory;

    public FileGenerator(String directory){
        this.setDirectory(directory);
    }

    public void setDirectory(String directory){
        if(!directory.endsWith(File.separator)) directory += File.separator;
        this.directory = directory;
    }

    public void generateDOTFile(String filePrefix, DirectedGraph<Granule, DependencyEdge> graph) {
        String strDOT = GraphConstructor.getDOTRepresentation(graph);
        writeFile(getFilename(filePrefix, ".dot"), strDOT);
    }

    public void generateCSVFile(String filePrefix, HashSet<CSVRepresentable> data) {
        String strCSV = "";
        if(data.size() != 0) {
            List<String> caption = data.iterator().next().getCSVCaption();
            strCSV = CSVBuilder.buildContent(caption, data);
        }
        writeFile(getFilename(filePrefix, ".csv"), strCSV);
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
