package fr.ubordeaux.jmetrics.presentation;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

public abstract class CSVGenerator {

    private final static char DELIMITER = ';'; // Use semicolon character for Excel support
    private final static String REPORT_DIRECTORY = "report/";

    public static void generateCSVFile(String fileInfo, Set<CSVRepresentable> data) {
        if (data.size() == 0) throw new IllegalStateException("Trying to generate CSV file with empty data.");
        List<String> caption = data.iterator().next().getCSVCaption();
        String strCSV = buildContent(caption, data);
        writeFile(getFilename(fileInfo), strCSV);
    }

    private static String buildContent(List<String> caption, Set<CSVRepresentable> data) {
        StringBuilder strBuilder = new StringBuilder();
        for (String str: caption) {
            strBuilder.append(str);
            strBuilder.append(DELIMITER);
        }
        strBuilder.append("\n");
        for (CSVRepresentable line: data) {
            for (String str: line.getCSVExposedData()) {
                strBuilder.append(str);
                strBuilder.append(DELIMITER);
            }
            strBuilder.append("\n");
        }
        return strBuilder.toString();
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

    private static String getFilename(String fileInfo) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-YYYY_hh-mm-ss");
        String formattedDate = dateFormat.format(new Date());
        return REPORT_DIRECTORY + "jmetrics_" + fileInfo + "_" + formattedDate + ".csv";
    }

}
