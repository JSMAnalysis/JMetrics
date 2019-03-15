package fr.ubordeaux.jmetrics.presentation;

import java.util.List;
import java.util.Set;

abstract class CSVBuilder {

    private final static char DELIMITER = ';'; // Use semicolon character for Excel support

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

}
