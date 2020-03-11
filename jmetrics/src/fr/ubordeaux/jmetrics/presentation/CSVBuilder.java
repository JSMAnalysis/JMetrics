package fr.ubordeaux.jmetrics.presentation;

import java.util.List;
import java.util.Set;

public final class CSVBuilder {

    private final static char DELIMITER = ';'; // Use semicolon character for Excel support

    // Private constructor to prevent instantiation
    private CSVBuilder() { }

    static String buildContent(List<String> caption, Set<CSVRepresentable> data) {
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

    static String buildContent(List<String> matrixCaption, int[][] matrix) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(DELIMITER); // Starting in (1, 0)
        for (String str: matrixCaption) {
            strBuilder.append(str);
            strBuilder.append(DELIMITER);
        }
        strBuilder.append("\n");
        for (int i = 0; i < matrix.length; i++) {
            strBuilder.append(matrixCaption.get(i));
            strBuilder.append(DELIMITER);
            for (int j = 0; j < matrix.length; j++) {
                strBuilder.append(matrix[i][j]);
                strBuilder.append(DELIMITER);
            }
            strBuilder.append("\n");
        }
        return strBuilder.toString();
    }

}
