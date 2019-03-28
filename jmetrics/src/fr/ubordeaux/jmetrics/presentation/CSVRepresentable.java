package fr.ubordeaux.jmetrics.presentation;

import java.util.List;

public interface CSVRepresentable {

    /**
     * Get the caption of the CSV representation of the element.
     * @return The List of Strings that compose the CSV caption.
     */
    List<String> getCSVCaption();

    /**
     * Get the exposed data of the element.
     * @return The List of Strings that compose the CSV exposed data.
     */
    List<String> getCSVExposedData();

}
